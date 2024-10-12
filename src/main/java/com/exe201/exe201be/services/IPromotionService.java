package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.PromotionsDTO;
import com.exe201.exe201be.entities.Promotions;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import org.springframework.data.domain.Page;

public interface IPromotionService {
    Promotions createPromotion(PromotionsDTO promotionsDTO) throws DataNotFoundException;
    Page<Promotions> getAllPromotions(int page, int size, Long supplierInfoId, String code);
}
