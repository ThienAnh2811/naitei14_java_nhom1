package org.example.foodanddrinkproject.service;

import org.example.foodanddrinkproject.dto.ChangePasswordRequest;
import org.example.foodanddrinkproject.dto.UpdateProfileRequest;
import org.example.foodanddrinkproject.dto.UserProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface    UserService {
    UserProfileDto getUserProfile(Long userId);
    UserProfileDto updateUserProfile(Long userId, UpdateProfileRequest request);
    void changePassword(Long userId, ChangePasswordRequest request);

    Page<UserProfileDto> getAllUsers(Pageable pageable);
    void banUser(Long userId, boolean isEnabled);
    void updateUser(Long userId, org.example.foodanddrinkproject.dto.AdminUpdateUserRequest request);
    org.example.foodanddrinkproject.dto.UserProfileDto getUserById(Long id);
    java.util.List<org.example.foodanddrinkproject.entity.Role> getAllRoles();
}
