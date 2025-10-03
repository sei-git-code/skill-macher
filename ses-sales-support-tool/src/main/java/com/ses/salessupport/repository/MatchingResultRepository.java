package com.ses.salessupport.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ses.salessupport.entity.Employee;
import com.ses.salessupport.entity.JobRequirement;
import com.ses.salessupport.entity.MatchingResult;

@Repository
public interface MatchingResultRepository extends JpaRepository<MatchingResult, Long> {

    List<MatchingResult> findByJobRequirement(JobRequirement jobRequirement);

    List<MatchingResult> findByEmployee(Employee employee);

    List<MatchingResult> findByStatus(MatchingResult.MatchingStatus status);

    List<MatchingResult> findByJobRequirementAndStatus(JobRequirement jobRequirement, MatchingResult.MatchingStatus status);

    List<MatchingResult> findByEmployeeAndStatus(Employee employee, MatchingResult.MatchingStatus status);

    @Query("SELECT mr FROM MatchingResult mr WHERE mr.matchScore >= :minScore ORDER BY mr.matchScore DESC")
    List<MatchingResult> findByMatchScoreGreaterThanEqualOrderByScoreDesc(@Param("minScore") BigDecimal minScore);

    @Query("SELECT mr FROM MatchingResult mr WHERE mr.jobRequirement = :jobRequirement ORDER BY mr.matchScore DESC")
    List<MatchingResult> findByJobRequirementOrderByMatchScoreDesc(@Param("jobRequirement") JobRequirement jobRequirement);

    @Query("SELECT mr FROM MatchingResult mr WHERE mr.employee = :employee ORDER BY mr.matchScore DESC")
    List<MatchingResult> findByEmployeeOrderByMatchScoreDesc(@Param("employee") Employee employee);

    @Query("SELECT mr FROM MatchingResult mr WHERE mr.jobRequirement = :jobRequirement AND mr.matchScore >= :minScore ORDER BY mr.matchScore DESC")
    List<MatchingResult> findByJobRequirementAndMinScoreOrderByScoreDesc(@Param("jobRequirement") JobRequirement jobRequirement, @Param("minScore") BigDecimal minScore);

    @Query("SELECT COUNT(mr) FROM MatchingResult mr WHERE mr.jobRequirement = :jobRequirement")
    Long countByJobRequirement(@Param("jobRequirement") JobRequirement jobRequirement);

    @Query("SELECT COUNT(mr) FROM MatchingResult mr WHERE mr.employee = :employee")
    Long countByEmployee(@Param("employee") Employee employee);

    @Query("SELECT COUNT(mr) FROM MatchingResult mr WHERE mr.status = :status")
    Long countByStatus(@Param("status") MatchingResult.MatchingStatus status);

    @Query("SELECT AVG(mr.matchScore) FROM MatchingResult mr WHERE mr.jobRequirement = :jobRequirement")
    BigDecimal getAverageMatchScoreByJobRequirement(@Param("jobRequirement") JobRequirement jobRequirement);

    @Query("SELECT AVG(mr.matchScore) FROM MatchingResult mr WHERE mr.employee = :employee")
    BigDecimal getAverageMatchScoreByEmployee(@Param("employee") Employee employee);

    @Query("SELECT mr FROM MatchingResult mr ORDER BY mr.createdAt DESC")
    List<MatchingResult> findAllOrderByCreatedAtDesc();

    @Query("SELECT mr FROM MatchingResult mr WHERE mr.status = 'PENDING' ORDER BY mr.createdAt ASC")
    List<MatchingResult> findPendingResultsOrderByCreatedAt();
}
