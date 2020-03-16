package com.productcamp.demo.repository;

import com.productcamp.demo.model.TokenUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TokenUserRepository
 *
 * @version 1.0
 */
public interface TokenUserRepository extends JpaRepository<TokenUser, Long> {
    TokenUser findOneByToken(String token);
}
