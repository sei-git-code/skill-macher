package com.ses.salessupport.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "job_requirements")
public class JobRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "description", length = 1000, nullable = false)
    private String description;

    @NotBlank
    @Size(max = 255)
    @Column(name = "client_name", nullable = false)
    private String clientName;

    @NotBlank
    @Size(max = 255)
    @Column(name = "project_name", nullable = false)
    private String projectName;

    @NotNull
    @Column(name = "required_skills", length = 2000, nullable = false)
    private String requiredSkills;

    @Column(name = "preferred_skills", length = 2000)
    private String preferredSkills;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull
    @Column(name = "duration_months", nullable = false)
    private Integer durationMonths;

    @NotNull
    @Column(name = "budget", nullable = false, precision = 15, scale = 2)
    private BigDecimal budget;

    @NotNull
    @Column(name = "required_experience_years", nullable = false)
    private Integer requiredExperienceYears;

    @Size(max = 255)
    @Column(name = "location")
    private String location;

    @Size(max = 50)
    @Enumerated(EnumType.STRING)
    @Column(name = "work_type", nullable = false)
    private WorkType workType;

    @Size(max = 50)
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority;

    @Size(max = 50)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private JobStatus status = JobStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "jobRequirement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MatchingResult> matchingResults = new ArrayList<>();

    // Constructors
    public JobRequirement() {}

    public JobRequirement(String title, String description, String clientName, String projectName,
                         String requiredSkills, LocalDate startDate, Integer durationMonths,
                         BigDecimal budget, Integer requiredExperienceYears, WorkType workType,
                         Priority priority, User createdBy) {
        this.title = title;
        this.description = description;
        this.clientName = clientName;
        this.projectName = projectName;
        this.requiredSkills = requiredSkills;
        this.startDate = startDate;
        this.durationMonths = durationMonths;
        this.budget = budget;
        this.requiredExperienceYears = requiredExperienceYears;
        this.workType = workType;
        this.priority = priority;
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public String getPreferredSkills() {
        return preferredSkills;
    }

    public void setPreferredSkills(String preferredSkills) {
        this.preferredSkills = preferredSkills;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public Integer getRequiredExperienceYears() {
        return requiredExperienceYears;
    }

    public void setRequiredExperienceYears(Integer requiredExperienceYears) {
        this.requiredExperienceYears = requiredExperienceYears;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public WorkType getWorkType() {
        return workType;
    }

    public void setWorkType(WorkType workType) {
        this.workType = workType;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
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

    public List<MatchingResult> getMatchingResults() {
        return matchingResults;
    }

    public void setMatchingResults(List<MatchingResult> matchingResults) {
        this.matchingResults = matchingResults;
    }

    public enum WorkType {
        REMOTE, ONSITE, HYBRID
    }

    public enum Priority {
        LOW, MEDIUM, HIGH, URGENT
    }

    public enum JobStatus {
        OPEN, IN_PROGRESS, CLOSED, CANCELLED
    }
}
