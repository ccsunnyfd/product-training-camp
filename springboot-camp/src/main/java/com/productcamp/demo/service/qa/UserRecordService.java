package com.productcamp.demo.service.qa;

import com.productcamp.demo.model.qa.*;
import com.productcamp.demo.pojo.qa.TestRecordBO;
import com.productcamp.demo.repository.qa.TestRepository;
import com.productcamp.demo.repository.qa.UserRecordRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * UserRecordService
 *
 * @version 1.0
 */
@Service
public class UserRecordService {
    private UserRecordRepository userRecordRepository;
    private TestRepository testRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    public void setUserRecordRepository(UserRecordRepository userRecordRepository) {
        this.userRecordRepository = userRecordRepository;
    }

    @Autowired
    public void setTestRepository(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    // 用户点击考卷，根据completed情况，判断是否为进行中状态，调用不同的private方法
    public UserRecord clickExam(Long userId, String testId) {
        // 根据userId,testId,completed=false条件查找数据库，看是否有命中
        Optional<UserRecord> userRecordOptional = userRecordRepository.findByUserIdAndTestIdAndCompleted(userId, testId, false);
        UserRecord userRecord = userRecordOptional.orElse(null);
        if (userRecord == null) {
            // 没有进行中的考试，则调用initTestRecord
            UserRecord newRecord = initTestRecord(userId, testId);
            doTask(newRecord.getId(), newRecord.getTimeLimit());
            return newRecord;
        }
            // 如果有进行中的考试，则返回记录的Test
        return userRecord;
    }

    // 定时器任务，用于在考试超时1分钟后，如果依然没有提交，直接对暂存数据进行评分，并将completed置为true
    private void doTask(String recordId, Long timeLimit) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                UserRecord nowRecord = userRecordRepository.findById(recordId).orElse(null);
                if (nowRecord != null && !nowRecord.getCompleted()) {
                    List<Question> nowRecordQuestionList = nowRecord.getQuestionList();

                    // 将从Test中取到的问题列表转成以问题ID为键，问题本身为键值的Map对象，用于作为比对正确答案的参照
                    String testId = nowRecord.getTestId();
                    Test test = testRepository.findById(testId).orElse(null);
                    List<Question> questionList = test.getQuestionList();
                    Map<String, Question> qId2Question = questionList.stream().collect(Collectors.toMap(Question::getId, Function.identity()));

                    // 计分
                    Long score = 0L;
                    TestRecordBO testRecordBO = null;
                    List<Question> qListToSave = new ArrayList<>();
                    for (Question nowQ: nowRecordQuestionList) {
                        String nowQId = nowQ.getId();
                        List<String> nowQCheckedIds = nowQ.getOptionAndRight().getOptionList().stream().filter(Option::getChecked).map(Option::getId).collect(Collectors.toList());
                        Question rightAnswerQuestion = qId2Question.getOrDefault(nowQId, null);
                        if (rightAnswerQuestion != null) {
                            OptionAndRight rightAnswerOptionAndRight = rightAnswerQuestion.getOptionAndRight();
                            List<Option> rightAnswerOptionList = rightAnswerOptionAndRight.getOptionList();
                            // 为每一个选项记录用户是否勾选
                            rightAnswerOptionList.forEach(x -> x.setChecked(nowQCheckedIds.contains(x.getId())));
                            if (nowQCheckedIds.containsAll(rightAnswerOptionAndRight.getRightIds())) {
                                //得分
                                rightAnswerQuestion.setGetPoints(rightAnswerQuestion.getPoints());
                                score += rightAnswerQuestion.getPoints();
                            } else {
                                //不得分
                                rightAnswerQuestion.setGetPoints(0L);
                            }
                            qListToSave.add(rightAnswerQuestion);
                        }
                    }

                    // 把包含questionList历史记录的版本持久化到UserRecord中去
                    nowRecord.setQuestionList(qListToSave);
                    nowRecord.setUpdatedAt(new Date());
                    nowRecord.setCompleted(true);
                    nowRecord.setScore(score);
                    userRecordRepository.save(nowRecord);
                }
            }
        }, timeLimit * 60000 + 10000); //指定启动定时器timeLimit分钟+10秒种之后运行定时器任务run方法，并且只运行一次
    }

    // 用户点击进行中的考卷时，加载进行中的考试数据（包括部分答题记录），返回记录的id
//    private Test getOngoingTest(Long userId, String testId) {
//    }

    // 用户初次点击考卷时，新建用户考试记录, 设置createdAt为当前时间，completed为false，testId为参数， 保存历史记录，返回新记录
    private UserRecord initTestRecord(Long userId, String testId) {
        Test test = testRepository.findById(testId).orElse(null);
        if (test == null) {
            return null;
        }
        Boolean shuffle = test.getShuffle();
        List<Question> questionList = test.getQuestionList();
        for ( Question question: questionList) {
            question.getOptionAndRight().setRightIds(null);
            question.setAnalysis(null);
            question.setGetPoints(0L);
        }
        if (shuffle) {
            Collections.shuffle(questionList);
        }
        UserRecord userRecord = UserRecord.builder()
                .testId(testId)
                .title(test.getTitle())
                .timeLimit(test.getTimeLimit())
                .totalScore(test.getTotalScore())
                .questionList(questionList)
                .userId(userId)
                .createdAt(new Date())
                .updatedAt(new Date())
                .completed(false)
                .build();
        return userRecordRepository.save(userRecord);
    }

    // 处理小程序端用户中途离开，暂存答题记录， 返回记录的id
    public String pauseTest(Long userId, String recordId, Map<String, Set<String>> answerMap) {
        @SuppressWarnings("DuplicatedCode") UserRecord userRecord = userRecordRepository.findById(recordId).orElse(null);
        if (userRecord == null) {
            return null;
        }
        List<Question> questionList = userRecord.getQuestionList();
        for (Question q : questionList) {
            OptionAndRight optionAndRight = q.getOptionAndRight();
            List<Option> optionList = optionAndRight.getOptionList();
            // 为每一个选项记录用户是否勾选
            optionList.forEach(x -> x.setChecked(answerMap.get(q.getId()).contains(x.getId())));
        }
        // 把包含questionList历史记录的版本持久化到UserRecord中去
        userRecord.setQuestionList(questionList);
        userRecord.setUpdatedAt(new Date());
        userRecord.setCompleted(false);
        userRecordRepository.save(userRecord);
        return recordId;
    }

    // 处理小程序端用户的交卷, 保存历史记录，返回试卷得分和记录的id
    public TestRecordBO processSubmittedTest(Long userId, String recordId, Map<String, Set<String>> answerMap) {
        UserRecord userRecord = userRecordRepository.findById(recordId).orElse(null);
        if (userRecord == null) {
            return null;
        }
        String testId = userRecord.getTestId();
        Test test = testRepository.findById(testId).orElse(null);
        List<Question> questionList = test.getQuestionList();
        Long score = 0L;
        TestRecordBO testRecordBO = null;

        // 将从Test中取到的问题列表转成以问题ID为键，问题本身为键值的Map对象，用于作为比对正确答案的参照
        Map<String, Question> qId2Question = questionList.stream().collect(Collectors.toMap(Question::getId, Function.identity()));
        Iterator<Map.Entry<String, Set<String>>> entries = answerMap.entrySet().iterator();
        List<Question> qListToSave = new ArrayList<>();
        while (entries.hasNext()) {
            Map.Entry<String, Set<String>> entry = entries.next();
            String k = entry.getKey();
            Question q = qId2Question.getOrDefault(k, null);
            if (q != null) {
                OptionAndRight optionAndRight = q.getOptionAndRight();
                List<Option> optionList = optionAndRight.getOptionList();
                // 为每一个选项记录用户是否勾选
                optionList.forEach(x -> x.setChecked(entry.getValue().contains(x.getId())));
                if (entry.getValue().containsAll(optionAndRight.getRightIds())) {
                    //得分
                    q.setGetPoints(q.getPoints());
                    score += q.getPoints();
                } else {
                    //不得分
                    q.setGetPoints(0L);
                }
                qListToSave.add(q);
            }
        }

//        for (Question q : questionList) {
//            OptionAndRight optionAndRight = q.getOptionAndRight();
//            List<Option> optionList = optionAndRight.getOptionList();
//            // 为每一个选项记录用户是否勾选
//            optionList.forEach(x -> x.setChecked(answerMap.get(q.getId()).contains(x.getId())));
//            if (answerMap.get(q.getId()).containsAll(optionAndRight.getRightIds())) {
//                //得分
//                q.setGetPoints(q.getPoints());
//                score += q.getPoints();
//            } else {
//                //不得分
//                q.setGetPoints(0L);
//            }
////            userQuestionList.add(q);
//        }

        // 把包含questionList历史记录的版本持久化到UserRecord中去
        userRecord.setQuestionList(qListToSave);
        userRecord.setUpdatedAt(new Date());
        userRecord.setCompleted(true);
        userRecord.setScore(score);
        userRecordRepository.save(userRecord);
        testRecordBO = new TestRecordBO(score, testId, recordId);
        return testRecordBO;
    }

    // 查询当前用户的答题记录列表，返回信息包括recordId、title、score、updatedAt
    public List<UserRecord> getUserRecordList(Long userId) {
        return userRecordRepository.findByUserIdOrderByUpdatedAtDesc(userId, true);
    }

    // 查询当前用户该门考试的答题记录
    public UserRecord getTestRecord(String recordId) {
//        List<UserRecord> testRecords = mongoTemplate.find(
//                new Query(Criteria.where("userId").is(userId).and("testId").is(testId)),
//                UserRecord.class
//        );
        Optional<UserRecord> optionalUserRecord = userRecordRepository.findById(recordId);
        return optionalUserRecord.orElse(null);
    }

}
