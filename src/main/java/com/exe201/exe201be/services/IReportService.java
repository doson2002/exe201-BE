package com.exe201.exe201be.services;

import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.responses.FoodItemReportResponse;

import java.util.Date;
import java.util.List;

public interface IReportService {

    List<FoodItemReportResponse> getTopSellingProducts(int limit);
    Double getTotalRevenue(Date startDate, Date endDate, Long supplierId);
    Long getTotalOrderCount(Date startDate, Date endDate,Long supplierId);

    Double getTotalRevenueByDate(Date date, Long supplierId);
    int getTotalOrderCountByDate(Date date,Long supplierId);
}
