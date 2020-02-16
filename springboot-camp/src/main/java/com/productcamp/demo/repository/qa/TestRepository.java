package com.productcamp.demo.repository.qa;

import com.productcamp.demo.model.qa.Test;
import com.productcamp.demo.model.qa.UserRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * TestRepository
 *
 * @version 1.0
 */
public interface TestRepository extends MongoRepository<Test, String> {
//    查询考卷列表
    @Query(fields="{'id': 1, 'title': 1, 'timeLimit': 1, 'updatedAt': 1}", sort = "{'updatedAt': -1}")
    List<Test> findByOrderByUpdatedAtDesc();
}
