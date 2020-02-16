package com.productcamp.demo.repository.qa;

import com.productcamp.demo.model.qa.UserRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * UserRecordRepository
 *
 * @version 1.0
 */
public interface UserRecordRepository extends MongoRepository<UserRecord, String> {
//    用户历史记录查询
    @Query(value="{'userId': ?0, 'completed': ?1 }", fields="{'id': 1, 'title': 1, 'testId': 1, 'score': 1, 'updatedAt': 1}", sort = "{'updatedAt': -1}")
    List<UserRecord> findByUserIdOrderByUpdatedAtDesc(Long userId, Boolean completed);

//    获取详细的某进行中考卷的用户答题记录(completed=false)
    @Query(value="{'userId': ?0, 'testId': ?1, 'completed': ?2 }", fields="{'id': 1, 'title': 1, 'totalScore': 1, 'timeLimit': 1, 'questionList': 1, 'createdAt': 1}")
    Optional<UserRecord> findByUserIdAndTestIdAndCompleted(Long userId, String testId, Boolean completed);

//    获取考试列表中进行中状态的记录(completed=false)
    @Query(value="{'userId': ?0, 'completed': ?1 }", fields="{'id': 1, 'title': 1, 'timeLimit': 1, 'testId': 1, 'userId': 1, 'createdAt': 1}", sort = "{'updatedAt': -1}")
    List<UserRecord> findByUserIdAndCompletedOrderByUpdatedAtDesc(Long userId, Boolean completed);
}
