package com.hotelmanagement.service;

import com.hotelmanagement.dto.response.UserResponse;
import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Integer id);
    UserResponse blockUser(Integer id);
    UserResponse getCurrentUserProfile();
}
