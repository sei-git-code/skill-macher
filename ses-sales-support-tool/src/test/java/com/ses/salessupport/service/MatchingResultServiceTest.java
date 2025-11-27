package com.ses.salessupport.service;

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

import com.ses.salessupport.entity.Employee;
import com.ses.salessupport.entity.JobRequirement;
import com.ses.salessupport.entity.MatchingResult;
import com.ses.salessupport.entity.MatchingResult.MatchingStatus;
import com.ses.salessupport.repository.MatchingResultRepository;

/**
 * MatchingResultServiceのユニットテスト
 */
@ExtendWith(MockitoExtension.class)
class MatchingResultServiceTest {

    @Mock
    private MatchingResultRepository matchingResultRepository;

    @InjectMocks
    private MatchingResultService matchingResultService;

    private MatchingResult matchingResult;
    private JobRequirement jobRequirement;
    private Employee employee;

    @BeforeEach
    void setUp() {
        // テスト用のJobRequirementを作成
        jobRequirement = new JobRequirement();
        jobRequirement.setId(1L);
        jobRequirement.setProjectName("テストプロジェクト");

        // テスト用のEmployeeを作成
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("田中");
        employee.setLastName("太郎");

        // テスト用のMatchingResultを作成
        matchingResult = new MatchingResult();
        matchingResult.setId(1L);
        matchingResult.setJobRequirement(jobRequirement);
        matchingResult.setEmployee(employee);
        matchingResult.setStatus(MatchingStatus.PENDING);
    }

    @Test
    void testGetMatchingResultsByJobRequirement_Success() {
        // Given
        List<MatchingResult> expectedResults = Arrays.asList(matchingResult);
        when(matchingResultRepository.findByJobRequirementOrderByMatchScoreDesc(any(JobRequirement.class)))
                .thenReturn(expectedResults);

        // When
        List<MatchingResult> results = matchingResultService.getMatchingResultsByJobRequirement(1L);

        // Then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(matchingResult.getId(), results.get(0).getId());
        verify(matchingResultRepository).findByJobRequirementOrderByMatchScoreDesc(any(JobRequirement.class));
    }

    @Test
    void testGetMatchingResultsByEmployee_Success() {
        // Given
        List<MatchingResult> expectedResults = Arrays.asList(matchingResult);
        when(matchingResultRepository.findByEmployeeOrderByMatchScoreDesc(any(Employee.class)))
                .thenReturn(expectedResults);

        // When
        List<MatchingResult> results = matchingResultService.getMatchingResultsByEmployee(1L);

        // Then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(matchingResult.getId(), results.get(0).getId());
        verify(matchingResultRepository).findByEmployeeOrderByMatchScoreDesc(any(Employee.class));
    }

    @Test
    void testGetMatchingResultById_Success() {
        // Given
        when(matchingResultRepository.findById(1L)).thenReturn(Optional.of(matchingResult));

        // When
        Optional<MatchingResult> result = matchingResultService.getMatchingResultById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(matchingResult.getId(), result.get().getId());
        verify(matchingResultRepository).findById(1L);
    }

    @Test
    void testGetMatchingResultById_NotFound() {
        // Given
        when(matchingResultRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<MatchingResult> result = matchingResultService.getMatchingResultById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(matchingResultRepository).findById(1L);
    }

    @Test
    void testUpdateMatchingResultStatus_Success() {
        // Given
        String comments = "テストコメント";
        when(matchingResultRepository.findById(1L)).thenReturn(Optional.of(matchingResult));
        when(matchingResultRepository.save(any(MatchingResult.class))).thenReturn(matchingResult);

        // When
        MatchingResult result = matchingResultService.updateMatchingResultStatus(1L, MatchingStatus.APPROVED, comments);

        // Then
        assertNotNull(result);
        assertEquals(MatchingStatus.APPROVED, result.getStatus());
        assertEquals(comments, result.getReviewNotes());
        verify(matchingResultRepository).findById(1L);
        verify(matchingResultRepository).save(matchingResult);
    }

    @Test
    void testUpdateMatchingResultStatus_NotFound() {
        // Given
        when(matchingResultRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            matchingResultService.updateMatchingResultStatus(1L, MatchingStatus.APPROVED, "コメント");
        });
    }

    @Test
    void testApproveMatchingResult_Success() {
        // Given
        String comments = "承認コメント";
        when(matchingResultRepository.findById(1L)).thenReturn(Optional.of(matchingResult));
        when(matchingResultRepository.save(any(MatchingResult.class))).thenReturn(matchingResult);

        // When
        MatchingResult result = matchingResultService.approveMatchingResult(1L, comments);

        // Then
        assertNotNull(result);
        assertEquals(MatchingStatus.APPROVED, result.getStatus());
        assertEquals(comments, result.getReviewNotes());
        verify(matchingResultRepository).findById(1L);
        verify(matchingResultRepository).save(matchingResult);
    }

    @Test
    void testRejectMatchingResult_Success() {
        // Given
        String reason = "却下理由";
        when(matchingResultRepository.findById(1L)).thenReturn(Optional.of(matchingResult));
        when(matchingResultRepository.save(any(MatchingResult.class))).thenReturn(matchingResult);

        // When
        MatchingResult result = matchingResultService.rejectMatchingResult(1L, reason);

        // Then
        assertNotNull(result);
        assertEquals(MatchingStatus.REJECTED, result.getStatus());
        assertEquals(reason, result.getReviewNotes());
        verify(matchingResultRepository).findById(1L);
        verify(matchingResultRepository).save(matchingResult);
    }

    @Test
    void testDeleteMatchingResult_Success() {
        // Given
        when(matchingResultRepository.existsById(1L)).thenReturn(true);

        // When
        matchingResultService.deleteMatchingResult(1L);

        // Then
        verify(matchingResultRepository).existsById(1L);
        verify(matchingResultRepository).deleteById(1L);
    }

    @Test
    void testDeleteMatchingResult_NotFound() {
        // Given
        when(matchingResultRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            matchingResultService.deleteMatchingResult(1L);
        });
    }

    @Test
    void testCountByStatus_Success() {
        // Given
        when(matchingResultRepository.countByStatus(MatchingStatus.PENDING)).thenReturn(5L);

        // When
        long count = matchingResultService.countByStatus(MatchingStatus.PENDING);

        // Then
        assertEquals(5L, count);
        verify(matchingResultRepository).countByStatus(MatchingStatus.PENDING);
    }

    @Test
    void testCountAll_Success() {
        // Given
        when(matchingResultRepository.count()).thenReturn(10L);

        // When
        long count = matchingResultService.countAll();

        // Then
        assertEquals(10L, count);
        verify(matchingResultRepository).count();
    }
}

