package com.ses.salessupport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ses.salessupport.service.DashboardService;

@Controller
@RequestMapping("/")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * ダッシュボードのメインページ
     */
    @GetMapping
    public String dashboard(Model model) {
        // 統計情報を取得
        DashboardService.DashboardStats stats = dashboardService.getDashboardStats();
        model.addAttribute("stats", stats);

        // 最近のアクティビティを取得
        DashboardService.RecentActivity recentActivity = dashboardService.getRecentActivity();
        model.addAttribute("recentActivity", recentActivity);

        // 優先度別ジョブ要件分布
        model.addAttribute("jobPriorityDistribution", dashboardService.getJobRequirementDistributionByPriority());

        // ステータス別ジョブ要件分布
        model.addAttribute("jobStatusDistribution", dashboardService.getJobRequirementDistributionByStatus());

        // 雇用形態別従業員分布
        model.addAttribute("employeeTypeDistribution", dashboardService.getEmployeeDistributionByEmploymentType());

        // マッチングスコア分布
        model.addAttribute("matchingScoreDistribution", dashboardService.getMatchingScoreDistribution());

        // 月別ジョブ要件推移
        model.addAttribute("jobTrendByMonth", dashboardService.getJobRequirementTrendByMonth());

        // システムログ統計
        model.addAttribute("systemLogStats", dashboardService.getSystemLogStats());

        return "dashboard";
    }

    /**
     * ダッシュボードの統計情報をJSONで取得（API用）
     */
    @GetMapping("/api/dashboard/stats")
    public DashboardService.DashboardStats getDashboardStats() {
        return dashboardService.getDashboardStats();
    }

    /**
     * 最近のアクティビティをJSONで取得（API用）
     */
    @GetMapping("/api/dashboard/recent-activity")
    public DashboardService.RecentActivity getRecentActivity() {
        return dashboardService.getRecentActivity();
    }
}
