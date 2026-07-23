package com.hotelmanagement.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Integer userId;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String cccdPassport;
    private Integer loyaltyPoints;
    private String roleName;
    private LocalDateTime createdAt;
}
