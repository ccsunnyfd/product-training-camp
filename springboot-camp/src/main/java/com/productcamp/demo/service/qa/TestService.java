package com.productcamp.demo.service.qa;

import com.productcamp.demo.model.qa.Test;
import com.productcamp.demo.repository.qa.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    public void setTestRepository(TestRepository testRepository) {
        this.testRepository = testRepository;
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

    public List<Test> getTestsByStatusAndCategory(Integer status, Long category) {
//        Criteria criteria = Criteria.where("userStatusList.status").is(status).and("category").is(category);
        List<Test> testList = testRepository.findAll();
        for ( Test t: testList) {
            t.setQuestionList(null);
        }
        return testList;
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
