package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.ChangePasswordDTO;
import com.exe201.exe201be.dtos.UpdateUserDTO;
import com.exe201.exe201be.dtos.UserDTO;
import com.exe201.exe201be.entities.Users;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.responses.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IUserService {
    Users createUser(UserDTO userDTO) throws Exception;

    Page<UserResponse> getAllUsers(String keyword, PageRequest pageRequest);

    Users getUser(Long id) throws DataNotFoundException;

    List<Users> getUserByRole(Long roleId) throws DataNotFoundException;

//    void deleteUser(Long userId);

    String login(String userAccount, String password) throws Exception;

    Boolean create(String name, String email, String password);

    Users changePassword (Long id, ChangePasswordDTO changePasswordDTO) throws DataNotFoundException;
    String generateRandomPassword(int minLen, int maxLen);

    Users getUserDetailsFromToken(String token) throws Exception;
    void blockOrEnable(Long userId, Boolean active) throws DataNotFoundException;

    boolean updateUser(long id, UpdateUserDTO updateUserDTO) throws Exception;

    void updatePassword(String email, String password) throws DataNotFoundException;
}
