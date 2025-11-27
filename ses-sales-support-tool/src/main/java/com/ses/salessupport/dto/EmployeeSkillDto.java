package com.ses.salessupport.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 従業員スキルDTO
 */
public class EmployeeSkillDto {
    
    private Long id;
    
    @NotNull(message = "従業員IDは必須です")
    private Long employeeId;
    
    @NotNull(message = "スキルマッピングIDは必須です")
    private Long skillMappingId;
    
    @NotBlank(message = "スキルレベルは必須です")
    private String skillLevel;
    
    @Min(value = 0, message = "経験年数は0年以上で入力してください")
    @Max(value = 50, message = "経験年数は50年以下で入力してください")
    private Integer experienceYears;
    
    // スキル情報（表示用）
    private String skillName;
    private String skillCategory;
    private String skillDescription;
    
    public EmployeeSkillDto() {}
    
    public EmployeeSkillDto(Long employeeId, Long skillMappingId, String skillLevel, Integer experienceYears) {
        this.employeeId = employeeId;
        this.skillMappingId = skillMappingId;
        this.skillLevel = skillLevel;
        this.experienceYears = experienceYears;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
    
    public Long getSkillMappingId() {
        return skillMappingId;
    }
    
    public void setSkillMappingId(Long skillMappingId) {
        this.skillMappingId = skillMappingId;
    }
    
    public String getSkillLevel() {
        return skillLevel;
    }
    
    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }
    
    public Integer getExperienceYears() {
        return experienceYears;
    }
    
    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }
    
    public String getSkillName() {
        return skillName;
    }
    
    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
    
    public String getSkillCategory() {
        return skillCategory;
    }
    
    public void setSkillCategory(String skillCategory) {
        this.skillCategory = skillCategory;
    }
    
    public String getSkillDescription() {
        return skillDescription;
    }
    
    public void setSkillDescription(String skillDescription) {
        this.skillDescription = skillDescription;
    }
}

