package com.exe201.exe201be.controllers;


import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.responses.FoodItemReportResponse;
import com.exe201.exe201be.services.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/report")
@RequiredArgsConstructor
public class ReportController {
    private final IReportService reportService;

    @GetMapping("/report_for_partner_by_range_date")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public Map<String, Object> getReportByRangeDate(@RequestParam(required = false) String startDate,
                                                   @RequestParam(required = false) String endDate,
                                                   @RequestParam(required = true) Long supplierInfoId) {
        Map<String, Object> report = new HashMap<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false); // Thiết lập để không cho phép định dạng không chính xác
            Date start;
            Date end;
            if (startDate != null && endDate != null) {
                start = sdf.parse(startDate);
                end = sdf.parse(endDate);
            } else {
                throw new RuntimeException("At least one date parameter is required.");
            }

            Double totalRevenue = reportService.getTotalRevenue(start, end, supplierInfoId);
            List<FoodItemReportResponse> topSellingProducts = reportService.getTopSellingProducts(5);
            Long totalOrderCount = reportService.getTotalOrderCount(start, end, supplierInfoId);

            report.put("totalOrderCount", totalOrderCount);
            report.put("totalRevenue", totalRevenue);
            report.put("topSellingProducts", topSellingProducts);
            report.put("supplierInfoId", supplierInfoId);
            return report;

        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format. Please use 'yyyy-MM-dd'.");
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while processing the request.");
        }
    }

    @GetMapping("/report_for_partner_by_date")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public Map<String, Object> getReportByDate(@RequestParam(required = false) String date,
                                               @RequestParam(required = true) Long supplierInfoId) {
        Map<String, Object> report = new HashMap<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false); // Thiết lập để không cho phép định dạng không chính xác
            Date dateSelected;
            if (date != null ) {
                dateSelected = sdf.parse(date);
            } else {
                throw new RuntimeException("At least one date parameter is required.");
            }

            Double totalRevenue = reportService.getTotalRevenueByDate(dateSelected, supplierInfoId);
            List<FoodItemReportResponse> topSellingProducts = reportService.getTopSellingProducts(5);
            int totalOrderCount = reportService.getTotalOrderCountByDate(dateSelected, supplierInfoId);
            report.put("totalOrderCount", totalOrderCount);
            report.put("totalRevenue", totalRevenue);
            report.put("topSellingProducts", topSellingProducts);
            report.put("supplierInfoId", supplierInfoId);
            return report;

        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format. Please use 'yyyy-MM-dd'.");
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while processing the request.");
        }
    }

}
