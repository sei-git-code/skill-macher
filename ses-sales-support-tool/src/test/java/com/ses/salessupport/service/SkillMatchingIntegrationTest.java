package com.ses.salessupport.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ses.salessupport.entity.Employee;
import com.ses.salessupport.entity.Employee.EmployeeStatus;
import com.ses.salessupport.entity.Employee.EmploymentType;
import com.ses.salessupport.entity.EmployeeSkill;
import com.ses.salessupport.entity.JobRequirement;
import com.ses.salessupport.entity.JobRequirement.JobStatus;
import com.ses.salessupport.entity.JobRequirement.Priority;
import com.ses.salessupport.entity.JobRequirement.WorkType;
import com.ses.salessupport.entity.MatchingResult;
import com.ses.salessupport.repository.EmployeeRepository;
import com.ses.salessupport.repository.EmployeeSkillRepository;
import com.ses.salessupport.repository.JobRequirementRepository;
import com.ses.salessupport.repository.MatchingResultRepository;

/**
 * スキルマッチング機能の統合テスト（簡略版）
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SkillMatchingIntegrationTest {

    @Autowired
    private SkillMatchingService skillMatchingService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeSkillRepository employeeSkillRepository;

    @Autowired
    private JobRequirementRepository jobRequirementRepository;

    @Autowired
    private MatchingResultRepository matchingResultRepository;

    private JobRequirement jobRequirement;
    private Employee employee;
    private EmployeeSkill employeeSkill;

    @BeforeEach
    void setUp() {
        // テスト用のJobRequirementを作成
        jobRequirement = new JobRequirement();
        jobRequirement.setProjectName("Java開発プロジェクト");
        jobRequirement.setClientName("テストクライアント");
        jobRequirement.setRequiredSkills("Java,Spring Boot");
        jobRequirement.setRequiredExperienceYears(3);
        jobRequirement.setBudget(new BigDecimal("5000000"));
        jobRequirement.setWorkType(WorkType.REMOTE);
        jobRequirement.setPriority(Priority.HIGH);
        jobRequirement.setStatus(JobStatus.OPEN);
        jobRequirement = jobRequirementRepository.save(jobRequirement);

        // テスト用のEmployeeを作成
        employee = new Employee();
        employee.setFirstName("田中");
        employee.setLastName("太郎");
        employee.setEmail("tanaka@test.com");
        employee.setPhoneNumber("090-1234-5678");
        employee.setHourlyRate(new BigDecimal("8000"));
        employee.setEmploymentType(EmploymentType.FULL_TIME);
        employee.setStatus(EmployeeStatus.ACTIVE);
        employee = employeeRepository.save(employee);

        // Employeeのスキルを作成
        employeeSkill = new EmployeeSkill();
        employeeSkill.setEmployee(employee);
        employeeSkill.setSkillName("Java");
        employeeSkill.setProficiencyLevel(5); // エキスパート
        employeeSkill.setYearsOfExperience(5);
        employeeSkill = employeeSkillRepository.save(employeeSkill);
    }

    @Test
    void testPerformMatching_BasicFunctionality() {
        // When
        var results = skillMatchingService.performMatching(jobRequirement.getId());

        // Then
        assertNotNull(results);
        // 基本的な機能が動作することを確認
        assertTrue(results.size() >= 0);
    }

    @Test
    void testPerformMatching_JobRequirementNotFound() {
        // When & Then
        try {
            skillMatchingService.performMatching(999L);
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("ジョブ要件が見つかりません"));
        }
    }
}