package com.ses.salessupport.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ses.salessupport.entity.Employee;
import com.ses.salessupport.entity.User;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeId(String employeeId);

    Optional<Employee> findByEmail(String email);

    List<Employee> findByStatus(Employee.EmployeeStatus status);

    List<Employee> findByEmploymentType(Employee.EmploymentType employmentType);

    List<Employee> findByCreatedBy(User createdBy);

    List<Employee> findByStatusAndEmploymentType(Employee.EmployeeStatus status, Employee.EmploymentType employmentType);

    @Query("SELECT e FROM Employee e WHERE e.hourlyRate BETWEEN :minRate AND :maxRate")
    List<Employee> findByHourlyRateRange(@Param("minRate") BigDecimal minRate, @Param("maxRate") BigDecimal maxRate);

    @Query("SELECT e FROM Employee e WHERE e.hireDate >= :hireDate")
    List<Employee> findByHireDateAfter(@Param("hireDate") LocalDate hireDate);

    @Query("SELECT e FROM Employee e WHERE e.firstName LIKE %:keyword% OR e.lastName LIKE %:keyword% OR e.email LIKE %:keyword%")
    List<Employee> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT e FROM Employee e WHERE e.city = :city")
    List<Employee> findByCity(@Param("city") String city);

    @Query("SELECT e FROM Employee e WHERE e.country = :country")
    List<Employee> findByCountry(@Param("country") String country);

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.status = :status")
    Long countByStatus(@Param("status") Employee.EmployeeStatus status);

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.createdBy = :user")
    Long countByCreatedBy(@Param("user") User user);

    @Query("SELECT e FROM Employee e ORDER BY e.createdAt DESC")
    List<Employee> findAllOrderByCreatedAtDesc();

    @Query("SELECT e FROM Employee e WHERE e.status = 'ACTIVE' ORDER BY e.hireDate DESC")
    List<Employee> findActiveEmployeesOrderByHireDate();

    boolean existsByEmployeeId(String employeeId);

    boolean existsByEmail(String email);
}
