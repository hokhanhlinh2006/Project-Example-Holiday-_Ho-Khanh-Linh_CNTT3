package com.hotelmanagement.service.impl;

import com.hotelmanagement.dto.request.EmployeeRequest;
import com.hotelmanagement.dto.response.EmployeeResponse;
import com.hotelmanagement.entity.Employee;
import com.hotelmanagement.entity.Role;
import com.hotelmanagement.exception.BadRequestException;
import com.hotelmanagement.exception.ResourceNotFoundException;
import com.hotelmanagement.repository.EmployeeRepository;
import com.hotelmanagement.repository.RoleRepository;
import com.hotelmanagement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private EmployeeResponse mapToResponse(Employee emp) {
        EmployeeResponse response = new EmployeeResponse();
        response.setEmployeeId(emp.getEmployeeId());
        response.setEmployeeCode(emp.getEmployeeCode());
        response.setFullName(emp.getFullName());
        response.setPosition(emp.getPosition());
        response.setEmail(emp.getEmail());
        response.setPhoneNumber(emp.getPhoneNumber());
        response.setSalaryRate(emp.getSalaryRate());
        response.setRoleName(emp.getRole().getRoleName());
        return response;
    }

    @Override
    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        if (employeeRepository.findByEmployeeCode(request.getEmployeeCode()).isPresent()) {
            throw new BadRequestException("Employee Code already exists");
        }
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + request.getRoleId()));

        Employee emp = Employee.builder()
                .employeeCode(request.getEmployeeCode())
                .fullName(request.getFullName())
                .position(request.getPosition())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .salaryRate(request.getSalaryRate())
                .role(role)
                .build();

        employeeRepository.save(emp);
        return mapToResponse(emp);
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(Integer id, EmployeeRequest request) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + request.getRoleId()));

        emp.setFullName(request.getFullName());
        emp.setPosition(request.getPosition());
        emp.setEmail(request.getEmail());
        emp.setPhoneNumber(request.getPhoneNumber());
        emp.setSalaryRate(request.getSalaryRate());
        emp.setRole(role);
        
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            emp.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        employeeRepository.save(emp);
        return mapToResponse(emp);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Integer id) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        return mapToResponse(emp);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteEmployee(Integer id) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        employeeRepository.delete(emp);
    }
}
