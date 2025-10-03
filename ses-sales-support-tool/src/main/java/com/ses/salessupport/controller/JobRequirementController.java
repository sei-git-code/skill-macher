package com.ses.salessupport.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ses.salessupport.entity.JobRequirement;
import com.ses.salessupport.service.JobRequirementService;

@Controller
@RequestMapping("/job-requirements")
public class JobRequirementController {

    @Autowired
    private JobRequirementService jobRequirementService;

    /**
     * ジョブ要件一覧ページ
     */
    @GetMapping
    public String listJobRequirements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            Model model) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<JobRequirement> jobRequirements;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<JobRequirement> searchResults = jobRequirementService.searchJobRequirements(keyword);
            // 検索結果をページネーション対応に変換（簡易実装）
            jobRequirements = Page.empty(pageable);
        } else {
            jobRequirements = jobRequirementService.getJobRequirements(pageable);
        }

        model.addAttribute("jobRequirements", jobRequirements);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", jobRequirements.getTotalPages());
        model.addAttribute("totalItems", jobRequirements.getTotalElements());
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("priority", priority);

        return "job-requirements/list";
    }

    /**
     * ジョブ要件詳細ページ
     */
    @GetMapping("/{id}")
    public String viewJobRequirement(@PathVariable Long id, Model model) {
        Optional<JobRequirement> jobRequirement = jobRequirementService.getJobRequirementById(id);
        
        if (jobRequirement.isPresent()) {
            model.addAttribute("jobRequirement", jobRequirement.get());
            return "job-requirements/detail";
        } else {
            return "error/404";
        }
    }

    /**
     * ジョブ要件作成フォーム
     */
    @GetMapping("/create")
    public String createJobRequirementForm(Model model) {
        model.addAttribute("jobRequirement", new JobRequirement());
        model.addAttribute("workTypes", JobRequirement.WorkType.values());
        model.addAttribute("priorities", JobRequirement.Priority.values());
        model.addAttribute("statuses", JobRequirement.JobStatus.values());
        return "job-requirements/create";
    }

    /**
     * ジョブ要件作成処理
     */
    @PostMapping("/create")
    public String createJobRequirement(@ModelAttribute JobRequirement jobRequirement, 
                                     RedirectAttributes redirectAttributes) {
        try {
            JobRequirement savedJobRequirement = jobRequirementService.createJobRequirement(jobRequirement);
            redirectAttributes.addFlashAttribute("successMessage", "ジョブ要件が正常に作成されました。");
            return "redirect:/job-requirements/" + savedJobRequirement.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "ジョブ要件の作成に失敗しました: " + e.getMessage());
            return "redirect:/job-requirements/create";
        }
    }

    /**
     * ジョブ要件編集フォーム
     */
    @GetMapping("/{id}/edit")
    public String editJobRequirementForm(@PathVariable Long id, Model model) {
        Optional<JobRequirement> jobRequirement = jobRequirementService.getJobRequirementById(id);
        
        if (jobRequirement.isPresent()) {
            model.addAttribute("jobRequirement", jobRequirement.get());
            model.addAttribute("workTypes", JobRequirement.WorkType.values());
            model.addAttribute("priorities", JobRequirement.Priority.values());
            model.addAttribute("statuses", JobRequirement.JobStatus.values());
            return "job-requirements/edit";
        } else {
            return "error/404";
        }
    }

    /**
     * ジョブ要件更新処理
     */
    @PostMapping("/{id}/edit")
    public String updateJobRequirement(@PathVariable Long id, 
                                     @ModelAttribute JobRequirement jobRequirement,
                                     RedirectAttributes redirectAttributes) {
        try {
            JobRequirement updatedJobRequirement = jobRequirementService.updateJobRequirement(id, jobRequirement);
            redirectAttributes.addFlashAttribute("successMessage", "ジョブ要件が正常に更新されました。");
            return "redirect:/job-requirements/" + updatedJobRequirement.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "ジョブ要件の更新に失敗しました: " + e.getMessage());
            return "redirect:/job-requirements/" + id + "/edit";
        }
    }

    /**
     * ジョブ要件削除処理
     */
    @PostMapping("/{id}/delete")
    public String deleteJobRequirement(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            jobRequirementService.deleteJobRequirement(id);
            redirectAttributes.addFlashAttribute("successMessage", "ジョブ要件が正常に削除されました。");
            return "redirect:/job-requirements";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "ジョブ要件の削除に失敗しました: " + e.getMessage());
            return "redirect:/job-requirements/" + id;
        }
    }

    /**
     * ジョブ要件ステータス更新
     */
    @PostMapping("/{id}/status")
    public String updateJobRequirementStatus(@PathVariable Long id, 
                                           @RequestParam JobRequirement.JobStatus status,
                                           RedirectAttributes redirectAttributes) {
        try {
            jobRequirementService.updateJobRequirementStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "ステータスが正常に更新されました。");
            return "redirect:/job-requirements/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "ステータスの更新に失敗しました: " + e.getMessage());
            return "redirect:/job-requirements/" + id;
        }
    }

    /**
     * オープンなジョブ要件一覧
     */
    @GetMapping("/open")
    public String openJobRequirements(Model model) {
        List<JobRequirement> openJobs = jobRequirementService.getOpenJobRequirements();
        model.addAttribute("jobRequirements", openJobs);
        model.addAttribute("title", "オープンなジョブ要件");
        return "job-requirements/list";
    }

    /**
     * ジョブ要件検索
     */
    @GetMapping("/search")
    public String searchJobRequirements(@RequestParam String keyword, Model model) {
        List<JobRequirement> searchResults = jobRequirementService.searchJobRequirements(keyword);
        model.addAttribute("jobRequirements", searchResults);
        model.addAttribute("keyword", keyword);
        model.addAttribute("title", "検索結果: " + keyword);
        return "job-requirements/list";
    }

    /**
     * API: ジョブ要件一覧をJSONで取得
     */
    @GetMapping("/api")
    @ResponseBody
    public List<JobRequirement> getJobRequirementsApi() {
        return jobRequirementService.getAllJobRequirements();
    }

    /**
     * API: ジョブ要件詳細をJSONで取得
     */
    @GetMapping("/api/{id}")
    @ResponseBody
    public Optional<JobRequirement> getJobRequirementApi(@PathVariable Long id) {
        return jobRequirementService.getJobRequirementById(id);
    }
}
