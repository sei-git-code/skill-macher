package com.ses.salessupport.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ses.salessupport.entity.Employee;
import com.ses.salessupport.entity.EmployeeSkill;
import com.ses.salessupport.entity.User;
import com.ses.salessupport.repository.EmployeeRepository;
import com.ses.salessupport.repository.EmployeeSkillRepository;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeSkillRepository employeeSkillRepository;

    @Autowired
    private AuthService authService;

    /**
     * 全ての従業員を取得
     */
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAllOrderByCreatedAtDesc();
    }

    /**
     * ページネーション付きで従業員を取得
     */
    @Transactional(readOnly = true)
    public Page<Employee> getEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    /**
     * IDで従業員を取得
     */
    @Transactional(readOnly = true)
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    /**
     * 従業員IDで従業員を取得
     */
    @Transactional(readOnly = true)
    public Optional<Employee> getEmployeeByEmployeeId(String employeeId) {
        return employeeRepository.findByEmployeeId(employeeId);
    }

    /**
     * ステータスで従業員を取得
     */
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByStatus(Employee.EmployeeStatus status) {
        return employeeRepository.findByStatus(status);
    }

    /**
     * 雇用形態で従業員を取得
     */
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByEmploymentType(Employee.EmploymentType employmentType) {
        return employeeRepository.findByEmploymentType(employmentType);
    }

    /**
     * アクティブな従業員を取得
     */
    @Transactional(readOnly = true)
    public List<Employee> getActiveEmployees() {
        return employeeRepository.findActiveEmployeesOrderByHireDate();
    }

    /**
     * キーワード検索
     */
    @Transactional(readOnly = true)
    public List<Employee> searchEmployees(String keyword) {
        return employeeRepository.findByKeyword(keyword);
    }

    /**
     * 時給範囲検索
     */
    @Transactional(readOnly = true)
    public List<Employee> searchEmployeesByHourlyRate(BigDecimal minRate, BigDecimal maxRate) {
        return employeeRepository.findByHourlyRateRange(minRate, maxRate);
    }

    /**
     * 都市で検索
     */
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByCity(String city) {
        return employeeRepository.findByCity(city);
    }

    /**
     * 国で検索
     */
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByCountry(String country) {
        return employeeRepository.findByCountry(country);
    }

    /**
     * 入社日以降の従業員を取得
     */
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesAfterHireDate(LocalDate hireDate) {
        return employeeRepository.findByHireDateAfter(hireDate);
    }

    /**
     * 新しい従業員を作成
     */
    public Employee createEmployee(Employee employee) {
        // 作成者を設定
        Optional<User> currentUser = authService.getCurrentUser();
        if (currentUser.isPresent()) {
            employee.setCreatedBy(currentUser.get());
        }

        return employeeRepository.save(employee);
    }

    /**
     * 従業員を更新
     */
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Employee existingEmployee = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("従業員が見つかりません: " + id));

        // 更新可能なフィールドを設定
        existingEmployee.setEmployeeId(updatedEmployee.getEmployeeId());
        existingEmployee.setFirstName(updatedEmployee.getFirstName());
        existingEmployee.setLastName(updatedEmployee.getLastName());
        existingEmployee.setEmail(updatedEmployee.getEmail());
        existingEmployee.setPhoneNumber(updatedEmployee.getPhoneNumber());
        existingEmployee.setDateOfBirth(updatedEmployee.getDateOfBirth());
        existingEmployee.setAddress(updatedEmployee.getAddress());
        existingEmployee.setCity(updatedEmployee.getCity());
        existingEmployee.setState(updatedEmployee.getState());
        existingEmployee.setPostalCode(updatedEmployee.getPostalCode());
        existingEmployee.setCountry(updatedEmployee.getCountry());
        existingEmployee.setHireDate(updatedEmployee.getHireDate());
        existingEmployee.setTerminationDate(updatedEmployee.getTerminationDate());
        existingEmployee.setHourlyRate(updatedEmployee.getHourlyRate());
        existingEmployee.setEmploymentType(updatedEmployee.getEmploymentType());
        existingEmployee.setStatus(updatedEmployee.getStatus());
        existingEmployee.setBio(updatedEmployee.getBio());
        existingEmployee.setProfileImageUrl(updatedEmployee.getProfileImageUrl());
        existingEmployee.setResumeUrl(updatedEmployee.getResumeUrl());
        existingEmployee.setPortfolioUrl(updatedEmployee.getPortfolioUrl());

        return employeeRepository.save(existingEmployee);
    }

    /**
     * 従業員を削除
     */
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("従業員が見つかりません: " + id);
        }
        employeeRepository.deleteById(id);
    }

    /**
     * 従業員のステータスを更新
     */
    public Employee updateEmployeeStatus(Long id, Employee.EmployeeStatus status) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("従業員が見つかりません: " + id));

        employee.setStatus(status);
        return employeeRepository.save(employee);
    }

    /**
     * 従業員の時給を更新
     */
    public Employee updateEmployeeHourlyRate(Long id, BigDecimal hourlyRate) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("従業員が見つかりません: " + id));

        employee.setHourlyRate(hourlyRate);
        return employeeRepository.save(employee);
    }

    /**
     * 従業員のスキルを追加
     */
    public EmployeeSkill addEmployeeSkill(Long employeeId, EmployeeSkill skill) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("従業員が見つかりません: " + employeeId));

        skill.setEmployee(employee);
        return employeeSkillRepository.save(skill);
    }

    /**
     * 従業員のスキルを更新
     */
    public EmployeeSkill updateEmployeeSkill(Long skillId, EmployeeSkill updatedSkill) {
        EmployeeSkill existingSkill = employeeSkillRepository.findById(skillId)
            .orElseThrow(() -> new RuntimeException("スキルが見つかりません: " + skillId));

        existingSkill.setSkillName(updatedSkill.getSkillName());
        existingSkill.setSkillCategory(updatedSkill.getSkillCategory());
        existingSkill.setProficiencyLevel(updatedSkill.getProficiencyLevel());
        existingSkill.setYearsOfExperience(updatedSkill.getYearsOfExperience());
        existingSkill.setDescription(updatedSkill.getDescription());
        existingSkill.setCertification(updatedSkill.getCertification());
        existingSkill.setIsVerified(updatedSkill.getIsVerified());

        return employeeSkillRepository.save(existingSkill);
    }

    /**
     * 従業員のスキルを削除
     */
    public void deleteEmployeeSkill(Long skillId) {
        if (!employeeSkillRepository.existsById(skillId)) {
            throw new RuntimeException("スキルが見つかりません: " + skillId);
        }
        employeeSkillRepository.deleteById(skillId);
    }

    /**
     * 従業員のスキル一覧を取得
     */
    @Transactional(readOnly = true)
    public List<EmployeeSkill> getEmployeeSkills(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("従業員が見つかりません: " + employeeId));

        return employeeSkillRepository.findByEmployeeOrderByProficiencyAndExperience(employee);
    }

    /**
     * 統計情報を取得
     */
    @Transactional(readOnly = true)
    public EmployeeStats getEmployeeStats() {
        long totalEmployees = employeeRepository.count();
        long activeEmployees = employeeRepository.countByStatus(Employee.EmployeeStatus.ACTIVE);
        long inactiveEmployees = employeeRepository.countByStatus(Employee.EmployeeStatus.INACTIVE);
        long terminatedEmployees = employeeRepository.countByStatus(Employee.EmployeeStatus.TERMINATED);

        return new EmployeeStats(totalEmployees, activeEmployees, inactiveEmployees, terminatedEmployees);
    }

    /**
     * 統計情報クラス
     */
    public static class EmployeeStats {
        private final long totalEmployees;
        private final long activeEmployees;
        private final long inactiveEmployees;
        private final long terminatedEmployees;

        public EmployeeStats(long totalEmployees, long activeEmployees, long inactiveEmployees, long terminatedEmployees) {
            this.totalEmployees = totalEmployees;
            this.activeEmployees = activeEmployees;
            this.inactiveEmployees = inactiveEmployees;
            this.terminatedEmployees = terminatedEmployees;
        }

        public long getTotalEmployees() { return totalEmployees; }
        public long getActiveEmployees() { return activeEmployees; }
        public long getInactiveEmployees() { return inactiveEmployees; }
        public long getTerminatedEmployees() { return terminatedEmployees; }
    }
}
