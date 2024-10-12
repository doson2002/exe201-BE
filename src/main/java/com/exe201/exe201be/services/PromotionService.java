package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.PromotionsDTO;
import com.exe201.exe201be.entities.FoodOrder;
import com.exe201.exe201be.entities.FoodOrderPromotion;
import com.exe201.exe201be.entities.Promotions;
import com.exe201.exe201be.entities.SupplierInfo;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.repositories.FoodOrderPromotionRepository;
import com.exe201.exe201be.repositories.FoodOrderRepository;
import com.exe201.exe201be.repositories.PromotionsRepository;
import com.exe201.exe201be.repositories.SupplierInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;


@Service
@RequiredArgsConstructor
public class PromotionService implements IPromotionService {
    private final PromotionsRepository promotionsRepository;
    private final SupplierInfoRepository supplierInfoRepository;
    private final FoodOrderRepository foodOrderRepository;
    private final FoodOrderPromotionRepository foodOrderPromotionRepository;



    @Override
    public Promotions createPromotion(PromotionsDTO promotionsDTO) throws DataNotFoundException {
        String code = generateRandomCode(8);
        SupplierInfo supplierInfo = supplierInfoRepository.findById(promotionsDTO.getSupplierId())
                .orElseThrow(() -> new DataNotFoundException("Supplier not found"));
        Promotions promotions = Promotions.builder()
                .code(code)
                .discountPercentage(promotionsDTO.getDiscountPercentage())
                .fixedDiscountAmount(promotionsDTO.getFixedDiscountAmount())
                .description(promotionsDTO.getDescription())
                .status(promotionsDTO.isStatus())
                .supplierInfo(supplierInfo)
                .build();
        return promotionsRepository.save(promotions);
    }

    public Page<Promotions> getAllPromotions(int page, int size, Long supplierInfoId, String code) {
        Pageable pageable = PageRequest.of(page, size);
        return promotionsRepository.searchPromotions(supplierInfoId, code, pageable);
    }

    public double applyPromotion(Long foodOrderId, Long promotionId, double totalPrice) throws DataNotFoundException {
        Promotions promotion = promotionsRepository.findById(promotionId)
                .orElseThrow(()->new DataNotFoundException("promotion not found"));
        FoodOrder foodOrder = foodOrderRepository.findById(foodOrderId)
                .orElseThrow(()->new DataNotFoundException("food order not found"));
        FoodOrderPromotion foodOrderPromotion = new FoodOrderPromotion();
        foodOrderPromotion.setPromotion(promotion);
        foodOrderPromotion.setFoodOrder(foodOrder);
        foodOrderPromotionRepository.save(foodOrderPromotion);
        double newTotalPrice = totalPrice - (promotion.getDiscountPercentage()*totalPrice + promotion.getFixedDiscountAmount());

        return newTotalPrice;
    }
    private String generateRandomCode(int length) {
        SecureRandom random = new SecureRandom();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code =  new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }

}
