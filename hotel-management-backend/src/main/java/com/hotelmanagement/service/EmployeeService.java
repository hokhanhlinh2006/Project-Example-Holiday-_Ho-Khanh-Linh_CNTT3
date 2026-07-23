package com.hotelmanagement.service;

import com.hotelmanagement.dto.request.EmployeeRequest;
import com.hotelmanagement.dto.response.EmployeeResponse;
import java.util.List;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest request);
    EmployeeResponse updateEmployee(Integer id, EmployeeRequest request);
    EmployeeResponse getEmployeeById(Integer id);
    List<EmployeeResponse> getAllEmployees();
    void deleteEmployee(Integer id);
}
