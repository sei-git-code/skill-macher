package com.ses.salessupport.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ダッシュボード統計DTO
 */
public class DashboardStatsDto {
    
    private Long totalJobRequirements;
    private Long activeJobRequirements;
    private Long closedJobRequirements;
    
    private Long totalEmployees;
    private Long activeEmployees;
    private Long inactiveEmployees;
    
    private Long totalMatchingResults;
    private Long pendingMatchingResults;
    private Long approvedMatchingResults;
    private Long rejectedMatchingResults;
    
    private List<RecentActivityDto> recentActivities;
    
    public DashboardStatsDto() {}
    
    public Long getTotalJobRequirements() {
        return totalJobRequirements;
    }
    
    public void setTotalJobRequirements(Long totalJobRequirements) {
        this.totalJobRequirements = totalJobRequirements;
    }
    
    public Long getActiveJobRequirements() {
        return activeJobRequirements;
    }
    
    public void setActiveJobRequirements(Long activeJobRequirements) {
        this.activeJobRequirements = activeJobRequirements;
    }
    
    public Long getClosedJobRequirements() {
        return closedJobRequirements;
    }
    
    public void setClosedJobRequirements(Long closedJobRequirements) {
        this.closedJobRequirements = closedJobRequirements;
    }
    
    public Long getTotalEmployees() {
        return totalEmployees;
    }
    
    public void setTotalEmployees(Long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }
    
    public Long getActiveEmployees() {
        return activeEmployees;
    }
    
    public void setActiveEmployees(Long activeEmployees) {
        this.activeEmployees = activeEmployees;
    }
    
    public Long getInactiveEmployees() {
        return inactiveEmployees;
    }
    
    public void setInactiveEmployees(Long inactiveEmployees) {
        this.inactiveEmployees = inactiveEmployees;
    }
    
    public Long getTotalMatchingResults() {
        return totalMatchingResults;
    }
    
    public void setTotalMatchingResults(Long totalMatchingResults) {
        this.totalMatchingResults = totalMatchingResults;
    }
    
    public Long getPendingMatchingResults() {
        return pendingMatchingResults;
    }
    
    public void setPendingMatchingResults(Long pendingMatchingResults) {
        this.pendingMatchingResults = pendingMatchingResults;
    }
    
    public Long getApprovedMatchingResults() {
        return approvedMatchingResults;
    }
    
    public void setApprovedMatchingResults(Long approvedMatchingResults) {
        this.approvedMatchingResults = approvedMatchingResults;
    }
    
    public Long getRejectedMatchingResults() {
        return rejectedMatchingResults;
    }
    
    public void setRejectedMatchingResults(Long rejectedMatchingResults) {
        this.rejectedMatchingResults = rejectedMatchingResults;
    }
    
    public List<RecentActivityDto> getRecentActivities() {
        return recentActivities;
    }
    
    public void setRecentActivities(List<RecentActivityDto> recentActivities) {
        this.recentActivities = recentActivities;
    }
    
    /**
     * 最近のアクティビティDTO
     */
    public static class RecentActivityDto {
        private String type;
        private String description;
        private LocalDateTime timestamp;
        private String user;
        
        public RecentActivityDto() {}
        
        public RecentActivityDto(String type, String description, LocalDateTime timestamp, String user) {
            this.type = type;
            this.description = description;
            this.timestamp = timestamp;
            this.user = user;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public LocalDateTime getTimestamp() {
            return timestamp;
        }
        
        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
        
        public String getUser() {
            return user;
        }
        
        public void setUser(String user) {
            this.user = user;
        }
    }
}

