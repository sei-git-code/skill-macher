package com.ses.salessupport.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

/**
 * マッチングリクエストDTO
 */
public class MatchingRequest {
    
    @NotNull(message = "案件要件IDは必須です")
    private Long jobRequirementId;
    
    private Boolean includeInactiveEmployees = false;
    
    private BigDecimal minimumScore = BigDecimal.valueOf(60.0);
    
    private Integer maxResults = 10;
    
    public MatchingRequest() {}
    
    public MatchingRequest(Long jobRequirementId) {
        this.jobRequirementId = jobRequirementId;
    }
    
    public Long getJobRequirementId() {
        return jobRequirementId;
    }
    
    public void setJobRequirementId(Long jobRequirementId) {
        this.jobRequirementId = jobRequirementId;
    }
    
    public Boolean getIncludeInactiveEmployees() {
        return includeInactiveEmployees;
    }
    
    public void setIncludeInactiveEmployees(Boolean includeInactiveEmployees) {
        this.includeInactiveEmployees = includeInactiveEmployees;
    }
    
    public BigDecimal getMinimumScore() {
        return minimumScore;
    }
    
    public void setMinimumScore(BigDecimal minimumScore) {
        this.minimumScore = minimumScore;
    }
    
    public Integer getMaxResults() {
        return maxResults;
    }
    
    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }
}

