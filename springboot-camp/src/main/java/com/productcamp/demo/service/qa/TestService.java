package com.productcamp.demo.service.qa;

import com.productcamp.demo.model.qa.Test;
import com.productcamp.demo.model.qa.UserRecord;
import com.productcamp.demo.repository.qa.TestRepository;
import com.productcamp.demo.repository.qa.UserRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * TestService
 *
 * @version 1.0
 */
@Service
public class TestService {
    @Autowired
    private MongoTemplate mongoTemplate;

    private TestRepository testRepository;
    private UserRecordRepository userRecordRepository;

    @Autowired
    public void setTestRepository(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Autowired
    public void setUserRecordRepository(UserRecordRepository userRecordRepository) {
        this.userRecordRepository = userRecordRepository;
    }

    public Test getTestById(String id) {
        Optional<Test> testOptional = testRepository.findById(id);
        return testOptional.orElse(null);
    }

    // 注意，PageRequest.of的第一个参数表示第几页，从0开始计数，这与通常的分页从1开始有些不同，要处理下
    //动态构造查询语句
    public Page<Test> getPageTests(String keywords, Integer pageNum, Integer
            size) {
//        Specification querySpeci = new Specification() {
//            @Override
//            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                Predicate predicate = null;
//                if (!StringUtils.isEmpty(keywords)) {
//                    Predicate p1 = criteriaBuilder.like(root.get("text"), "%" + keywords + "%");
//                    predicate = criteriaBuilder.or(p1);
//                }
//                return predicate;
//            }
//        };
        Page<Test> page = testRepository.findAll(PageRequest.of(pageNum - 1, size));
        return page;
    }

    // 小程序端根据考试状态获取考试列表（包括进行中的考试记录）
    public Map<String, Object> getTestsByStatusAndCategory(Long userId, Integer status, Long category) {
        // status: 1. 进行中 2. 可参加 3. 全部
//        Criteria criteria = Criteria.where("userStatusList.status").is(status).and("category").is(category);
        Map<String, Object> map = new HashMap<>();
        List<UserRecord> recordList = userRecordRepository.findByUserIdAndCompletedOrderByUpdatedAtDesc(userId, false);
        if (status == 1L) {
            map.put("onGoing", recordList);
        } else {
            if (status == 3L) {
                map.put("onGoing", recordList);
            }
            List<Test> testList = testRepository.findByOrderByUpdatedAtDesc();
            List<String> onGoingTestIdList = recordList.stream().map(UserRecord::getTestId).collect(Collectors.toList());
            testList = testList.stream().filter(x -> !(onGoingTestIdList.contains(x.getId()))).collect(Collectors.toList());
            map.put("ready", testList);
        }
        return map;
    }

    // 插入或更新条目
    public String saveOrUpdateTest(Test test) {
        Test newT;
        if (test.getId() == null) {
            newT = testRepository.insert(test);
        } else {
            newT = testRepository.save(test);
        }
        return newT.getId();
    }

    public void delTest(String id) {
        testRepository.deleteById(id);
    }


}
