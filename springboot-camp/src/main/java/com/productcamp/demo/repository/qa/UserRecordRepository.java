package com.productcamp.demo.repository.qa;

import com.productcamp.demo.model.qa.UserRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * UserRecordRepository
 *
 * @version 1.0
 */
public interface UserRecordRepository extends MongoRepository<UserRecord, String> {
    @Query(value="{'userId': ?0 }", fields="{'record.id': 1, 'record.title': 1, 'record.score': 1, 'updatedAt': 1}", sort = "{'updatedAt': -1}")
    List<UserRecord> findByUserIdOrderByUpdatedAtDesc(Long userId);
}
