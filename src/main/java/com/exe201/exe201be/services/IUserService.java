package com.exe201.exe201be.services;

import com.example.swp.dtos.ChangePasswordDTO;
import com.example.swp.dtos.UserDTO;
import com.example.swp.entities.Users;
import com.example.swp.exceptions.DataNotFoundException;
import com.example.swp.responses.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IUserService {
    Users createUser(UserDTO userDTO) throws Exception;

    Page<UserResponse> getAllUsers(String keyword, PageRequest pageRequest);

    Users getUser(Long id) throws DataNotFoundException;

    List<Users> getUserByRoleAndCounter(Long roleId, Long counterId) throws DataNotFoundException;

    void deleteUser(Long userId);

    String login(String userAccount, String password,Long roleId) throws Exception;

    Boolean create(String name, String email, String password);

    Users changePassword (Long id, ChangePasswordDTO changePasswordDTO) throws DataNotFoundException;
    String generateRandomPassword(int minLen, int maxLen);

    Users getUserDetailsFromToken(String token) throws Exception;
    void blockOrEnable(Long userId, Boolean active) throws DataNotFoundException;

    Users updateUser(long id, UserDTO userDTO) throws Exception;

    void updatePassword(String email, String password) throws DataNotFoundException;
}
