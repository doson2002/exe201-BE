package com.exe201.exe201be.repositories;


import com.exe201.exe201be.entities.Token;
import com.exe201.exe201be.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByUser(Users user);
    Token findByToken(String token);
    Token findByRefreshToken(String token);
    List<Token> findByUser_Id(Long userId);

    void deleteByUser_Id(Long userId);
}

