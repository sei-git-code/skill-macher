package com.ses.salessupport.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ses.salessupport.entity.JobRequirement;
import com.ses.salessupport.entity.User;

@Repository
public interface JobRequirementRepository extends JpaRepository<JobRequirement, Long> {

    List<JobRequirement> findByStatus(JobRequirement.JobStatus status);

    List<JobRequirement> findByPriority(JobRequirement.Priority priority);

    List<JobRequirement> findByWorkType(JobRequirement.WorkType workType);

    List<JobRequirement> findByCreatedBy(User createdBy);

    List<JobRequirement> findByClientName(String clientName);

    List<JobRequirement> findByStatusAndPriority(JobRequirement.JobStatus status, JobRequirement.Priority priority);

    @Query("SELECT jr FROM JobRequirement jr WHERE jr.budget BETWEEN :minBudget AND :maxBudget")
    List<JobRequirement> findByBudgetRange(@Param("minBudget") BigDecimal minBudget, @Param("maxBudget") BigDecimal maxBudget);

    @Query("SELECT jr FROM JobRequirement jr WHERE jr.startDate >= :startDate")
    List<JobRequirement> findByStartDateAfter(@Param("startDate") LocalDate startDate);

    @Query("SELECT jr FROM JobRequirement jr WHERE jr.requiredSkills LIKE %:skill%")
    List<JobRequirement> findByRequiredSkillsContaining(@Param("skill") String skill);

    @Query("SELECT jr FROM JobRequirement jr WHERE jr.title LIKE %:keyword% OR jr.description LIKE %:keyword% OR jr.clientName LIKE %:keyword%")
    List<JobRequirement> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT COUNT(jr) FROM JobRequirement jr WHERE jr.status = :status")
    Long countByStatus(@Param("status") JobRequirement.JobStatus status);

    @Query("SELECT COUNT(jr) FROM JobRequirement jr WHERE jr.createdBy = :user")
    Long countByCreatedBy(@Param("user") User user);

    @Query("SELECT jr FROM JobRequirement jr ORDER BY jr.createdAt DESC")
    List<JobRequirement> findAllOrderByCreatedAtDesc();

    @Query("SELECT jr FROM JobRequirement jr WHERE jr.status = 'OPEN' ORDER BY jr.priority DESC, jr.createdAt ASC")
    List<JobRequirement> findOpenJobsOrderByPriorityAndCreatedAt();
}
