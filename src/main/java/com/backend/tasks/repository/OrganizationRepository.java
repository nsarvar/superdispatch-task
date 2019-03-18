package com.backend.tasks.repository;

import com.backend.tasks.entity.Organization;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Sarvar Nishonboyev on Mar 13, 2019
 */
public interface OrganizationRepository extends CrudRepository<Organization, Long> {
    Organization findByName(String name);
}
