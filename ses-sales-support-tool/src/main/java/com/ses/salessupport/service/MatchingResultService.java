package com.ses.salessupport.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ses.salessupport.entity.Employee;
import com.ses.salessupport.entity.JobRequirement;
import com.ses.salessupport.entity.MatchingResult;
import com.ses.salessupport.entity.MatchingResult.MatchingStatus;
import com.ses.salessupport.repository.MatchingResultRepository;

@Service
@Transactional
public class MatchingResultService {

    @Autowired
    private MatchingResultRepository matchingResultRepository;

    /**
     * ジョブ要件IDでマッチング結果を取得
     */
    public List<MatchingResult> getMatchingResultsByJobRequirement(Long jobRequirementId) {
        JobRequirement jobRequirement = new JobRequirement();
        jobRequirement.setId(jobRequirementId);
        return matchingResultRepository.findByJobRequirementOrderByMatchScoreDesc(jobRequirement);
    }

    /**
     * 従業員IDでマッチング結果を取得
     */
    public List<MatchingResult> getMatchingResultsByEmployee(Long employeeId) {
        Employee employee = new Employee();
        employee.setId(employeeId);
        return matchingResultRepository.findByEmployeeOrderByMatchScoreDesc(employee);
    }

    /**
     * マッチング結果IDで取得
     */
    public Optional<MatchingResult> getMatchingResultById(Long id) {
        return matchingResultRepository.findById(id);
    }

    /**
     * マッチング結果のステータスを更新
     */
    public MatchingResult updateMatchingResultStatus(Long id, MatchingStatus status, String comments) {
        MatchingResult result = matchingResultRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("マッチング結果が見つかりません: " + id));
        
        result.setStatus(status);
        if (comments != null && !comments.trim().isEmpty()) {
            result.setReviewNotes(comments);
        }
        
        return matchingResultRepository.save(result);
    }

    /**
     * マッチング結果を承認
     */
    public MatchingResult approveMatchingResult(Long id, String comments) {
        return updateMatchingResultStatus(id, MatchingStatus.APPROVED, comments);
    }

    /**
     * マッチング結果を却下
     */
    public MatchingResult rejectMatchingResult(Long id, String comments) {
        return updateMatchingResultStatus(id, MatchingStatus.REJECTED, comments);
    }

    /**
     * マッチング結果を削除
     */
    public void deleteMatchingResult(Long id) {
        if (!matchingResultRepository.existsById(id)) {
            throw new RuntimeException("マッチング結果が見つかりません: " + id);
        }
        matchingResultRepository.deleteById(id);
    }

    /**
     * ステータス別のマッチング結果数を取得
     */
    public long countByStatus(MatchingStatus status) {
        return matchingResultRepository.countByStatus(status);
    }

    /**
     * 全マッチング結果数を取得
     */
    public long countAll() {
        return matchingResultRepository.count();
    }
}
