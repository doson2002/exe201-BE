package com.exe201.exe201be.services;


import com.exe201.exe201be.entities.Token;
import com.exe201.exe201be.entities.Users;
import org.springframework.stereotype.Service;

@Service

public interface ITokenService {
    Token addToken(Users user, String token, boolean isMobileDevice);
    Token refreshToken(String refreshToken, Users user) throws Exception;

    void deleteToken(String token);

    void deleteAllTokensForUser(Long userId) ;

}
