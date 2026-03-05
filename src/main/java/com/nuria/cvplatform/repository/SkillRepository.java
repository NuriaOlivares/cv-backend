package com.nuria.cvplatform.repository;

import com.nuria.cvplatform.enums.SkillCategory;
import com.nuria.cvplatform.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByProfileIdOrderByDisplayOrderAsc(Long profileId);
    List<Skill> findByProfileIdAndCategoryOrderByDisplayOrderAsc(Long profileId, SkillCategory category);
}