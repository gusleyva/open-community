package com.opencommunity.repository;

import com.opencommunity.domain.Project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Project entity.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(value = "select distinct project from Project project left join fetch project.initiatives",
        countQuery = "select count(distinct project) from Project project")
    Page<Project> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct project from Project project left join fetch project.initiatives")
    List<Project> findAllWithEagerRelationships();

    @Query("select project from Project project left join fetch project.initiatives where project.id =:id")
    Optional<Project> findOneWithEagerRelationships(@Param("id") Long id);
}
