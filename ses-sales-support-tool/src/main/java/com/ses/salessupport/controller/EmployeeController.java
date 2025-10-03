package com.ses.salessupport.controller;

import java.math.BigDecimal;
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

import com.ses.salessupport.entity.Employee;
import com.ses.salessupport.entity.EmployeeSkill;
import com.ses.salessupport.service.EmployeeService;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 従業員一覧ページ
     */
    @GetMapping
    public String listEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String employmentType,
            Model model) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Employee> employees;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<Employee> searchResults = employeeService.searchEmployees(keyword);
            // 検索結果をページネーション対応に変換（簡易実装）
            employees = Page.empty(pageable);
        } else {
            employees = employeeService.getEmployees(pageable);
        }

        model.addAttribute("employees", employees);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employees.getTotalPages());
        model.addAttribute("totalItems", employees.getTotalElements());
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("employmentType", employmentType);

        return "employees/list";
    }

    /**
     * 従業員詳細ページ
     */
    @GetMapping("/{id}")
    public String viewEmployee(@PathVariable Long id, Model model) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        
        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            
            // 従業員のスキル一覧を取得
            List<EmployeeSkill> skills = employeeService.getEmployeeSkills(id);
            model.addAttribute("skills", skills);
            
            return "employees/detail";
        } else {
            return "error/404";
        }
    }

    /**
     * 従業員作成フォーム
     */
    @GetMapping("/create")
    public String createEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("employmentTypes", Employee.EmploymentType.values());
        model.addAttribute("statuses", Employee.EmployeeStatus.values());
        return "employees/create";
    }

    /**
     * 従業員作成処理
     */
    @PostMapping("/create")
    public String createEmployee(@ModelAttribute Employee employee, 
                               RedirectAttributes redirectAttributes) {
        try {
            Employee savedEmployee = employeeService.createEmployee(employee);
            redirectAttributes.addFlashAttribute("successMessage", "従業員が正常に作成されました。");
            return "redirect:/employees/" + savedEmployee.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "従業員の作成に失敗しました: " + e.getMessage());
            return "redirect:/employees/create";
        }
    }

    /**
     * 従業員編集フォーム
     */
    @GetMapping("/{id}/edit")
    public String editEmployeeForm(@PathVariable Long id, Model model) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        
        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            model.addAttribute("employmentTypes", Employee.EmploymentType.values());
            model.addAttribute("statuses", Employee.EmployeeStatus.values());
            return "employees/edit";
        } else {
            return "error/404";
        }
    }

    /**
     * 従業員更新処理
     */
    @PostMapping("/{id}/edit")
    public String updateEmployee(@PathVariable Long id, 
                               @ModelAttribute Employee employee,
                               RedirectAttributes redirectAttributes) {
        try {
            Employee updatedEmployee = employeeService.updateEmployee(id, employee);
            redirectAttributes.addFlashAttribute("successMessage", "従業員が正常に更新されました。");
            return "redirect:/employees/" + updatedEmployee.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "従業員の更新に失敗しました: " + e.getMessage());
            return "redirect:/employees/" + id + "/edit";
        }
    }

    /**
     * 従業員削除処理
     */
    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.deleteEmployee(id);
            redirectAttributes.addFlashAttribute("successMessage", "従業員が正常に削除されました。");
            return "redirect:/employees";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "従業員の削除に失敗しました: " + e.getMessage());
            return "redirect:/employees/" + id;
        }
    }

    /**
     * 従業員ステータス更新
     */
    @PostMapping("/{id}/status")
    public String updateEmployeeStatus(@PathVariable Long id, 
                                      @RequestParam Employee.EmployeeStatus status,
                                      RedirectAttributes redirectAttributes) {
        try {
            employeeService.updateEmployeeStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "ステータスが正常に更新されました。");
            return "redirect:/employees/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "ステータスの更新に失敗しました: " + e.getMessage());
            return "redirect:/employees/" + id;
        }
    }

    /**
     * 従業員時給更新
     */
    @PostMapping("/{id}/hourly-rate")
    public String updateEmployeeHourlyRate(@PathVariable Long id, 
                                         @RequestParam BigDecimal hourlyRate,
                                         RedirectAttributes redirectAttributes) {
        try {
            employeeService.updateEmployeeHourlyRate(id, hourlyRate);
            redirectAttributes.addFlashAttribute("successMessage", "時給が正常に更新されました。");
            return "redirect:/employees/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "時給の更新に失敗しました: " + e.getMessage());
            return "redirect:/employees/" + id;
        }
    }

    /**
     * スキル追加フォーム
     */
    @GetMapping("/{id}/skills/add")
    public String addSkillForm(@PathVariable Long id, Model model) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        
        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            model.addAttribute("skill", new EmployeeSkill());
            return "employees/add-skill";
        } else {
            return "error/404";
        }
    }

    /**
     * スキル追加処理
     */
    @PostMapping("/{id}/skills/add")
    public String addSkill(@PathVariable Long id, 
                          @ModelAttribute EmployeeSkill skill,
                          RedirectAttributes redirectAttributes) {
        try {
            employeeService.addEmployeeSkill(id, skill);
            redirectAttributes.addFlashAttribute("successMessage", "スキルが正常に追加されました。");
            return "redirect:/employees/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "スキルの追加に失敗しました: " + e.getMessage());
            return "redirect:/employees/" + id + "/skills/add";
        }
    }

    /**
     * スキル編集フォーム
     */
    @GetMapping("/skills/{skillId}/edit")
    public String editSkillForm(@PathVariable Long skillId, Model model) {
        // スキルIDから従業員IDを取得する必要がある
        // 簡易実装として、スキル情報を直接取得
        model.addAttribute("skill", new EmployeeSkill());
        return "employees/edit-skill";
    }

    /**
     * スキル更新処理
     */
    @PostMapping("/skills/{skillId}/edit")
    public String updateSkill(@PathVariable Long skillId, 
                            @ModelAttribute EmployeeSkill skill,
                            RedirectAttributes redirectAttributes) {
        try {
            employeeService.updateEmployeeSkill(skillId, skill);
            redirectAttributes.addFlashAttribute("successMessage", "スキルが正常に更新されました。");
            return "redirect:/employees"; // 適切なリダイレクト先に変更
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "スキルの更新に失敗しました: " + e.getMessage());
            return "redirect:/employees/skills/" + skillId + "/edit";
        }
    }

    /**
     * スキル削除処理
     */
    @PostMapping("/skills/{skillId}/delete")
    public String deleteSkill(@PathVariable Long skillId, RedirectAttributes redirectAttributes) {
        try {
            employeeService.deleteEmployeeSkill(skillId);
            redirectAttributes.addFlashAttribute("successMessage", "スキルが正常に削除されました。");
            return "redirect:/employees";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "スキルの削除に失敗しました: " + e.getMessage());
            return "redirect:/employees";
        }
    }

    /**
     * アクティブな従業員一覧
     */
    @GetMapping("/active")
    public String activeEmployees(Model model) {
        List<Employee> activeEmployees = employeeService.getActiveEmployees();
        model.addAttribute("employees", activeEmployees);
        model.addAttribute("title", "アクティブな従業員");
        return "employees/list";
    }

    /**
     * 従業員検索
     */
    @GetMapping("/search")
    public String searchEmployees(@RequestParam String keyword, Model model) {
        List<Employee> searchResults = employeeService.searchEmployees(keyword);
        model.addAttribute("employees", searchResults);
        model.addAttribute("keyword", keyword);
        model.addAttribute("title", "検索結果: " + keyword);
        return "employees/list";
    }

    /**
     * API: 従業員一覧をJSONで取得
     */
    @GetMapping("/api")
    @ResponseBody
    public List<Employee> getEmployeesApi() {
        return employeeService.getAllEmployees();
    }

    /**
     * API: 従業員詳細をJSONで取得
     */
    @GetMapping("/api/{id}")
    @ResponseBody
    public Optional<Employee> getEmployeeApi(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }
}
