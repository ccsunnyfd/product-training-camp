package com.productcamp.demo.repository;

import com.productcamp.demo.model.IdentifiedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * IdentifiedUserRepository
 *
 * @version 1.0
 */
public interface IdentifiedUserRepository extends JpaRepository<IdentifiedUser, Long> {
    IdentifiedUser findOneByPhoneNumber(String phoneNumber);
}
