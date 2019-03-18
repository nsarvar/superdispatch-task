package com.backend.tasks.repository;

import com.backend.tasks.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Sarvar Nishonboyev on Mar 13, 2019
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndOrgId(Long id, Long orgId);
    List<User> findAllByOrgId(Long orgId);
    User findByUsername(String username);
}
