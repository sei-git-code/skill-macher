package com.ses.salessupport.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "matching_results")
public class MatchingResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_requirement_id", nullable = false)
    private JobRequirement jobRequirement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @NotNull
    @Column(name = "match_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal matchScore; // 0.00 - 100.00

    @NotNull
    @Column(name = "skill_match_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal skillMatchScore;

    @NotNull
    @Column(name = "experience_match_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal experienceMatchScore;

    @Column(name = "budget_match_score", precision = 5, scale = 2)
    private BigDecimal budgetMatchScore;

    @Column(name = "location_match_score", precision = 5, scale = 2)
    private BigDecimal locationMatchScore;

    @Column(name = "availability_match_score", precision = 5, scale = 2)
    private BigDecimal availabilityMatchScore;

    @Column(name = "matched_skills", length = 2000)
    private String matchedSkills;

    @Column(name = "missing_skills", length = 2000)
    private String missingSkills;

    @Column(name = "recommendation_reason", length = 1000)
    private String recommendationReason;

    @Column(name = "estimated_project_cost", precision = 15, scale = 2)
    private BigDecimal estimatedProjectCost;

    @Column(name = "estimated_duration_months")
    private Integer estimatedDurationMonths;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MatchingStatus status = MatchingStatus.PENDING;

    @Column(name = "reviewed_by")
    private String reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "review_notes", length = 1000)
    private String reviewNotes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public MatchingResult() {}

    public MatchingResult(JobRequirement jobRequirement, Employee employee,
                         BigDecimal matchScore, BigDecimal skillMatchScore,
                         BigDecimal experienceMatchScore) {
        this.jobRequirement = jobRequirement;
        this.employee = employee;
        this.matchScore = matchScore;
        this.skillMatchScore = skillMatchScore;
        this.experienceMatchScore = experienceMatchScore;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobRequirement getJobRequirement() {
        return jobRequirement;
    }

    public void setJobRequirement(JobRequirement jobRequirement) {
        this.jobRequirement = jobRequirement;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public BigDecimal getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(BigDecimal matchScore) {
        this.matchScore = matchScore;
    }

    public BigDecimal getSkillMatchScore() {
        return skillMatchScore;
    }

    public void setSkillMatchScore(BigDecimal skillMatchScore) {
        this.skillMatchScore = skillMatchScore;
    }

    public BigDecimal getExperienceMatchScore() {
        return experienceMatchScore;
    }

    public void setExperienceMatchScore(BigDecimal experienceMatchScore) {
        this.experienceMatchScore = experienceMatchScore;
    }

    public BigDecimal getBudgetMatchScore() {
        return budgetMatchScore;
    }

    public void setBudgetMatchScore(BigDecimal budgetMatchScore) {
        this.budgetMatchScore = budgetMatchScore;
    }

    public BigDecimal getLocationMatchScore() {
        return locationMatchScore;
    }

    public void setLocationMatchScore(BigDecimal locationMatchScore) {
        this.locationMatchScore = locationMatchScore;
    }

    public BigDecimal getAvailabilityMatchScore() {
        return availabilityMatchScore;
    }

    public void setAvailabilityMatchScore(BigDecimal availabilityMatchScore) {
        this.availabilityMatchScore = availabilityMatchScore;
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

    public String getRecommendationReason() {
        return recommendationReason;
    }

    public void setRecommendationReason(String recommendationReason) {
        this.recommendationReason = recommendationReason;
    }

    public BigDecimal getEstimatedProjectCost() {
        return estimatedProjectCost;
    }

    public void setEstimatedProjectCost(BigDecimal estimatedProjectCost) {
        this.estimatedProjectCost = estimatedProjectCost;
    }

    public Integer getEstimatedDurationMonths() {
        return estimatedDurationMonths;
    }

    public void setEstimatedDurationMonths(Integer estimatedDurationMonths) {
        this.estimatedDurationMonths = estimatedDurationMonths;
    }

    public MatchingStatus getStatus() {
        return status;
    }

    public void setStatus(MatchingStatus status) {
        this.status = status;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public String getReviewNotes() {
        return reviewNotes;
    }

    public void setReviewNotes(String reviewNotes) {
        this.reviewNotes = reviewNotes;
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

    public enum MatchingStatus {
        PENDING, APPROVED, REJECTED, INTERVIEWED, SELECTED, REJECTED_BY_CLIENT
    }
}
