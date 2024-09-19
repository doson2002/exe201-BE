package com.exe201.exe201be.controllers;


import com.exe201.exe201be.components.JwtTokenUtils;
import com.exe201.exe201be.dtos.ChangePasswordDTO;
import com.exe201.exe201be.dtos.UpdateUserDTO;
import com.exe201.exe201be.dtos.UserDTO;
import com.exe201.exe201be.dtos.UserLoginDTO;
import com.exe201.exe201be.entities.Token;
import com.exe201.exe201be.entities.Users;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.responses.LoginResponse;
import com.exe201.exe201be.responses.RegisterResponse;
import com.exe201.exe201be.responses.UserListResponse;
import com.exe201.exe201be.responses.UserResponse;
import com.exe201.exe201be.services.ITokenService;
import com.exe201.exe201be.services.IUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("http://www.dodakat.com.s3-website-ap-southeast-1.amazonaws.com")
@SecurityRequirement(name = "bearer-key")
@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final JwtTokenUtils jwtTokenUtils;
    private final IUserService userService;
    private final ITokenService tokenService;

    @GetMapping("/generate-secret-key")
    public ResponseEntity<?> generateSecretKey(){
        return ResponseEntity.ok(jwtTokenUtils.generateSecretKey());
    }

    private boolean isMobileDevice(String userAgent) {
        return userAgent.toLowerCase().contains("mobile");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO,
            HttpServletRequest request
    ) {
        try {
            String token = userService.login(
                    userLoginDTO.getEmail(),
                    userLoginDTO.getPassword()
            );
            String userAgent = request.getHeader("User-Agent");

            Users userDetail = userService.getUserDetailsFromToken(token);

            Token jwtToken = tokenService.addToken(userDetail, token,isMobileDevice(userAgent));

            return ResponseEntity.ok(LoginResponse.builder()
                    .message("Login successfully")
                    .token(jwtToken.getToken())
                    .tokenType(jwtToken.getTokenType())
                    .refreshToken(jwtToken.getRefreshToken())
                    .name(userDetail.getFullName())
                    .email(userDetail.getUsername())
                    .phone(userDetail.getPhoneNumber())
                    .gender(userDetail.getGender())
                    .imgUrl(userDetail.getImgUrl())
                    .firstLogin(userDetail.getFirstLogin())
                    .roles(userDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .id(userDetail.getId())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    LoginResponse.builder().message("FAIL").build()
            );
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO,
                                                BindingResult result) {
        RegisterResponse registerResponse = new RegisterResponse();
        List<String> errorMessages = null;
        if (result.hasErrors()) {
            errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            registerResponse.setMessage(errorMessages.toString());
            return ResponseEntity.badRequest().body(registerResponse);
        }
        try {

           Users user = userService.createUser(userDTO);
            registerResponse.setMessage("Đăng ký tài khoản thành công");
            registerResponse.setUser(user);
            return ResponseEntity.ok(registerResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @Transactional
    @PutMapping("/update_password/{id}")
    public ResponseEntity<?> updatePassword(@PathVariable long id, @RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
             userService.changePassword(id, changePasswordDTO);
            return ResponseEntity.ok("Update password successfully!!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/get_all_users")
        public ResponseEntity<UserListResponse> getUsers(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam("page") int page, @RequestParam("limit") int limit){
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<UserResponse> userPage = userService.getAllUsers(keyword, pageRequest);
        int totalPages = userPage.getTotalPages();
        List<UserResponse> users = userPage.getContent();
        return ResponseEntity.ok(UserListResponse.builder()
                .users(users)
                .totalPages(totalPages)
                .build());
    }
    @GetMapping("/get_user_by_id/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getUserById(@Valid @PathVariable Long id) throws DataNotFoundException {
        Users user =userService.getUser(id);
        return ResponseEntity.ok(UserResponse.fromUser(user));
    }

    @GetMapping("/get_user_by_role")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?>getUsersByRole(
            @RequestParam(defaultValue = "") Long roleId)
            throws DataNotFoundException {
        List<Users> users =userService.getUserByRole(roleId);
        return ResponseEntity.ok(users);
    }


    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody UpdateUserDTO updateUserDTO) {
        try {
            boolean emailUpdated = userService.updateUser(id, updateUserDTO);
            Map<String, Object> response = new HashMap<>();

            response.put("message", "Update User successfully");
            // Nếu email đã được cập nhật, trả về thông báo đặc biệt
            if (emailUpdated) {
                response.put("emailUpdated", true);  // Trạng thái email đã cập nhật
                response.put("emailUpdateMessage", "Bạn đã thay đổi email, vui lòng đăng nhập lại.");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
//    @DeleteMapping("/delete_user/{userId}")
//    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
//        userService.deleteUser(userId);
//        return new ResponseEntity<>("Users deleted successfully", HttpStatus.OK);
//    }
    @PutMapping("/block/{userId}/{active}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> blockOrEnable(
            @Valid @PathVariable long userId,
            @Valid @PathVariable int active
    ) {
        try {
            userService.blockOrEnable(userId, active > 0);
            String message = active > 0 ? "Successfully enabled the user." : "Successfully blocked the user.";
            return ResponseEntity.ok().body(message);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body("User not found.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
