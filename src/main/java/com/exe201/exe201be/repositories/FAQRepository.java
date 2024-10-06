package com.exe201.exe201be.repositories;

import com.exe201.exe201be.entities.Delivery;
import com.exe201.exe201be.entities.FAQ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FAQRepository extends JpaRepository<FAQ, Long> {
    // Sử dụng @Query để tìm kiếm theo title hoặc article
    @Query("SELECT f FROM FAQ f WHERE LOWER(f.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(f.article) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<FAQ> searchByTitleOrArticle(@Param("search") String search);
}