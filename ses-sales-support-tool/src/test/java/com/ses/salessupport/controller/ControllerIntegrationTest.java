package com.ses.salessupport.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.ses.salessupport.entity.Employee;
import com.ses.salessupport.entity.Employee.EmployeeStatus;
import com.ses.salessupport.entity.Employee.EmploymentType;
import com.ses.salessupport.entity.JobRequirement;
import com.ses.salessupport.entity.JobRequirement.JobStatus;
import com.ses.salessupport.entity.JobRequirement.Priority;
import com.ses.salessupport.entity.JobRequirement.WorkType;
import com.ses.salessupport.repository.EmployeeRepository;
import com.ses.salessupport.repository.JobRequirementRepository;

/**
 * コントローラーの統合テスト（簡略版）
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class ControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JobRequirementRepository jobRequirementRepository;

    private Employee testEmployee;
    private JobRequirement testJobRequirement;

    @BeforeEach
    void setUp() {
        // テスト用のEmployeeを作成
        testEmployee = new Employee();
        testEmployee.setFirstName("田中");
        testEmployee.setLastName("太郎");
        testEmployee.setEmail("tanaka@test.com");
        testEmployee.setPhoneNumber("090-1234-5678");
        testEmployee.setHourlyRate(new BigDecimal("8000"));
        testEmployee.setEmploymentType(EmploymentType.FULL_TIME);
        testEmployee.setStatus(EmployeeStatus.ACTIVE);
        testEmployee = employeeRepository.save(testEmployee);

        // テスト用のJobRequirementを作成
        testJobRequirement = new JobRequirement();
        testJobRequirement.setProjectName("テストプロジェクト");
        testJobRequirement.setClientName("テストクライアント");
        testJobRequirement.setRequiredSkills("Java,Spring Boot");
        testJobRequirement.setRequiredExperienceYears(3);
        testJobRequirement.setBudget(new BigDecimal("5000000"));
        testJobRequirement.setWorkType(WorkType.REMOTE);
        testJobRequirement.setPriority(Priority.HIGH);
        testJobRequirement.setStatus(JobStatus.OPEN);
        testJobRequirement = jobRequirementRepository.save(testJobRequirement);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDashboardController_GetDashboard() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testEmployeeController_GetEmployees() throws Exception {
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testJobRequirementController_GetJobRequirements() throws Exception {
        mockMvc.perform(get("/job-requirements"))
                .andExpect(status().isOk());
    }

    @Test
    void testAccessControl_Unauthenticated() throws Exception {
        mockMvc.perform(get("/employees"))
                .andExpect(status().is3xxRedirection()); // 未認証はリダイレクト
    }
}