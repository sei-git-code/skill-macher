package com.ses.salessupport.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ses.salessupport.entity.JobRequirement;
import com.ses.salessupport.entity.User;
import com.ses.salessupport.repository.JobRequirementRepository;

@Service
@Transactional
public class JobRequirementService {

    @Autowired
    private JobRequirementRepository jobRequirementRepository;

    @Autowired
    private AuthService authService;

    /**
     * 全てのジョブ要件を取得
     */
    @Transactional(readOnly = true)
    public List<JobRequirement> getAllJobRequirements() {
        return jobRequirementRepository.findAllOrderByCreatedAtDesc();
    }

    /**
     * ページネーション付きでジョブ要件を取得
     */
    @Transactional(readOnly = true)
    public Page<JobRequirement> getJobRequirements(Pageable pageable) {
        return jobRequirementRepository.findAll(pageable);
    }

    /**
     * IDでジョブ要件を取得
     */
    @Transactional(readOnly = true)
    public Optional<JobRequirement> getJobRequirementById(Long id) {
        return jobRequirementRepository.findById(id);
    }

    /**
     * ステータスでジョブ要件を取得
     */
    @Transactional(readOnly = true)
    public List<JobRequirement> getJobRequirementsByStatus(JobRequirement.JobStatus status) {
        return jobRequirementRepository.findByStatus(status);
    }

    /**
     * 優先度でジョブ要件を取得
     */
    @Transactional(readOnly = true)
    public List<JobRequirement> getJobRequirementsByPriority(JobRequirement.Priority priority) {
        return jobRequirementRepository.findByPriority(priority);
    }

    /**
     * クライアント名でジョブ要件を取得
     */
    @Transactional(readOnly = true)
    public List<JobRequirement> getJobRequirementsByClient(String clientName) {
        return jobRequirementRepository.findByClientName(clientName);
    }

    /**
     * オープンなジョブ要件を優先度順で取得
     */
    @Transactional(readOnly = true)
    public List<JobRequirement> getOpenJobRequirements() {
        return jobRequirementRepository.findOpenJobsOrderByPriorityAndCreatedAt();
    }

    /**
     * キーワード検索
     */
    @Transactional(readOnly = true)
    public List<JobRequirement> searchJobRequirements(String keyword) {
        return jobRequirementRepository.findByKeyword(keyword);
    }

    /**
     * スキル検索
     */
    @Transactional(readOnly = true)
    public List<JobRequirement> searchJobRequirementsBySkill(String skill) {
        return jobRequirementRepository.findByRequiredSkillsContaining(skill);
    }

    /**
     * 予算範囲検索
     */
    @Transactional(readOnly = true)
    public List<JobRequirement> searchJobRequirementsByBudget(BigDecimal minBudget, BigDecimal maxBudget) {
        return jobRequirementRepository.findByBudgetRange(minBudget, maxBudget);
    }

    /**
     * 開始日以降のジョブ要件を取得
     */
    @Transactional(readOnly = true)
    public List<JobRequirement> getJobRequirementsAfterStartDate(LocalDate startDate) {
        return jobRequirementRepository.findByStartDateAfter(startDate);
    }

    /**
     * 新しいジョブ要件を作成
     */
    public JobRequirement createJobRequirement(JobRequirement jobRequirement) {
        // 作成者を設定
        Optional<User> currentUser = authService.getCurrentUser();
        if (currentUser.isPresent()) {
            jobRequirement.setCreatedBy(currentUser.get());
        }

        return jobRequirementRepository.save(jobRequirement);
    }

    /**
     * ジョブ要件を更新
     */
    public JobRequirement updateJobRequirement(Long id, JobRequirement updatedJobRequirement) {
        JobRequirement existingJobRequirement = jobRequirementRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("ジョブ要件が見つかりません: " + id));

        // 更新可能なフィールドを設定
        existingJobRequirement.setTitle(updatedJobRequirement.getTitle());
        existingJobRequirement.setDescription(updatedJobRequirement.getDescription());
        existingJobRequirement.setClientName(updatedJobRequirement.getClientName());
        existingJobRequirement.setProjectName(updatedJobRequirement.getProjectName());
        existingJobRequirement.setRequiredSkills(updatedJobRequirement.getRequiredSkills());
        existingJobRequirement.setPreferredSkills(updatedJobRequirement.getPreferredSkills());
        existingJobRequirement.setStartDate(updatedJobRequirement.getStartDate());
        existingJobRequirement.setEndDate(updatedJobRequirement.getEndDate());
        existingJobRequirement.setDurationMonths(updatedJobRequirement.getDurationMonths());
        existingJobRequirement.setBudget(updatedJobRequirement.getBudget());
        existingJobRequirement.setRequiredExperienceYears(updatedJobRequirement.getRequiredExperienceYears());
        existingJobRequirement.setLocation(updatedJobRequirement.getLocation());
        existingJobRequirement.setWorkType(updatedJobRequirement.getWorkType());
        existingJobRequirement.setPriority(updatedJobRequirement.getPriority());
        existingJobRequirement.setStatus(updatedJobRequirement.getStatus());

        return jobRequirementRepository.save(existingJobRequirement);
    }

    /**
     * ジョブ要件を削除
     */
    public void deleteJobRequirement(Long id) {
        if (!jobRequirementRepository.existsById(id)) {
            throw new RuntimeException("ジョブ要件が見つかりません: " + id);
        }
        jobRequirementRepository.deleteById(id);
    }

    /**
     * ジョブ要件のステータスを更新
     */
    public JobRequirement updateJobRequirementStatus(Long id, JobRequirement.JobStatus status) {
        JobRequirement jobRequirement = jobRequirementRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("ジョブ要件が見つかりません: " + id));

        jobRequirement.setStatus(status);
        return jobRequirementRepository.save(jobRequirement);
    }

    /**
     * ジョブ要件の優先度を更新
     */
    public JobRequirement updateJobRequirementPriority(Long id, JobRequirement.Priority priority) {
        JobRequirement jobRequirement = jobRequirementRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("ジョブ要件が見つかりません: " + id));

        jobRequirement.setPriority(priority);
        return jobRequirementRepository.save(jobRequirement);
    }

    /**
     * 統計情報を取得
     */
    @Transactional(readOnly = true)
    public JobRequirementStats getJobRequirementStats() {
        long totalJobs = jobRequirementRepository.count();
        long openJobs = jobRequirementRepository.countByStatus(JobRequirement.JobStatus.OPEN);
        long inProgressJobs = jobRequirementRepository.countByStatus(JobRequirement.JobStatus.IN_PROGRESS);
        long closedJobs = jobRequirementRepository.countByStatus(JobRequirement.JobStatus.CLOSED);

        return new JobRequirementStats(totalJobs, openJobs, inProgressJobs, closedJobs);
    }

    /**
     * 統計情報クラス
     */
    public static class JobRequirementStats {
        private final long totalJobs;
        private final long openJobs;
        private final long inProgressJobs;
        private final long closedJobs;

        public JobRequirementStats(long totalJobs, long openJobs, long inProgressJobs, long closedJobs) {
            this.totalJobs = totalJobs;
            this.openJobs = openJobs;
            this.inProgressJobs = inProgressJobs;
            this.closedJobs = closedJobs;
        }

        public long getTotalJobs() { return totalJobs; }
        public long getOpenJobs() { return openJobs; }
        public long getInProgressJobs() { return inProgressJobs; }
        public long getClosedJobs() { return closedJobs; }
    }
}
