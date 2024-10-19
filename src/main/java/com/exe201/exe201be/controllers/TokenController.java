package com.exe201.exe201be.controllers;

import com.exe201.exe201be.entities.Token;
import com.exe201.exe201be.entities.Users;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.repositories.UserRepository;
import com.exe201.exe201be.services.ITokenService;
import com.exe201.exe201be.services.IUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("${api.prefix}/token")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class TokenController {
    private final ITokenService tokenService;
    private final IUserService userService;
    private final UserRepository   userRepository;
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) throws DataNotFoundException {
        String refreshToken = request.get("refreshToken");
        String email = request.get("email");

        Optional<Users> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new DataNotFoundException("User with email " + email + " not found");
        }
        Users user = optionalUser.get();
        try {
            Token token = tokenService.refreshToken(refreshToken, user);
            Map<String, String> response = new HashMap<>();
            response.put("token", token.getToken());
            response.put("refreshToken", token.getRefreshToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
