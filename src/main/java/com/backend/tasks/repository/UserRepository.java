package com.backend.tasks.repository;

import com.backend.tasks.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Sarvar Nishonboyev on Mar 13, 2019
 */
public interface UserRepository extends CrudRepository<User, Long> {
}
