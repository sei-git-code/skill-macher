package com.ses.salessupport.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ses.salessupport.entity.SystemLog;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {

    List<SystemLog> findByLogLevel(SystemLog.LogLevel logLevel);

    List<SystemLog> findByLogCategory(String logCategory);

    List<SystemLog> findByUserId(String userId);

    List<SystemLog> findByLogLevelAndLogCategory(SystemLog.LogLevel logLevel, String logCategory);

    @Query("SELECT sl FROM SystemLog sl WHERE sl.createdAt BETWEEN :startDate AND :endDate ORDER BY sl.createdAt DESC")
    List<SystemLog> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT sl FROM SystemLog sl WHERE sl.logLevel = :logLevel AND sl.createdAt BETWEEN :startDate AND :endDate ORDER BY sl.createdAt DESC")
    List<SystemLog> findByLogLevelAndDateRange(@Param("logLevel") SystemLog.LogLevel logLevel, 
                                               @Param("startDate") LocalDateTime startDate, 
                                               @Param("endDate") LocalDateTime endDate);

    @Query("SELECT sl FROM SystemLog sl WHERE sl.message LIKE %:keyword% ORDER BY sl.createdAt DESC")
    List<SystemLog> findByMessageContaining(@Param("keyword") String keyword);

    @Query("SELECT sl FROM SystemLog sl WHERE sl.endpoint = :endpoint ORDER BY sl.createdAt DESC")
    List<SystemLog> findByEndpoint(@Param("endpoint") String endpoint);

    @Query("SELECT sl FROM SystemLog sl WHERE sl.statusCode = :statusCode ORDER BY sl.createdAt DESC")
    List<SystemLog> findByStatusCode(@Param("statusCode") Integer statusCode);

    @Query("SELECT sl FROM SystemLog sl WHERE sl.responseTimeMs > :threshold ORDER BY sl.responseTimeMs DESC")
    List<SystemLog> findBySlowResponseTime(@Param("threshold") Long threshold);

    @Query("SELECT COUNT(sl) FROM SystemLog sl WHERE sl.logLevel = :logLevel")
    Long countByLogLevel(@Param("logLevel") SystemLog.LogLevel logLevel);

    @Query("SELECT COUNT(sl) FROM SystemLog sl WHERE sl.logCategory = :logCategory")
    Long countByLogCategory(@Param("logCategory") String logCategory);

    @Query("SELECT COUNT(sl) FROM SystemLog sl WHERE sl.createdAt BETWEEN :startDate AND :endDate")
    Long countByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT sl FROM SystemLog sl ORDER BY sl.createdAt DESC")
    List<SystemLog> findAllOrderByCreatedAtDesc();

    @Query("SELECT sl FROM SystemLog sl WHERE sl.logLevel IN ('ERROR', 'FATAL') ORDER BY sl.createdAt DESC")
    List<SystemLog> findErrorLogsOrderByCreatedAtDesc();
}
