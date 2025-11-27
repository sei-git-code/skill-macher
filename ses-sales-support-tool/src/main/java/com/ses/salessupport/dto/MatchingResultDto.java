package com.ses.salessupport.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * マッチング結果DTO
 */
public class MatchingResultDto {
    
    private Long id;
    
    private Long jobRequirementId;
    private String projectName;
    private String clientName;
    
    private Long employeeId;
    private String employeeName;
    private String employeeEmail;
    private String employeeStatus;
    
    private BigDecimal matchingScore;
    private String status;
    private String matchedSkills;
    private String missingSkills;
    private String comments;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public MatchingResultDto() {}
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getJobRequirementId() {
        return jobRequirementId;
    }
    
    public void setJobRequirementId(Long jobRequirementId) {
        this.jobRequirementId = jobRequirementId;
    }
    
    public String getProjectName() {
        return projectName;
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    public String getClientName() {
        return clientName;
    }
    
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    
    public Long getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getEmployeeName() {
        return employeeName;
    }
    
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    
    public String getEmployeeEmail() {
        return employeeEmail;
    }
    
    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }
    
    public String getEmployeeStatus() {
        return employeeStatus;
    }
    
    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }
    
    public BigDecimal getMatchingScore() {
        return matchingScore;
    }
    
    public void setMatchingScore(BigDecimal matchingScore) {
        this.matchingScore = matchingScore;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMatchedSkills() {
        return matchedSkills;
    }
    
    public void setMatchedSkills(String matchedSkills) {
        this.matchedSkills = matchedSkills;
    }
    
    public String getMissingSkills() {
        return missingSkills;
    }
    
    public void setMissingSkills(String missingSkills) {
        this.missingSkills = missingSkills;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

