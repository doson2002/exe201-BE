package com.exe201.exe201be.services;

import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.responses.FoodItemReportResponse;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IReportService {

    List<FoodItemReportResponse> getTopSellingProducts(int limit);
    Double getTotalRevenue(Date startDate, Date endDate, Long supplierId);
    Long getTotalOrderCount(Date startDate, Date endDate,Long supplierId);

    Double getTotalRevenueByDate(Date date, Long supplierId);
    int getTotalOrderCountByDate(Date date,Long supplierId);

    Map<String, Object> getUserCountAndPercentageChangeByDate(Date date);

    Map<String, Object> getTotalProductSoldAndPercentageChangeByDate(Date date);
    Map<String, Object> getTotalOrderCountAndPercentageChangeByDate(Date date);

    Map<String, Object> getTotalRevenueAndPercentageChangeByDate(Date date);

    Map<String, Map<String, Double>> getTotalRevenueForAdminByDateRange(Date startDate, Date endDate);
}
