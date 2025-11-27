package com.ses.salessupport.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ses.salessupport.entity.JobRequirement;
import com.ses.salessupport.entity.JobRequirement.JobStatus;
import com.ses.salessupport.entity.JobRequirement.Priority;
import com.ses.salessupport.entity.JobRequirement.WorkType;
import com.ses.salessupport.repository.JobRequirementRepository;

/**
 * JobRequirementServiceのユニットテスト
 */
@ExtendWith(MockitoExtension.class)
class JobRequirementServiceTest {

    @Mock
    private JobRequirementRepository jobRequirementRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private JobRequirementService jobRequirementService;

    private JobRequirement jobRequirement;

    @BeforeEach
    void setUp() {
        jobRequirement = new JobRequirement();
        jobRequirement.setId(1L);
        jobRequirement.setProjectName("テストプロジェクト");
        jobRequirement.setClientName("テストクライアント");
        jobRequirement.setRequiredSkills("Java,Spring Boot");
        jobRequirement.setRequiredExperienceYears(3);
        jobRequirement.setBudget(new BigDecimal("5000000"));
        jobRequirement.setWorkType(WorkType.REMOTE);
        jobRequirement.setPriority(Priority.HIGH);
        jobRequirement.setStatus(JobStatus.OPEN);
    }

    @Test
    void testGetAllJobRequirements_Success() {
        // Given
        List<JobRequirement> expectedRequirements = Arrays.asList(jobRequirement);
        when(jobRequirementRepository.findAll()).thenReturn(expectedRequirements);

        // When
        List<JobRequirement> requirements = jobRequirementService.getAllJobRequirements();

        // Then
        assertNotNull(requirements);
        assertEquals(1, requirements.size());
        assertEquals(jobRequirement.getId(), requirements.get(0).getId());
        verify(jobRequirementRepository).findAll();
    }

    @Test
    void testGetJobRequirementById_Success() {
        // Given
        when(jobRequirementRepository.findById(1L)).thenReturn(Optional.of(jobRequirement));

        // When
        Optional<JobRequirement> result = jobRequirementService.getJobRequirementById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(jobRequirement.getId(), result.get().getId());
        verify(jobRequirementRepository).findById(1L);
    }

    @Test
    void testGetJobRequirementById_NotFound() {
        // Given
        when(jobRequirementRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<JobRequirement> result = jobRequirementService.getJobRequirementById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(jobRequirementRepository).findById(1L);
    }

    @Test
    void testGetOpenJobRequirements_Success() {
        // Given
        List<JobRequirement> expectedRequirements = Arrays.asList(jobRequirement);
        when(jobRequirementRepository.findByStatus(JobStatus.OPEN)).thenReturn(expectedRequirements);

        // When
        List<JobRequirement> requirements = jobRequirementService.getOpenJobRequirements();

        // Then
        assertNotNull(requirements);
        assertEquals(1, requirements.size());
        assertEquals(JobStatus.OPEN, requirements.get(0).getStatus());
        verify(jobRequirementRepository).findByStatus(JobStatus.OPEN);
    }

    @Test
    void testCreateJobRequirement_Success() {
        // Given
        when(jobRequirementRepository.save(any(JobRequirement.class))).thenReturn(jobRequirement);

        // When
        JobRequirement result = jobRequirementService.createJobRequirement(jobRequirement);

        // Then
        assertNotNull(result);
        assertEquals(jobRequirement.getId(), result.getId());
        verify(jobRequirementRepository).save(jobRequirement);
    }

    @Test
    void testDeleteJobRequirement_Success() {
        // Given
        when(jobRequirementRepository.existsById(1L)).thenReturn(true);

        // When
        jobRequirementService.deleteJobRequirement(1L);

        // Then
        verify(jobRequirementRepository).existsById(1L);
        verify(jobRequirementRepository).deleteById(1L);
    }

    @Test
    void testDeleteJobRequirement_NotFound() {
        // Given
        when(jobRequirementRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            jobRequirementService.deleteJobRequirement(1L);
        });
    }

    @Test
    void testUpdateJobRequirementStatus_Success() {
        // Given
        when(jobRequirementRepository.findById(1L)).thenReturn(Optional.of(jobRequirement));
        when(jobRequirementRepository.save(any(JobRequirement.class))).thenReturn(jobRequirement);

        // When
        JobRequirement result = jobRequirementService.updateJobRequirementStatus(1L, JobStatus.CLOSED);

        // Then
        assertNotNull(result);
        assertEquals(JobStatus.CLOSED, result.getStatus());
        verify(jobRequirementRepository).findById(1L);
        verify(jobRequirementRepository).save(jobRequirement);
    }

    @Test
    void testUpdateJobRequirementStatus_NotFound() {
        // Given
        when(jobRequirementRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            jobRequirementService.updateJobRequirementStatus(1L, JobStatus.CLOSED);
        });
    }

    @Test
    void testUpdateJobRequirementPriority_Success() {
        // Given
        when(jobRequirementRepository.findById(1L)).thenReturn(Optional.of(jobRequirement));
        when(jobRequirementRepository.save(any(JobRequirement.class))).thenReturn(jobRequirement);

        // When
        JobRequirement result = jobRequirementService.updateJobRequirementPriority(1L, Priority.MEDIUM);

        // Then
        assertNotNull(result);
        assertEquals(Priority.MEDIUM, result.getPriority());
        verify(jobRequirementRepository).findById(1L);
        verify(jobRequirementRepository).save(jobRequirement);
    }

    @Test
    void testUpdateJobRequirementPriority_NotFound() {
        // Given
        when(jobRequirementRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            jobRequirementService.updateJobRequirementPriority(1L, Priority.MEDIUM);
        });
    }

    @Test
    void testGetJobRequirementStats_Success() {
        // Given
        when(jobRequirementRepository.count()).thenReturn(15L);
        when(jobRequirementRepository.countByStatus(JobStatus.OPEN)).thenReturn(10L);
        when(jobRequirementRepository.countByStatus(JobStatus.IN_PROGRESS)).thenReturn(3L);
        when(jobRequirementRepository.countByStatus(JobStatus.CLOSED)).thenReturn(2L);

        // When
        JobRequirementService.JobRequirementStats stats = jobRequirementService.getJobRequirementStats();

        // Then
        assertNotNull(stats);
        assertEquals(15L, stats.getTotalJobs());
        assertEquals(10L, stats.getOpenJobs());
        assertEquals(3L, stats.getInProgressJobs());
        assertEquals(2L, stats.getClosedJobs());
        verify(jobRequirementRepository).count();
        verify(jobRequirementRepository).countByStatus(JobStatus.OPEN);
        verify(jobRequirementRepository).countByStatus(JobStatus.IN_PROGRESS);
        verify(jobRequirementRepository).countByStatus(JobStatus.CLOSED);
    }
}
