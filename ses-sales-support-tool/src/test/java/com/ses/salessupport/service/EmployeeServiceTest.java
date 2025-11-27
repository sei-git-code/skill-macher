package com.ses.salessupport.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ses.salessupport.entity.Employee;
import com.ses.salessupport.entity.Employee.EmployeeStatus;
import com.ses.salessupport.entity.Employee.EmploymentType;
import com.ses.salessupport.repository.EmployeeRepository;

/**
 * EmployeeServiceのユニットテスト
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("田中");
        employee.setLastName("太郎");
        employee.setEmail("tanaka@test.com");
        employee.setPhoneNumber("090-1234-5678");
        employee.setHourlyRate(new BigDecimal("8000"));
        employee.setEmploymentType(EmploymentType.FULL_TIME);
        employee.setStatus(EmployeeStatus.ACTIVE);
    }

    @Test
    void testGetAllEmployees_Success() {
        // Given
        List<Employee> expectedEmployees = Arrays.asList(employee);
        when(employeeRepository.findAll()).thenReturn(expectedEmployees);

        // When
        List<Employee> employees = employeeService.getAllEmployees();

        // Then
        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals(employee.getId(), employees.get(0).getId());
        verify(employeeRepository).findAll();
    }

    @Test
    void testGetEmployeeById_Success() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // When
        Optional<Employee> result = employeeService.getEmployeeById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(employee.getId(), result.get().getId());
        verify(employeeRepository).findById(1L);
    }

    @Test
    void testGetEmployeeById_NotFound() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Employee> result = employeeService.getEmployeeById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(employeeRepository).findById(1L);
    }

    @Test
    void testGetActiveEmployees_Success() {
        // Given
        List<Employee> expectedEmployees = Arrays.asList(employee);
        when(employeeRepository.findByStatus(EmployeeStatus.ACTIVE)).thenReturn(expectedEmployees);

        // When
        List<Employee> employees = employeeService.getActiveEmployees();

        // Then
        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals(EmployeeStatus.ACTIVE, employees.get(0).getStatus());
        verify(employeeRepository).findByStatus(EmployeeStatus.ACTIVE);
    }

    @Test
    void testCreateEmployee_Success() {
        // Given
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // When
        Employee result = employeeService.createEmployee(employee);

        // Then
        assertNotNull(result);
        assertEquals(employee.getId(), result.getId());
        verify(employeeRepository).save(employee);
    }

    @Test
    void testDeleteEmployee_Success() {
        // Given
        when(employeeRepository.existsById(1L)).thenReturn(true);

        // When
        employeeService.deleteEmployee(1L);

        // Then
        verify(employeeRepository).existsById(1L);
        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void testDeleteEmployee_NotFound() {
        // Given
        when(employeeRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            employeeService.deleteEmployee(1L);
        });
    }

    @Test
    void testUpdateEmployeeStatus_Success() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // When
        Employee result = employeeService.updateEmployeeStatus(1L, EmployeeStatus.INACTIVE);

        // Then
        assertNotNull(result);
        assertEquals(EmployeeStatus.INACTIVE, result.getStatus());
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(employee);
    }

    @Test
    void testUpdateEmployeeStatus_NotFound() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            employeeService.updateEmployeeStatus(1L, EmployeeStatus.INACTIVE);
        });
    }

    @Test
    void testUpdateEmployeeHourlyRate_Success() {
        // Given
        BigDecimal newRate = new BigDecimal("9000");
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // When
        Employee result = employeeService.updateEmployeeHourlyRate(1L, newRate);

        // Then
        assertNotNull(result);
        assertEquals(newRate, result.getHourlyRate());
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(employee);
    }

    @Test
    void testUpdateEmployeeHourlyRate_NotFound() {
        // Given
        BigDecimal newRate = new BigDecimal("9000");
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            employeeService.updateEmployeeHourlyRate(1L, newRate);
        });
    }

    @Test
    void testGetEmployeeStats_Success() {
        // Given
        when(employeeRepository.count()).thenReturn(10L);
        when(employeeRepository.countByStatus(EmployeeStatus.ACTIVE)).thenReturn(8L);
        when(employeeRepository.countByStatus(EmployeeStatus.INACTIVE)).thenReturn(2L);

        // When
        EmployeeService.EmployeeStats stats = employeeService.getEmployeeStats();

        // Then
        assertNotNull(stats);
        assertEquals(10L, stats.getTotalEmployees());
        assertEquals(8L, stats.getActiveEmployees());
        assertEquals(2L, stats.getInactiveEmployees());
        verify(employeeRepository).count();
        verify(employeeRepository).countByStatus(EmployeeStatus.ACTIVE);
        verify(employeeRepository).countByStatus(EmployeeStatus.INACTIVE);
    }
}
