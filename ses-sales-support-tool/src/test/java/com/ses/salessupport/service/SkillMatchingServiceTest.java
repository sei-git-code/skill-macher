package com.ses.salessupport.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ses.salessupport.entity.Employee;
import com.ses.salessupport.entity.EmployeeSkill;
import com.ses.salessupport.entity.JobRequirement;
import com.ses.salessupport.entity.MatchingResult;
import com.ses.salessupport.repository.EmployeeRepository;
import com.ses.salessupport.repository.EmployeeSkillRepository;
import com.ses.salessupport.repository.JobRequirementRepository;
import com.ses.salessupport.repository.MatchingResultRepository;
import com.ses.salessupport.repository.SkillMappingRepository;

/**
 * SkillMatchingServiceのユニットテスト
 */
@ExtendWith(MockitoExtension.class)
class SkillMatchingServiceTest {

    @Mock
    private JobRequirementRepository jobRequirementRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeSkillRepository employeeSkillRepository;

    @Mock
    private SkillMappingRepository skillMappingRepository;

    @Mock
    private MatchingResultRepository matchingResultRepository;

    @InjectMocks
    private SkillMatchingService skillMatchingService;

    private JobRequirement jobRequirement;
    private Employee employee;
    private EmployeeSkill employeeSkill;

    @BeforeEach
    void setUp() {
        // テスト用のJobRequirementを作成
        jobRequirement = new JobRequirement();
        jobRequirement.setId(1L);
        jobRequirement.setProjectName("テストプロジェクト");
        jobRequirement.setClientName("テストクライアント");
        jobRequirement.setRequiredSkills("Java,Spring Boot,PostgreSQL");
        jobRequirement.setRequiredExperienceYears(3);
        jobRequirement.setBudget(new BigDecimal("5000000"));

        // テスト用のEmployeeを作成
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("田中");
        employee.setLastName("太郎");
        employee.setEmail("tanaka@test.com");
        employee.setHourlyRate(new BigDecimal("8000"));

        // テスト用のEmployeeSkillを作成
        employeeSkill = new EmployeeSkill();
        employeeSkill.setId(1L);
        employeeSkill.setEmployee(employee);
        employeeSkill.setSkillName("Java");
        employeeSkill.setProficiencyLevel(4); // 上級レベル
        employeeSkill.setYearsOfExperience(5);
    }

    @Test
    void testPerformMatching_Success() {
        // Given
        when(jobRequirementRepository.findById(1L)).thenReturn(Optional.of(jobRequirement));
        when(employeeRepository.findByStatus(Employee.EmployeeStatus.ACTIVE))
                .thenReturn(Arrays.asList(employee));
        when(employeeSkillRepository.findByEmployee(employee))
                .thenReturn(Arrays.asList(employeeSkill));
        when(skillMappingRepository.findBySkillName(anyString()))
                .thenReturn(Optional.empty());
        when(matchingResultRepository.save(any(MatchingResult.class)))
                .thenReturn(new MatchingResult());

        // When
        List<MatchingResult> results = skillMatchingService.performMatching(1L);

        // Then
        assertNotNull(results);
        assertFalse(results.isEmpty());
        verify(matchingResultRepository, atLeastOnce()).save(any(MatchingResult.class));
    }

    @Test
    void testPerformMatching_JobRequirementNotFound() {
        // Given
        when(jobRequirementRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            skillMatchingService.performMatching(1L);
        });
    }

    @Test
    void testPerformMatching_NoActiveEmployees() {
        // Given
        when(jobRequirementRepository.findById(1L)).thenReturn(Optional.of(jobRequirement));
        when(employeeRepository.findByStatus(Employee.EmployeeStatus.ACTIVE))
                .thenReturn(Arrays.asList());

        // When
        List<MatchingResult> results = skillMatchingService.performMatching(1L);

        // Then
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void testPerformMatching_EmployeeWithNoSkills() {
        // Given
        when(jobRequirementRepository.findById(1L)).thenReturn(Optional.of(jobRequirement));
        when(employeeRepository.findByStatus(Employee.EmployeeStatus.ACTIVE))
                .thenReturn(Arrays.asList(employee));
        when(employeeSkillRepository.findByEmployee(employee))
                .thenReturn(Arrays.asList()); // スキルなし
        when(skillMappingRepository.findBySkillName(anyString()))
                .thenReturn(Optional.empty());

        // When
        List<MatchingResult> results = skillMatchingService.performMatching(1L);

        // Then
        assertNotNull(results);
        assertTrue(results.isEmpty()); // スキルがないためマッチング結果なし
    }

    @Test
    void testPerformMatching_HighScoreMatch() {
        // Given
        EmployeeSkill javaSkill = new EmployeeSkill();
        javaSkill.setEmployee(employee);
        javaSkill.setSkillName("Java");
        javaSkill.setProficiencyLevel(5); // エキスパートレベル
        javaSkill.setYearsOfExperience(5);

        EmployeeSkill springSkill = new EmployeeSkill();
        springSkill.setEmployee(employee);
        springSkill.setSkillName("Spring Boot");
        springSkill.setProficiencyLevel(4); // 上級レベル
        springSkill.setYearsOfExperience(3);

        when(jobRequirementRepository.findById(1L)).thenReturn(Optional.of(jobRequirement));
        when(employeeRepository.findByStatus(Employee.EmployeeStatus.ACTIVE))
                .thenReturn(Arrays.asList(employee));
        when(employeeSkillRepository.findByEmployee(employee))
                .thenReturn(Arrays.asList(javaSkill, springSkill));
        when(skillMappingRepository.findBySkillName(anyString()))
                .thenReturn(Optional.empty());
        when(matchingResultRepository.save(any(MatchingResult.class)))
                .thenReturn(new MatchingResult());

        // When
        List<MatchingResult> results = skillMatchingService.performMatching(1L);

        // Then
        assertNotNull(results);
        assertFalse(results.isEmpty());
        
        // 最初の結果（最高スコア）をチェック
        MatchingResult result = results.get(0);
        assertTrue(result.getMatchScore().compareTo(BigDecimal.valueOf(30.0)) >= 0);
        assertNotNull(result.getMatchedSkills());
        assertNotNull(result.getMissingSkills());
    }

    @Test
    void testPerformMatching_LowScoreFiltered() {
        // Given
        EmployeeSkill unrelatedSkill = new EmployeeSkill();
        unrelatedSkill.setEmployee(employee);
        unrelatedSkill.setSkillName("Python"); // 要件にないスキル
        unrelatedSkill.setProficiencyLevel(1); // 初級レベル
        unrelatedSkill.setYearsOfExperience(1);

        when(jobRequirementRepository.findById(1L)).thenReturn(Optional.of(jobRequirement));
        when(employeeRepository.findByStatus(Employee.EmployeeStatus.ACTIVE))
                .thenReturn(Arrays.asList(employee));
        when(employeeSkillRepository.findByEmployee(employee))
                .thenReturn(Arrays.asList(unrelatedSkill));
        when(skillMappingRepository.findBySkillName(anyString()))
                .thenReturn(Optional.empty());

        // When
        List<MatchingResult> results = skillMatchingService.performMatching(1L);

        // Then
        assertNotNull(results);
        assertTrue(results.isEmpty()); // 低スコアのため除外される
    }
}

