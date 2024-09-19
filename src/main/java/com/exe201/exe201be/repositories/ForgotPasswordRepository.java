package com.exe201.exe201be.repositories;


import com.exe201.exe201be.entities.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Long> {

    @Query("SELECT fp FROM ForgotPassword fp " +
            "WHERE fp.user.email = :email " +
            "AND fp.verify = false " +
            "AND fp.expirationTime = (SELECT MAX(fp2.expirationTime) FROM ForgotPassword fp2 WHERE fp2.user.email = :email AND fp2.verify = false)")
    Optional<ForgotPassword> findLatestOtpSent(@Param("email") String email);
}
