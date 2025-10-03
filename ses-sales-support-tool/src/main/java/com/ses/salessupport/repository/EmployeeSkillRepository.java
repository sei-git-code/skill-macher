package com.ses.salessupport.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ses.salessupport.entity.Employee;
import com.ses.salessupport.entity.EmployeeSkill;

@Repository
public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkill, Long> {

    List<EmployeeSkill> findByEmployee(Employee employee);

    List<EmployeeSkill> findBySkillName(String skillName);

    List<EmployeeSkill> findBySkillCategory(String skillCategory);

    List<EmployeeSkill> findByProficiencyLevel(Integer proficiencyLevel);

    List<EmployeeSkill> findByIsVerified(Boolean isVerified);

    List<EmployeeSkill> findByEmployeeAndSkillName(Employee employee, String skillName);

    @Query("SELECT es FROM EmployeeSkill es WHERE es.skillName LIKE %:keyword%")
    List<EmployeeSkill> findBySkillNameContaining(@Param("keyword") String keyword);

    @Query("SELECT es FROM EmployeeSkill es WHERE es.proficiencyLevel >= :minLevel")
    List<EmployeeSkill> findByProficiencyLevelGreaterThanEqual(@Param("minLevel") Integer minLevel);

    @Query("SELECT es FROM EmployeeSkill es WHERE es.yearsOfExperience >= :minYears")
    List<EmployeeSkill> findByYearsOfExperienceGreaterThanEqual(@Param("minYears") Integer minYears);

    @Query("SELECT es FROM EmployeeSkill es WHERE es.employee = :employee AND es.proficiencyLevel >= :minLevel")
    List<EmployeeSkill> findByEmployeeAndProficiencyLevelGreaterThanEqual(@Param("employee") Employee employee, @Param("minLevel") Integer minLevel);

    @Query("SELECT DISTINCT es.skillName FROM EmployeeSkill es ORDER BY es.skillName")
    List<String> findDistinctSkillNames();

    @Query("SELECT DISTINCT es.skillCategory FROM EmployeeSkill es WHERE es.skillCategory IS NOT NULL ORDER BY es.skillCategory")
    List<String> findDistinctSkillCategories();

    @Query("SELECT es FROM EmployeeSkill es WHERE es.employee = :employee ORDER BY es.proficiencyLevel DESC, es.yearsOfExperience DESC")
    List<EmployeeSkill> findByEmployeeOrderByProficiencyAndExperience(@Param("employee") Employee employee);

    @Query("SELECT COUNT(es) FROM EmployeeSkill es WHERE es.skillName = :skillName")
    Long countBySkillName(@Param("skillName") String skillName);

    @Query("SELECT COUNT(es) FROM EmployeeSkill es WHERE es.employee = :employee")
    Long countByEmployee(@Param("employee") Employee employee);
}
