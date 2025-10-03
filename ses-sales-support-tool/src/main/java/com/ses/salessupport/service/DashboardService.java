package com.ses.salessupport.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ses.salessupport.entity.Employee;
import com.ses.salessupport.entity.JobRequirement;
import com.ses.salessupport.entity.MatchingResult;
import com.ses.salessupport.entity.SystemLog;
import com.ses.salessupport.repository.EmployeeRepository;
import com.ses.salessupport.repository.JobRequirementRepository;
import com.ses.salessupport.repository.MatchingResultRepository;
import com.ses.salessupport.repository.SystemLogRepository;
import com.ses.salessupport.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRequirementRepository jobRequirementRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MatchingResultRepository matchingResultRepository;

    @Autowired
    private SystemLogRepository systemLogRepository;

    /**
     * ダッシュボードの統計情報を取得
     */
    public DashboardStats getDashboardStats() {
        DashboardStats stats = new DashboardStats();

        // ユーザー統計
        stats.setTotalUsers(userRepository.count());
        stats.setActiveUsers(userRepository.countActiveUsers());

        // ジョブ要件統計
        stats.setTotalJobRequirements(jobRequirementRepository.count());
        stats.setOpenJobRequirements(jobRequirementRepository.countByStatus(JobRequirement.JobStatus.OPEN));
        stats.setInProgressJobRequirements(jobRequirementRepository.countByStatus(JobRequirement.JobStatus.IN_PROGRESS));
        stats.setClosedJobRequirements(jobRequirementRepository.countByStatus(JobRequirement.JobStatus.CLOSED));

        // 従業員統計
        stats.setTotalEmployees(employeeRepository.count());
        stats.setActiveEmployees(employeeRepository.countByStatus(Employee.EmployeeStatus.ACTIVE));
        stats.setInactiveEmployees(employeeRepository.countByStatus(Employee.EmployeeStatus.INACTIVE));

        // マッチング統計
        stats.setTotalMatchingResults(matchingResultRepository.count());
        stats.setPendingMatchingResults(matchingResultRepository.countByStatus(MatchingResult.MatchingStatus.PENDING));
        stats.setApprovedMatchingResults(matchingResultRepository.countByStatus(MatchingResult.MatchingStatus.APPROVED));

        return stats;
    }

    /**
     * 最近のアクティビティを取得
     */
    public RecentActivity getRecentActivity() {
        RecentActivity activity = new RecentActivity();

        // 最近のジョブ要件
        List<JobRequirement> recentJobs = jobRequirementRepository.findAllOrderByCreatedAtDesc();
        activity.setRecentJobRequirements(recentJobs.subList(0, Math.min(5, recentJobs.size())));

        // 最近の従業員
        List<Employee> recentEmployees = employeeRepository.findAllOrderByCreatedAtDesc();
        activity.setRecentEmployees(recentEmployees.subList(0, Math.min(5, recentEmployees.size())));

        // 最近のマッチング結果
        List<MatchingResult> recentMatchingResults = matchingResultRepository.findAllOrderByCreatedAtDesc();
        activity.setRecentMatchingResults(recentMatchingResults.subList(0, Math.min(5, recentMatchingResults.size())));

        return activity;
    }

    /**
     * 優先度別ジョブ要件の分布を取得
     */
    public Map<JobRequirement.Priority, Long> getJobRequirementDistributionByPriority() {
        Map<JobRequirement.Priority, Long> distribution = new HashMap<>();
        
        for (JobRequirement.Priority priority : JobRequirement.Priority.values()) {
            long count = jobRequirementRepository.countByStatus(JobRequirement.JobStatus.OPEN);
            distribution.put(priority, count);
        }
        
        return distribution;
    }

    /**
     * ステータス別ジョブ要件の分布を取得
     */
    public Map<JobRequirement.JobStatus, Long> getJobRequirementDistributionByStatus() {
        Map<JobRequirement.JobStatus, Long> distribution = new HashMap<>();
        
        for (JobRequirement.JobStatus status : JobRequirement.JobStatus.values()) {
            long count = jobRequirementRepository.countByStatus(status);
            distribution.put(status, count);
        }
        
        return distribution;
    }

    /**
     * 雇用形態別従業員の分布を取得
     */
    public Map<Employee.EmploymentType, Long> getEmployeeDistributionByEmploymentType() {
        Map<Employee.EmploymentType, Long> distribution = new HashMap<>();
        
        for (Employee.EmploymentType type : Employee.EmploymentType.values()) {
            long count = employeeRepository.countByStatus(Employee.EmployeeStatus.ACTIVE);
            distribution.put(type, count);
        }
        
        return distribution;
    }

    /**
     * マッチングスコアの分布を取得
     */
    public Map<String, Long> getMatchingScoreDistribution() {
        Map<String, Long> distribution = new HashMap<>();
        
        List<MatchingResult> allResults = matchingResultRepository.findAll();
        
        long excellent = allResults.stream()
            .filter(r -> r.getMatchScore().compareTo(BigDecimal.valueOf(80)) >= 0)
            .count();
        
        long good = allResults.stream()
            .filter(r -> r.getMatchScore().compareTo(BigDecimal.valueOf(60)) >= 0 && 
                        r.getMatchScore().compareTo(BigDecimal.valueOf(80)) < 0)
            .count();
        
        long fair = allResults.stream()
            .filter(r -> r.getMatchScore().compareTo(BigDecimal.valueOf(40)) >= 0 && 
                        r.getMatchScore().compareTo(BigDecimal.valueOf(60)) < 0)
            .count();
        
        long poor = allResults.stream()
            .filter(r -> r.getMatchScore().compareTo(BigDecimal.valueOf(40)) < 0)
            .count();
        
        distribution.put("優秀 (80%以上)", excellent);
        distribution.put("良好 (60-79%)", good);
        distribution.put("普通 (40-59%)", fair);
        distribution.put("低い (40%未満)", poor);
        
        return distribution;
    }

    /**
     * 月別ジョブ要件作成数の推移を取得
     */
    public Map<String, Long> getJobRequirementTrendByMonth() {
        Map<String, Long> trend = new HashMap<>();
        
        LocalDate startDate = LocalDate.now().minusMonths(12);
        List<JobRequirement> recentJobs = jobRequirementRepository.findByStartDateAfter(startDate);
        
        // 月別に集計
        for (int i = 0; i < 12; i++) {
            LocalDate monthStart = startDate.plusMonths(i);
            LocalDate monthEnd = monthStart.plusMonths(1);
            
            long count = recentJobs.stream()
                .filter(job -> job.getCreatedAt().toLocalDate().isAfter(monthStart.minusDays(1)) &&
                              job.getCreatedAt().toLocalDate().isBefore(monthEnd))
                .count();
            
            trend.put(monthStart.getYear() + "-" + String.format("%02d", monthStart.getMonthValue()), count);
        }
        
        return trend;
    }

    /**
     * システムログの統計を取得
     */
    public Map<SystemLog.LogLevel, Long> getSystemLogStats() {
        Map<SystemLog.LogLevel, Long> stats = new HashMap<>();
        
        for (SystemLog.LogLevel level : SystemLog.LogLevel.values()) {
            long count = systemLogRepository.countByLogLevel(level);
            stats.put(level, count);
        }
        
        return stats;
    }

    /**
     * ダッシュボード統計クラス
     */
    public static class DashboardStats {
        private long totalUsers;
        private long activeUsers;
        private long totalJobRequirements;
        private long openJobRequirements;
        private long inProgressJobRequirements;
        private long closedJobRequirements;
        private long totalEmployees;
        private long activeEmployees;
        private long inactiveEmployees;
        private long totalMatchingResults;
        private long pendingMatchingResults;
        private long approvedMatchingResults;

        // Getters and Setters
        public long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

        public long getActiveUsers() { return activeUsers; }
        public void setActiveUsers(long activeUsers) { this.activeUsers = activeUsers; }

        public long getTotalJobRequirements() { return totalJobRequirements; }
        public void setTotalJobRequirements(long totalJobRequirements) { this.totalJobRequirements = totalJobRequirements; }

        public long getOpenJobRequirements() { return openJobRequirements; }
        public void setOpenJobRequirements(long openJobRequirements) { this.openJobRequirements = openJobRequirements; }

        public long getInProgressJobRequirements() { return inProgressJobRequirements; }
        public void setInProgressJobRequirements(long inProgressJobRequirements) { this.inProgressJobRequirements = inProgressJobRequirements; }

        public long getClosedJobRequirements() { return closedJobRequirements; }
        public void setClosedJobRequirements(long closedJobRequirements) { this.closedJobRequirements = closedJobRequirements; }

        public long getTotalEmployees() { return totalEmployees; }
        public void setTotalEmployees(long totalEmployees) { this.totalEmployees = totalEmployees; }

        public long getActiveEmployees() { return activeEmployees; }
        public void setActiveEmployees(long activeEmployees) { this.activeEmployees = activeEmployees; }

        public long getInactiveEmployees() { return inactiveEmployees; }
        public void setInactiveEmployees(long inactiveEmployees) { this.inactiveEmployees = inactiveEmployees; }

        public long getTotalMatchingResults() { return totalMatchingResults; }
        public void setTotalMatchingResults(long totalMatchingResults) { this.totalMatchingResults = totalMatchingResults; }

        public long getPendingMatchingResults() { return pendingMatchingResults; }
        public void setPendingMatchingResults(long pendingMatchingResults) { this.pendingMatchingResults = pendingMatchingResults; }

        public long getApprovedMatchingResults() { return approvedMatchingResults; }
        public void setApprovedMatchingResults(long approvedMatchingResults) { this.approvedMatchingResults = approvedMatchingResults; }
    }

    /**
     * 最近のアクティビティクラス
     */
    public static class RecentActivity {
        private List<JobRequirement> recentJobRequirements;
        private List<Employee> recentEmployees;
        private List<MatchingResult> recentMatchingResults;

        // Getters and Setters
        public List<JobRequirement> getRecentJobRequirements() { return recentJobRequirements; }
        public void setRecentJobRequirements(List<JobRequirement> recentJobRequirements) { this.recentJobRequirements = recentJobRequirements; }

        public List<Employee> getRecentEmployees() { return recentEmployees; }
        public void setRecentEmployees(List<Employee> recentEmployees) { this.recentEmployees = recentEmployees; }

        public List<MatchingResult> getRecentMatchingResults() { return recentMatchingResults; }
        public void setRecentMatchingResults(List<MatchingResult> recentMatchingResults) { this.recentMatchingResults = recentMatchingResults; }
    }
}
