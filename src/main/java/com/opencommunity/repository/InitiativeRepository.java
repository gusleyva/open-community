package com.opencommunity.repository;

import com.opencommunity.domain.Initiative;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Initiative entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InitiativeRepository extends JpaRepository<Initiative, Long> {
}
