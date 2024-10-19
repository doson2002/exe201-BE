package com.exe201.exe201be.repositories;


import com.exe201.exe201be.entities.FoodOrder;
import com.exe201.exe201be.entities.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    boolean existsByEmail(String email);

    @Query("SELECT u FROM Users u WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR u.fullName ILIKE %:keyword%) " +
            "ORDER BY u.fullName ASC")
    Page<Users> searchUsers(@Param("keyword") String keyword, Pageable pageable);

    Optional<Users> findByEmail(String email);

    Users findUserById(long id);

    @Query("SELECT u FROM Users u WHERE u.role.id = :roleId ")
    List<Users> findByRoleId(@Param("roleId") Long roleId);

    @Query("SELECT u FROM Users u WHERE u.role.id = :roleId " +
            "AND (:keyword IS NULL OR :keyword = '' OR u.phoneNumber LIKE %:keyword% " +
            "OR u.email LIKE %:keyword% OR u.fullName LIKE %:keyword%)")
    Page<Users> findByRoleIdAndKeyword(@Param("roleId") Long roleId, @Param("keyword") String keyword, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Users u set u.password = ?2 where u.email = ?1")
    void updatePassword(String email, String password);


    @Query("SELECT u FROM Users u WHERE DATE(u.createdDate) = :createdDate")
    List<Users> findByCreatedDate(@Param("createdDate") Date createdDate);





}
