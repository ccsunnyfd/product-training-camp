package com.productcamp.demo.service.qa;

import com.productcamp.demo.model.qa.*;
import com.productcamp.demo.pojo.qa.SimpleUserRecordBO;
import com.productcamp.demo.pojo.qa.TestRecordBO;
import com.productcamp.demo.repository.qa.UserRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * UserRecordService
 *
 * @version 1.0
 */
@Service
public class UserRecordService {
    private UserRecordRepository userRecordRepository;
    private TestService testService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    public void setUserRecordRepository(UserRecordRepository userRecordRepository) {
        this.userRecordRepository = userRecordRepository;
    }

    @Autowired
    public void setTestService(TestService testService) {
        this.testService = testService;
    }


    // 处理小程序端用户的交卷, 保存历史记录，返回试卷得分和新记录的id
    public TestRecordBO processSubmittedTest(Long userId, String testId, Map<String, Set<String>> answerMap) {
        Test test = testService.getTestById(testId);
        List<Question> questionList = test.getQuestionList();
//        Map<String, Set<String>> question2OptionId = answerBOS.stream().collect(Collectors.toMap(AnswerBO::getQuestionId, AnswerBO::getOptionIds));
//        List<Question> userQuestionList = new ArrayList<>();
        Long score = 0L;
        TestRecordBO testRecordBO = null;
        for (Question q : questionList) {
            OptionAndRight optionAndRight = q.getOptionAndRight();
            List<Option> optionList = optionAndRight.getOptionList();
            // 为每一个选项记录用户是否勾选
            optionList.forEach(x -> x.setChecked(answerMap.get(q.getId()).contains(x.getId())));
            if (answerMap.get(q.getId()).containsAll(optionAndRight.getRightIds())) {
                //得分
                q.setGetPoints(q.getPoints());
                score += q.getPoints();
            } else {
                //不得分
                q.setGetPoints(0L);
            }
//            userQuestionList.add(q);
        }

        // 把包含questionList历史记录的Test版本持久化到UserRecord中去
        test.setScore(score);

        UserRecord addRecord = new UserRecord();
        addRecord.setUserId(userId);
        addRecord.setRecord(test);
        addRecord.setCreatedAt(new Date());
        addRecord.setUpdatedAt(new Date());
        UserRecord newRecord = userRecordRepository.save(addRecord);
        testRecordBO = new TestRecordBO(score, testId, newRecord.getId());
        return testRecordBO;
    }

    // 查询当前用户的答题记录列表，返回信息包括recordId、title、score、updatedAt
    public List<UserRecord> getUserRecordList(Long userId) {
        return userRecordRepository.findByUserIdOrderByUpdatedAtDesc(userId);
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
