package com.hotelmanagement.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class EmployeeResponse {
    private Integer employeeId;
    private String employeeCode;
    private String fullName;
    private String position;
    private String email;
    private String phoneNumber;
    private BigDecimal salaryRate;
    private String roleName;
}
