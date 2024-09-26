package com.exe201.exe201be.services;

import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.entities.FoodOrder;
import com.exe201.exe201be.repositories.FoodItemRepository;
import com.exe201.exe201be.repositories.FoodOrderRepository;
import com.exe201.exe201be.responses.FoodItemReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {

    private final FoodItemRepository foodItemRepository;
    private final FoodOrderRepository foodOrderRepository;

    public List<FoodItemReportResponse> getTopSellingProducts(int limit) {
        // Gọi phương thức findTopSellingProducts từ repository để lấy danh sách sản phẩm bán chạy
        List<FoodItem> foodItems = foodItemRepository.findTopSellingProducts(limit);

        // Chuyển đổi danh sách FoodItem thành danh sách FoodItemReportResponse
        return foodItems.stream()
                .map(FoodItemReportResponse::fromFoodItem)
                .collect(Collectors.toList());
    }

    public Double getTotalRevenue(Date startDate, Date endDate, Long supplierId) {
        // Tìm tất cả đơn hàng trong khoảng thời gian từ startDate đến endDate
        List<FoodOrder> orders = foodOrderRepository.findByOrderTimeBetweenAndSupplierInfo_Id(startDate, endDate, supplierId);
        return orders.stream().mapToDouble(FoodOrder::getTotalPrice).sum();
    }
    public Long getTotalOrderCount(Date startDate, Date endDate,Long supplierId) {
        // Tính số lượng đơn hàng trong khoảng thời gian từ startDate đến endDate
        List<FoodOrder> orders = new ArrayList<>();
        if (startDate != null && endDate != null) {
            orders = foodOrderRepository.findByOrderTimeBetweenAndSupplierInfo_Id(startDate, endDate, supplierId);
        }
        return (long) orders.size();
    }

    public Double getTotalRevenueByDate(Date date, Long supplierId) {
        // Tìm tất cả đơn hàng trong khoảng thời gian từ startDate đến endDate
        List<FoodOrder> orders = foodOrderRepository.findByOrderTimeAndSupplierInfo_Id(date, supplierId);
        return orders.stream().mapToDouble(FoodOrder::getTotalPrice).sum();
    }
    public int getTotalOrderCountByDate(Date date,Long supplierId) {
        // Tính số lượng đơn hàng trong khoảng thời gian từ startDate đến endDate
        List<FoodOrder> orders = new ArrayList<>();

        orders = foodOrderRepository.findByOrderTimeAndSupplierInfo_Id(date, supplierId);

        return  orders.size();
    }


}
