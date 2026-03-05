package com.nuria.cvplatform.repository;

import com.nuria.cvplatform.model.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findByProfileIdOrderByDisplayOrderAsc(Long profileId);
}