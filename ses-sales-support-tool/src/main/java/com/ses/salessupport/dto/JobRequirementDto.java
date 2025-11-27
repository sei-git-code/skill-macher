package com.ses.salessupport.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 人員募集要項DTO
 */
public class JobRequirementDto {
    
    private Long id;
    
    @NotBlank(message = "プロジェクト名は必須です")
    @Size(max = 200, message = "プロジェクト名は200文字以内で入力してください")
    private String projectName;
    
    @NotBlank(message = "クライアント名は必須です")
    @Size(max = 200, message = "クライアント名は200文字以内で入力してください")
    private String clientName;
    
    @Future(message = "開始日は未来の日付を入力してください")
    private LocalDate startDate;
    
    @Future(message = "終了日は未来の日付を入力してください")
    private LocalDate endDate;
    
    @NotBlank(message = "勤務形態は必須です")
    private String workType;
    
    @NotBlank(message = "優先度は必須です")
    private String priority;
    
    @NotBlank(message = "ステータスは必須です")
    private String status;
    
    @Min(value = 0, message = "経験年数は0年以上で入力してください")
    @Max(value = 50, message = "経験年数は50年以下で入力してください")
    private Integer experienceYears;
    
    @DecimalMin(value = "0.0", message = "予算は0以上で入力してください")
    private BigDecimal budget;
    
    @Size(max = 2000, message = "必須スキルは2000文字以内で入力してください")
    private String requiredSkills;
    
    @Size(max = 2000, message = "希望スキルは2000文字以内で入力してください")
    private String preferredSkills;
    
    @Size(max = 5000, message = "説明は5000文字以内で入力してください")
    private String description;
    
    public JobRequirementDto() {}
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public String getWorkType() {
        return workType;
    }
    
    public void setWorkType(String workType) {
        this.workType = workType;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Integer getExperienceYears() {
        return experienceYears;
    }
    
    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }
    
    public BigDecimal getBudget() {
        return budget;
    }
    
    public void setBudget(BigDecimal budget) {
        this.budget = budget;
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}

