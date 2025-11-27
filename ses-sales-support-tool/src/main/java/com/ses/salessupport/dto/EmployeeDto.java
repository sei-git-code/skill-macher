package com.ses.salessupport.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * SES人員DTO
 */
public class EmployeeDto {
    
    private Long id;
    
    @NotBlank(message = "名前は必須です")
    @Size(max = 200, message = "名前は200文字以内で入力してください")
    private String name;
    
    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "有効なメールアドレスを入力してください")
    @Size(max = 255, message = "メールアドレスは255文字以内で入力してください")
    private String email;
    
    @Size(max = 20, message = "電話番号は20文字以内で入力してください")
    private String phone;
    
    @Size(max = 500, message = "住所は500文字以内で入力してください")
    private String address;
    
    @NotBlank(message = "雇用形態は必須です")
    private String employmentType;
    
    @NotBlank(message = "ステータスは必須です")
    private String status;
    
    @Min(value = 0, message = "経験年数は0年以上で入力してください")
    @Max(value = 50, message = "経験年数は50年以下で入力してください")
    private Integer experienceYears;
    
    @DecimalMin(value = "0.0", message = "時給は0以上で入力してください")
    private BigDecimal hourlyRate;
    
    @Size(max = 5000, message = "プロフィールは5000文字以内で入力してください")
    private String profile;
    
    private List<EmployeeSkillDto> skills;
    
    public EmployeeDto() {}
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getEmploymentType() {
        return employmentType;
    }
    
    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
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
    
    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }
    
    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
    
    public String getProfile() {
        return profile;
    }
    
    public void setProfile(String profile) {
        this.profile = profile;
    }
    
    public List<EmployeeSkillDto> getSkills() {
        return skills;
    }
    
    public void setSkills(List<EmployeeSkillDto> skills) {
        this.skills = skills;
    }
}

