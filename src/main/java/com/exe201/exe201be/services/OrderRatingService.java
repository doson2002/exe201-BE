package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.OrderRatingDTO;
import com.exe201.exe201be.entities.*;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.repositories.FoodOrderRepository;
import com.exe201.exe201be.repositories.OrderRatingRepository;
import com.exe201.exe201be.repositories.SupplierInfoRepository;
import com.exe201.exe201be.repositories.UserRepository;
import com.exe201.exe201be.responses.SupplierRatingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderRatingService implements IOrderRatingService{
    private final OrderRatingRepository orderRatingRepository;

    private final SupplierInfoRepository supplierInfoRepository;

    private final UserRepository userRepository;

    // Method to add a new rating and update the totalStarRating
    public OrderRating addRating(OrderRatingDTO orderRatingDTO) throws DataNotFoundException {
        SupplierInfo existingSupplier = supplierInfoRepository.findById(orderRatingDTO.getSupplierId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find Supplier with id: "+ orderRatingDTO.getSupplierId()));
        Users existingUser = userRepository.findById(orderRatingDTO.getUserId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find User with id: "+ orderRatingDTO.getUserId()));
        // Save the new rating
        OrderRating newOrderRating = new OrderRating();
        newOrderRating.setRatingStar(orderRatingDTO.getRatingStar());
        newOrderRating.setResponseMessage(orderRatingDTO.getResponseMessage());
        newOrderRating.setSupplierInfo(existingSupplier);
        newOrderRating.setUsers(existingUser);

        orderRatingRepository.save(newOrderRating);


        // Update the totalStarRating for the SupplierInfo associated with this order
        updateSupplierTotalStarRating(newOrderRating.getSupplierInfo().getId(), newOrderRating.getRatingStar());

        return newOrderRating;
    }

    private void updateSupplierTotalStarRating(Long supplierInfoId, int newRating) throws DataNotFoundException {
        // Fetch the supplier info
        SupplierInfo supplierInfo = supplierInfoRepository.findById(supplierInfoId)
                .orElseThrow(() -> new DataNotFoundException("SupplierInfo not found for this id :: " + supplierInfoId));

        // Calculate the new totalStarRating
        double currentTotalStarRating = supplierInfo.getTotalStarRating();
        int currentReviewCount = supplierInfo.getTotalReviewCount();

        double newTotalStarRating = (currentTotalStarRating * currentReviewCount + newRating) / (currentReviewCount + 1);
// Định dạng số mới với 1 chữ số thập phân
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String formattedRating = decimalFormat.format(newTotalStarRating);
        newTotalStarRating = Double.parseDouble(formattedRating);
        // Update supplier info
        supplierInfo.setTotalStarRating(newTotalStarRating);
        supplierInfo.setTotalReviewCount(currentReviewCount + 1);
        supplierInfoRepository.save(supplierInfo);
    }

    public List<OrderRating> getRatingBySupplierId(Long supplierInfoId) {
        return orderRatingRepository.findBySupplierInfo_Id(supplierInfoId);
    }

    public SupplierRatingResponse getSupplierRatings(Long supplierId) throws DataNotFoundException {
        // Lấy thông tin nhà cung cấp
        SupplierInfo supplierInfo = supplierInfoRepository.findById(supplierId)
                .orElseThrow(() -> new DataNotFoundException("Supplier not found"));

        // Lấy tổng số lượng đánh giá
        int totalReviews = supplierInfo.getTotalReviewCount();

        // Tính giá trị trung bình của đánh giá
        double averageRating = supplierInfo.getTotalStarRating();

        // Đếm số lượng đánh giá cho từng mức sao
        int oneStarCount = orderRatingRepository.countByRatingStarAndSupplierInfo(1, supplierInfo);
        int twoStarCount = orderRatingRepository.countByRatingStarAndSupplierInfo(2, supplierInfo);
        int threeStarCount = orderRatingRepository.countByRatingStarAndSupplierInfo(3, supplierInfo);
        int fourStarCount = orderRatingRepository.countByRatingStarAndSupplierInfo(4, supplierInfo);
        int fiveStarCount = orderRatingRepository.countByRatingStarAndSupplierInfo(5, supplierInfo);

        // Tạo và trả về đối tượng SupplierRatingResponse
        SupplierRatingResponse response = new SupplierRatingResponse();
        response.setTotalReviews(totalReviews);
        response.setAverageRating(averageRating);
        response.setOneStarCount(oneStarCount);
        response.setTwoStarCount(twoStarCount);
        response.setThreeStarCount(threeStarCount);
        response.setFourStarCount(fourStarCount);
        response.setFiveStarCount(fiveStarCount);

        return response;
    }
    public Map<Integer, List<String>> getAllResponseMessagesByStars(Long supplierId) {
        // Lấy tất cả các đánh giá của một supplier
        List<OrderRating> ratings = orderRatingRepository.findBySupplierInfoId(supplierId);

        // Phân loại các response_message theo ratingStar
        Map<Integer, List<String>> responseMessagesByStars = ratings.stream()
                .collect(Collectors.groupingBy(
                        OrderRating::getRatingStar,
                        Collectors.mapping(OrderRating::getResponseMessage, Collectors.toList())
                ));

        return responseMessagesByStars;
    }
}
