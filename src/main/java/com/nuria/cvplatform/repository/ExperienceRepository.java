package com.nuria.cvplatform.repository;

import com.nuria.cvplatform.model.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findByProfileIdOrderByStartDateDesc(Long profileId);
}