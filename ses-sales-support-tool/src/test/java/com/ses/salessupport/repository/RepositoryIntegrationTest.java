package com.ses.salessupport.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.ses.salessupport.entity.Employee;
import com.ses.salessupport.entity.Employee.EmployeeStatus;
import com.ses.salessupport.entity.Employee.EmploymentType;
import com.ses.salessupport.entity.EmployeeSkill;
import com.ses.salessupport.entity.JobRequirement;
import com.ses.salessupport.entity.JobRequirement.JobStatus;
import com.ses.salessupport.entity.JobRequirement.Priority;
import com.ses.salessupport.entity.JobRequirement.WorkType;
import com.ses.salessupport.entity.MatchingResult;
import com.ses.salessupport.entity.MatchingResult.MatchingStatus;

/**
 * リポジトリの統合テスト（簡略版）
 */
@DataJpaTest
@ActiveProfiles("test")
class RepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeSkillRepository employeeSkillRepository;

    @Autowired
    private JobRequirementRepository jobRequirementRepository;

    @Autowired
    private MatchingResultRepository matchingResultRepository;

    private Employee employee;
    private JobRequirement jobRequirement;
    private EmployeeSkill employeeSkill;
    private MatchingResult matchingResult;

    @BeforeEach
    void setUp() {
        // テスト用のEmployeeを作成
        employee = new Employee();
        employee.setFirstName("田中");
        employee.setLastName("太郎");
        employee.setEmail("tanaka@test.com");
        employee.setPhoneNumber("090-1234-5678");
        employee.setHourlyRate(new BigDecimal("8000"));
        employee.setEmploymentType(EmploymentType.FULL_TIME);
        employee.setStatus(EmployeeStatus.ACTIVE);

        // テスト用のJobRequirementを作成
        jobRequirement = new JobRequirement();
        jobRequirement.setProjectName("テストプロジェクト");
        jobRequirement.setClientName("テストクライアント");
        jobRequirement.setRequiredSkills("Java,Spring Boot");
        jobRequirement.setRequiredExperienceYears(3);
        jobRequirement.setBudget(new BigDecimal("5000000"));
        jobRequirement.setWorkType(WorkType.REMOTE);
        jobRequirement.setPriority(Priority.HIGH);
        jobRequirement.setStatus(JobStatus.OPEN);

        // テスト用のEmployeeSkillを作成
        employeeSkill = new EmployeeSkill();
        employeeSkill.setSkillName("Java");
        employeeSkill.setProficiencyLevel(4);
        employeeSkill.setYearsOfExperience(5);

        // テスト用のMatchingResultを作成
        matchingResult = new MatchingResult();
        matchingResult.setMatchScore(new BigDecimal("85.5"));
        matchingResult.setSkillMatchScore(new BigDecimal("90.0"));
        matchingResult.setExperienceMatchScore(new BigDecimal("80.0"));
        matchingResult.setBudgetMatchScore(new BigDecimal("85.0"));
        matchingResult.setMatchedSkills("Java,Spring Boot");
        matchingResult.setMissingSkills("PostgreSQL");
        matchingResult.setRecommendationReason("スキルと経験が要件に適合");
        matchingResult.setStatus(MatchingStatus.PENDING);
    }

    @Test
    void testEmployeeRepository_SaveAndFind() {
        // Given
        Employee savedEmployee = entityManager.persistAndFlush(employee);

        // When
        Optional<Employee> foundEmployee = employeeRepository.findById(savedEmployee.getId());

        // Then
        assertTrue(foundEmployee.isPresent());
        assertEquals(employee.getFirstName(), foundEmployee.get().getFirstName());
        assertEquals(employee.getEmail(), foundEmployee.get().getEmail());
    }

    @Test
    void testJobRequirementRepository_SaveAndFind() {
        // Given
        JobRequirement savedRequirement = entityManager.persistAndFlush(jobRequirement);

        // When
        Optional<JobRequirement> foundRequirement = jobRequirementRepository.findById(savedRequirement.getId());

        // Then
        assertTrue(foundRequirement.isPresent());
        assertEquals(jobRequirement.getProjectName(), foundRequirement.get().getProjectName());
        assertEquals(jobRequirement.getClientName(), foundRequirement.get().getClientName());
    }

    @Test
    void testMatchingResultRepository_SaveAndFind() {
        // Given
        Employee savedEmployee = entityManager.persistAndFlush(employee);
        JobRequirement savedRequirement = entityManager.persistAndFlush(jobRequirement);
        
        matchingResult.setEmployee(savedEmployee);
        matchingResult.setJobRequirement(savedRequirement);
        MatchingResult savedResult = entityManager.persistAndFlush(matchingResult);

        // When
        Optional<MatchingResult> foundResult = matchingResultRepository.findById(savedResult.getId());

        // Then
        assertTrue(foundResult.isPresent());
        assertEquals(matchingResult.getMatchScore(), foundResult.get().getMatchScore());
        assertEquals(MatchingStatus.PENDING, foundResult.get().getStatus());
    }
}