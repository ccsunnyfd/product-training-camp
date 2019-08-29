package com.productcamp.demo.repository;

import com.productcamp.demo.model.MovieInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * MovieInfoRepository
 *
 * @version 1.0
 */
public interface MovieInfoRepository extends JpaRepository<MovieInfo, String>, JpaSpecificationExecutor {

}
