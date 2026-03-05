package com.nuria.cvplatform.repository;

import com.nuria.cvplatform.model.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Long> {
    List<Certification> findByProfileIdOrderByDisplayOrderAsc(Long profileId);
}