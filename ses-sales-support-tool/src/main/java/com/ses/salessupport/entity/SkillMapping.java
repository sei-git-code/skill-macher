package com.ses.salessupport.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "skill_mappings")
public class SkillMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(name = "skill_name", nullable = false)
    private String skillName;

    @Size(max = 100)
    @Column(name = "skill_category")
    private String skillCategory;

    @Size(max = 255)
    @Column(name = "related_skill_1")
    private String relatedSkill1;

    @Size(max = 255)
    @Column(name = "related_skill_2")
    private String relatedSkill2;

    @Size(max = 255)
    @Column(name = "related_skill_3")
    private String relatedSkill3;

    @Size(max = 255)
    @Column(name = "related_skill_4")
    private String relatedSkill4;

    @Size(max = 255)
    @Column(name = "related_skill_5")
    private String relatedSkill5;

    @NotNull
    @Column(name = "skill_weight", nullable = false, precision = 3, scale = 2)
    private Double skillWeight = 1.0;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Size(max = 1000)
    @Column(name = "keywords", length = 1000)
    private String keywords;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public SkillMapping() {}

    public SkillMapping(String skillName, String skillCategory, Double skillWeight) {
        this.skillName = skillName;
        this.skillCategory = skillCategory;
        this.skillWeight = skillWeight;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getRelatedSkill1() {
        return relatedSkill1;
    }

    public void setRelatedSkill1(String relatedSkill1) {
        this.relatedSkill1 = relatedSkill1;
    }

    public String getRelatedSkill2() {
        return relatedSkill2;
    }

    public void setRelatedSkill2(String relatedSkill2) {
        this.relatedSkill2 = relatedSkill2;
    }

    public String getRelatedSkill3() {
        return relatedSkill3;
    }

    public void setRelatedSkill3(String relatedSkill3) {
        this.relatedSkill3 = relatedSkill3;
    }

    public String getRelatedSkill4() {
        return relatedSkill4;
    }

    public void setRelatedSkill4(String relatedSkill4) {
        this.relatedSkill4 = relatedSkill4;
    }

    public String getRelatedSkill5() {
        return relatedSkill5;
    }

    public void setRelatedSkill5(String relatedSkill5) {
        this.relatedSkill5 = relatedSkill5;
    }

    public Double getSkillWeight() {
        return skillWeight;
    }

    public void setSkillWeight(Double skillWeight) {
        this.skillWeight = skillWeight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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
