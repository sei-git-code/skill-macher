package com.ses.salessupport.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ses.salessupport.entity.Employee;
import com.ses.salessupport.entity.EmployeeSkill;
import com.ses.salessupport.entity.JobRequirement;
import com.ses.salessupport.entity.MatchingResult;
import com.ses.salessupport.entity.SkillMapping;
import com.ses.salessupport.repository.EmployeeRepository;
import com.ses.salessupport.repository.EmployeeSkillRepository;
import com.ses.salessupport.repository.JobRequirementRepository;
import com.ses.salessupport.repository.MatchingResultRepository;
import com.ses.salessupport.repository.SkillMappingRepository;

@Service
@Transactional
public class SkillMatchingService {

    @Autowired
    private JobRequirementRepository jobRequirementRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeSkillRepository employeeSkillRepository;

    @Autowired
    private SkillMappingRepository skillMappingRepository;

    @Autowired
    private MatchingResultRepository matchingResultRepository;

    /**
     * ジョブ要件と従業員のスキルマッチングを実行
     */
    public List<MatchingResult> performMatching(Long jobRequirementId) {
        JobRequirement jobRequirement = jobRequirementRepository.findById(jobRequirementId)
            .orElseThrow(() -> new RuntimeException("ジョブ要件が見つかりません: " + jobRequirementId));

        List<Employee> activeEmployees = employeeRepository.findByStatus(Employee.EmployeeStatus.ACTIVE);
        List<MatchingResult> results = new ArrayList<>();

        for (Employee employee : activeEmployees) {
            MatchingResult result = calculateMatchScore(jobRequirement, employee);
            if (result.getMatchScore().compareTo(BigDecimal.valueOf(30.0)) >= 0) { // 30%以上のマッチングのみ
                results.add(result);
            }
        }

        // マッチングスコアでソート
        results.sort((a, b) -> b.getMatchScore().compareTo(a.getMatchScore()));

        // 結果を保存
        for (MatchingResult result : results) {
            matchingResultRepository.save(result);
        }

        return results;
    }

    /**
     * マッチングスコアを計算
     */
    private MatchingResult calculateMatchScore(JobRequirement jobRequirement, Employee employee) {
        MatchingResult result = new MatchingResult(jobRequirement, employee, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

        // スキルマッチングスコア計算
        BigDecimal skillScore = calculateSkillMatchScore(jobRequirement, employee);
        result.setSkillMatchScore(skillScore);

        // 経験年数マッチングスコア計算
        BigDecimal experienceScore = calculateExperienceMatchScore(jobRequirement, employee);
        result.setExperienceMatchScore(experienceScore);

        // 予算マッチングスコア計算
        BigDecimal budgetScore = calculateBudgetMatchScore(jobRequirement, employee);
        result.setBudgetMatchScore(budgetScore);

        // 総合マッチングスコア計算（重み付け）
        BigDecimal totalScore = skillScore.multiply(BigDecimal.valueOf(0.5))
            .add(experienceScore.multiply(BigDecimal.valueOf(0.3)))
            .add(budgetScore.multiply(BigDecimal.valueOf(0.2)));

        result.setMatchScore(totalScore);

        // マッチしたスキルと不足スキルを設定
        setMatchedAndMissingSkills(jobRequirement, employee, result);

        // 推奨理由を設定
        setRecommendationReason(result);

        return result;
    }

    /**
     * スキルマッチングスコアを計算
     */
    private BigDecimal calculateSkillMatchScore(JobRequirement jobRequirement, Employee employee) {
        List<String> requiredSkills = parseSkills(jobRequirement.getRequiredSkills());
        List<EmployeeSkill> employeeSkills = employeeSkillRepository.findByEmployee(employee);

        if (requiredSkills.isEmpty()) {
            return BigDecimal.ZERO;
        }

        int matchedSkills = 0;
        BigDecimal totalProficiency = BigDecimal.ZERO;

        for (String requiredSkill : requiredSkills) {
            Optional<EmployeeSkill> matchingSkill = employeeSkills.stream()
                .filter(skill -> isSkillMatch(skill.getSkillName(), requiredSkill))
                .findFirst();

            if (matchingSkill.isPresent()) {
                matchedSkills++;
                totalProficiency = totalProficiency.add(BigDecimal.valueOf(matchingSkill.get().getProficiencyLevel()));
            }
        }

        if (matchedSkills == 0) {
            return BigDecimal.ZERO;
        }

        // スキルマッチ率と熟練度の平均を計算
        BigDecimal matchRate = BigDecimal.valueOf(matchedSkills)
            .divide(BigDecimal.valueOf(requiredSkills.size()), 4, RoundingMode.HALF_UP);
        
        BigDecimal avgProficiency = totalProficiency
            .divide(BigDecimal.valueOf(matchedSkills), 4, RoundingMode.HALF_UP);

        // スキルマッチ率70%、熟練度30%で重み付け
        return matchRate.multiply(BigDecimal.valueOf(0.7))
            .add(avgProficiency.divide(BigDecimal.valueOf(5), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(0.3)))
            .multiply(BigDecimal.valueOf(100));
    }

    /**
     * 経験年数マッチングスコアを計算
     */
    private BigDecimal calculateExperienceMatchScore(JobRequirement jobRequirement, Employee employee) {
        int requiredYears = jobRequirement.getRequiredExperienceYears();
        
        // 従業員の総経験年数を計算（最も高いスキルの経験年数を使用）
        List<EmployeeSkill> skills = employeeSkillRepository.findByEmployee(employee);
        int maxExperienceYears = skills.stream()
            .mapToInt(EmployeeSkill::getYearsOfExperience)
            .max()
            .orElse(0);

        if (requiredYears == 0) {
            return BigDecimal.valueOf(100);
        }

        if (maxExperienceYears >= requiredYears) {
            return BigDecimal.valueOf(100);
        } else {
            // 経験年数が不足している場合のスコア計算
            return BigDecimal.valueOf(maxExperienceYears)
                .divide(BigDecimal.valueOf(requiredYears), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        }
    }

    /**
     * 予算マッチングスコアを計算
     */
    private BigDecimal calculateBudgetMatchScore(JobRequirement jobRequirement, Employee employee) {
        BigDecimal jobBudget = jobRequirement.getBudget();
        BigDecimal employeeHourlyRate = employee.getHourlyRate();
        Integer durationMonths = jobRequirement.getDurationMonths();

        if (jobBudget == null || employeeHourlyRate == null || durationMonths == null) {
            return BigDecimal.valueOf(50); // デフォルトスコア
        }

        // 月間稼働時間を160時間と仮定
        BigDecimal monthlyHours = BigDecimal.valueOf(160);
        BigDecimal totalHours = monthlyHours.multiply(BigDecimal.valueOf(durationMonths));
        BigDecimal estimatedCost = employeeHourlyRate.multiply(totalHours);

        // 予算との差を計算
        BigDecimal costDifference = estimatedCost.subtract(jobBudget);
        BigDecimal costRatio = costDifference.divide(jobBudget, 4, RoundingMode.HALF_UP);

        if (costRatio.compareTo(BigDecimal.ZERO) <= 0) {
            // 予算内の場合
            return BigDecimal.valueOf(100);
        } else if (costRatio.compareTo(BigDecimal.valueOf(0.2)) <= 0) {
            // 20%以内の超過
            return BigDecimal.valueOf(80);
        } else if (costRatio.compareTo(BigDecimal.valueOf(0.5)) <= 0) {
            // 50%以内の超過
            return BigDecimal.valueOf(60);
        } else {
            // 50%以上の超過
            return BigDecimal.valueOf(30);
        }
    }

    /**
     * スキルがマッチするかどうかを判定
     */
    private boolean isSkillMatch(String employeeSkill, String requiredSkill) {
        // 完全一致
        if (employeeSkill.equalsIgnoreCase(requiredSkill)) {
            return true;
        }

        // 部分一致
        if (employeeSkill.toLowerCase().contains(requiredSkill.toLowerCase()) ||
            requiredSkill.toLowerCase().contains(employeeSkill.toLowerCase())) {
            return true;
        }

        // スキルマッピングを使用した関連性チェック
        Optional<SkillMapping> mapping = skillMappingRepository.findBySkillName(employeeSkill);
        if (mapping.isPresent()) {
            SkillMapping skillMap = mapping.get();
            return skillMap.getRelatedSkill1().equalsIgnoreCase(requiredSkill) ||
                   skillMap.getRelatedSkill2().equalsIgnoreCase(requiredSkill) ||
                   skillMap.getRelatedSkill3().equalsIgnoreCase(requiredSkill) ||
                   skillMap.getRelatedSkill4().equalsIgnoreCase(requiredSkill) ||
                   skillMap.getRelatedSkill5().equalsIgnoreCase(requiredSkill);
        }

        return false;
    }

    /**
     * スキル文字列をパース
     */
    private List<String> parseSkills(String skillsString) {
        if (skillsString == null || skillsString.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(skillsString.split("[,;]"))
            .map(String::trim)
            .filter(skill -> !skill.isEmpty())
            .collect(Collectors.toList());
    }

    /**
     * マッチしたスキルと不足スキルを設定
     */
    private void setMatchedAndMissingSkills(JobRequirement jobRequirement, Employee employee, MatchingResult result) {
        List<String> requiredSkills = parseSkills(jobRequirement.getRequiredSkills());
        List<EmployeeSkill> employeeSkills = employeeSkillRepository.findByEmployee(employee);

        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        for (String requiredSkill : requiredSkills) {
            boolean isMatched = employeeSkills.stream()
                .anyMatch(skill -> isSkillMatch(skill.getSkillName(), requiredSkill));

            if (isMatched) {
                matchedSkills.add(requiredSkill);
            } else {
                missingSkills.add(requiredSkill);
            }
        }

        result.setMatchedSkills(String.join(", ", matchedSkills));
        result.setMissingSkills(String.join(", ", missingSkills));
    }

    /**
     * 推奨理由を設定
     */
    private void setRecommendationReason(MatchingResult result) {
        StringBuilder reason = new StringBuilder();

        if (result.getSkillMatchScore().compareTo(BigDecimal.valueOf(80)) >= 0) {
            reason.append("スキルマッチングが非常に高い。");
        } else if (result.getSkillMatchScore().compareTo(BigDecimal.valueOf(60)) >= 0) {
            reason.append("スキルマッチングが良好。");
        } else {
            reason.append("スキルマッチングに改善の余地あり。");
        }

        if (result.getExperienceMatchScore().compareTo(BigDecimal.valueOf(80)) >= 0) {
            reason.append(" 経験年数が十分。");
        } else if (result.getExperienceMatchScore().compareTo(BigDecimal.valueOf(60)) >= 0) {
            reason.append(" 経験年数は適切。");
        } else {
            reason.append(" 経験年数が不足している可能性。");
        }

        if (result.getBudgetMatchScore().compareTo(BigDecimal.valueOf(80)) >= 0) {
            reason.append(" 予算内で対応可能。");
        } else {
            reason.append(" 予算を超過する可能性。");
        }

        result.setRecommendationReason(reason.toString());
    }
}
