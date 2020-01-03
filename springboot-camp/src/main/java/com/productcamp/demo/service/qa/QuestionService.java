package com.productcamp.demo.service.qa;

import com.alibaba.fastjson.JSONObject;
import com.productcamp.demo.model.qa.Question;
import com.productcamp.demo.repository.qa.QuestionRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * QuestionService
 *
 * @version 1.0
 */
@Service
public class QuestionService {
    @Autowired
    private MongoTemplate mongoTemplate;

    private QuestionRepository questionRepository;

    @Autowired
    public void setQuestionRepository(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // 按照题目类型抽样取题
    public List<Document> getSampleListByQtype(Integer qtype, Long sampleSize) {
        Criteria criteria = Criteria.where("qtype").is(qtype);
//        TypedAggregation<Question> agg = Aggregation.newAggregation(Question.class,
//                Aggregation.match(criteria), Aggregation.sample(sampleSize), Aggregation.project("id"));
        TypedAggregation<Question> agg = Aggregation.newAggregation(Question.class,
                Aggregation.match(criteria), Aggregation.sample(sampleSize));
        AggregationResults<Document> result = mongoTemplate.aggregate(agg, Document.class);
//        List<JSONObject> sampleList = result.getMappedResults().stream().map(item -> JSONObject.parseObject(item.toJson())).collect(Collectors.toList());
        List<Document> sampleList = result.getMappedResults();
        for ( Document x: sampleList) {
            x.put("id", x.getObjectId("_id").toString());
            x.remove("_id");
        };
        return sampleList;
    }

    public Question getQuestionById(String id) {
        Optional<Question> questionOptional = questionRepository.findById(id);
        if (questionOptional.isPresent()) {
            return questionOptional.get();
        } else {
            return null;
        }
    }

    // 注意，PageRequest.of的第一个参数表示第几页，从0开始计数，这与通常的分页从1开始有些不同，要处理下
    //动态构造查询语句
    public Page<Question> getPageQuestions(String keywords, Integer pageNum, Integer
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
        Page<Question> page = questionRepository.findAll(PageRequest.of(pageNum - 1, size));
        return page;
    }


    // 插入或更新条目
    public String saveOrUpdateQuestion(Question question) {
        Question newQ;
        if (StringUtils.isEmpty(question.getId())) {
            newQ = questionRepository.insert(question);
        } else {
            newQ = questionRepository.save(question);
        }
        return newQ.getId();
    }

    public void delQuestion(String id) {
        questionRepository.deleteById(id);
    }
}
