package com.ses.salessupport.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ses.salessupport.entity.SkillMapping;

@Repository
public interface SkillMappingRepository extends JpaRepository<SkillMapping, Long> {

    Optional<SkillMapping> findBySkillName(String skillName);

    List<SkillMapping> findBySkillCategory(String skillCategory);

    List<SkillMapping> findByIsActive(Boolean isActive);

    List<SkillMapping> findBySkillCategoryAndIsActive(String skillCategory, Boolean isActive);

    @Query("SELECT sm FROM SkillMapping sm WHERE sm.skillName LIKE %:keyword% OR sm.description LIKE %:keyword%")
    List<SkillMapping> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT sm FROM SkillMapping sm WHERE sm.keywords LIKE %:keyword%")
    List<SkillMapping> findByKeywordsContaining(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT sm.skillCategory FROM SkillMapping sm WHERE sm.isActive = true ORDER BY sm.skillCategory")
    List<String> findDistinctActiveSkillCategories();

    @Query("SELECT sm FROM SkillMapping sm WHERE sm.isActive = true ORDER BY sm.skillName")
    List<SkillMapping> findActiveSkillsOrderByName();

    @Query("SELECT sm FROM SkillMapping sm WHERE sm.skillCategory = :category AND sm.isActive = true ORDER BY sm.skillName")
    List<SkillMapping> findActiveSkillsByCategoryOrderByName(@Param("category") String category);

    @Query("SELECT sm FROM SkillMapping sm WHERE sm.skillWeight >= :minWeight ORDER BY sm.skillWeight DESC")
    List<SkillMapping> findBySkillWeightGreaterThanEqualOrderByWeightDesc(@Param("minWeight") Double minWeight);

    @Query("SELECT COUNT(sm) FROM SkillMapping sm WHERE sm.isActive = true")
    Long countActiveSkills();

    @Query("SELECT COUNT(sm) FROM SkillMapping sm WHERE sm.skillCategory = :category AND sm.isActive = true")
    Long countActiveSkillsByCategory(@Param("category") String category);

    boolean existsBySkillName(String skillName);
}
