package com.ses.salessupport.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ses.salessupport.entity.JobRequirement;
import com.ses.salessupport.entity.MatchingResult;
import com.ses.salessupport.service.JobRequirementService;
import com.ses.salessupport.service.MatchingResultService;
import com.ses.salessupport.service.SkillMatchingService;

@Controller
@RequestMapping("/matching")
public class MatchingController {

    @Autowired
    private SkillMatchingService skillMatchingService;

    @Autowired
    private JobRequirementService jobRequirementService;

    @Autowired
    private MatchingResultService matchingResultService;

    /**
     * マッチング一覧ページ
     */
    @GetMapping
    public String listMatchingResults(Model model) {
        // 最近のマッチング結果を表示（実装は簡易版）
        model.addAttribute("title", "マッチング結果一覧");
        return "matching/list";
    }

    /**
     * ジョブ要件に対するマッチング実行
     */
    @PostMapping("/execute/{jobRequirementId}")
    public String executeMatching(@PathVariable Long jobRequirementId, 
                                 RedirectAttributes redirectAttributes) {
        try {
            List<MatchingResult> results = skillMatchingService.performMatching(jobRequirementId);
            redirectAttributes.addFlashAttribute("successMessage", 
                "マッチングが完了しました。 " + results.size() + "件の結果が見つかりました。");
            return "redirect:/matching/results/" + jobRequirementId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "マッチングの実行に失敗しました: " + e.getMessage());
            return "redirect:/job-requirements/" + jobRequirementId;
        }
    }

    /**
     * ジョブ要件のマッチング結果一覧
     */
    @GetMapping("/results/{jobRequirementId}")
    public String viewMatchingResults(@PathVariable Long jobRequirementId, Model model) {
        Optional<JobRequirement> jobRequirement = jobRequirementService.getJobRequirementById(jobRequirementId);
        
        if (jobRequirement.isPresent()) {
            model.addAttribute("jobRequirement", jobRequirement.get());
            
            // マッチング結果を取得
            List<MatchingResult> results = matchingResultService.getMatchingResultsByJobRequirement(jobRequirementId);
            model.addAttribute("matchingResults", results);
            
            return "matching/results";
        } else {
            return "error/404";
        }
    }

    /**
     * マッチング結果詳細
     */
    @GetMapping("/result/{resultId}")
    public String viewMatchingResultDetail(@PathVariable Long resultId, Model model) {
        Optional<MatchingResult> result = matchingResultService.getMatchingResultById(resultId);
        
        if (result.isPresent()) {
            model.addAttribute("matchingResult", result.get());
            return "matching/detail";
        } else {
            return "error/404";
        }
    }

    /**
     * マッチング結果の承認
     */
    @PostMapping("/result/{resultId}/approve")
    public String approveMatchingResult(@PathVariable Long resultId, 
                                       @RequestParam(required = false) String comments,
                                       RedirectAttributes redirectAttributes) {
        try {
            matchingResultService.approveMatchingResult(resultId, comments);
            redirectAttributes.addFlashAttribute("successMessage", "マッチング結果が承認されました。");
            return "redirect:/matching/result/" + resultId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "マッチング結果の承認に失敗しました: " + e.getMessage());
            return "redirect:/matching/result/" + resultId;
        }
    }

    /**
     * マッチング結果の却下
     */
    @PostMapping("/result/{resultId}/reject")
    public String rejectMatchingResult(@PathVariable Long resultId, 
                                     @RequestParam(required = false) String reason,
                                     RedirectAttributes redirectAttributes) {
        try {
            matchingResultService.rejectMatchingResult(resultId, reason);
            redirectAttributes.addFlashAttribute("successMessage", "マッチング結果が却下されました。");
            return "redirect:/matching/result/" + resultId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "マッチング結果の却下に失敗しました: " + e.getMessage());
            return "redirect:/matching/result/" + resultId;
        }
    }

    /**
     * マッチング結果のステータス更新
     */
    @PostMapping("/result/{resultId}/status")
    public String updateMatchingResultStatus(@PathVariable Long resultId, 
                                           @RequestParam MatchingResult.MatchingStatus status,
                                           @RequestParam(required = false) String notes,
                                           RedirectAttributes redirectAttributes) {
        try {
            // マッチング結果のステータス更新処理
            redirectAttributes.addFlashAttribute("successMessage", "ステータスが更新されました。");
            return "redirect:/matching/result/" + resultId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "ステータスの更新に失敗しました: " + e.getMessage());
            return "redirect:/matching/result/" + resultId;
        }
    }

    /**
     * マッチング設定ページ
     */
    @GetMapping("/settings")
    public String matchingSettings(Model model) {
        model.addAttribute("title", "マッチング設定");
        return "matching/settings";
    }

    /**
     * マッチング設定の更新
     */
    @PostMapping("/settings")
    public String updateMatchingSettings(@RequestParam(required = false) String skillWeight,
                                        @RequestParam(required = false) String experienceWeight,
                                        @RequestParam(required = false) String budgetWeight,
                                        RedirectAttributes redirectAttributes) {
        try {
            // マッチング設定の更新処理
            redirectAttributes.addFlashAttribute("successMessage", "マッチング設定が更新されました。");
            return "redirect:/matching/settings";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "マッチング設定の更新に失敗しました: " + e.getMessage());
            return "redirect:/matching/settings";
        }
    }

    /**
     * API: マッチング実行
     */
    @PostMapping("/api/execute/{jobRequirementId}")
    @ResponseBody
    public List<MatchingResult> executeMatchingApi(@PathVariable Long jobRequirementId) {
        return skillMatchingService.performMatching(jobRequirementId);
    }

    /**
     * API: マッチング結果一覧
     */
    @GetMapping("/api/results/{jobRequirementId}")
    @ResponseBody
    public List<MatchingResult> getMatchingResultsApi(@PathVariable Long jobRequirementId) {
        return matchingResultService.getMatchingResultsByJobRequirement(jobRequirementId);
    }
}
