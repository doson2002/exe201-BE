package com.exe201.exe201be.controllers;


import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.responses.FoodItemReportResponse;
import com.exe201.exe201be.responses.SupplierOrderPercentageResponse;
import com.exe201.exe201be.services.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/get_user_count_by_date")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<?> getUserCountByDate(@RequestParam String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false); // Thiết lập để không cho phép định dạng không chính xác
            Date dateSelected;
            if (date != null ) {
                dateSelected = sdf.parse(date);
            } else {
                throw new RuntimeException("At least one date parameter is required.");
            }
            // Gọi service để lấy dữ liệu user và phần trăm thay đổi
            Map<String, Object> result = reportService.getUserCountAndPercentageChangeByDate(dateSelected);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // Xử lý các lỗi khác
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while processing your request.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/get_product_sold_by_date")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<?> getProductSoldByDate(@RequestParam String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false); // Thiết lập để không cho phép định dạng không chính xác
            Date dateSelected;
            if (date != null ) {
                dateSelected = sdf.parse(date);
            } else {
                throw new RuntimeException("At least one date parameter is required.");
            }
            // Lấy tổng số lượng sản phẩm bán được và phần trăm thay đổi so với ngày hôm qua
            Map<String, Object> result = reportService.getTotalProductSoldAndPercentageChangeByDate(dateSelected);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // Xử lý khi có lỗi xảy ra
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Có lỗi xảy ra trong quá trình tính toán");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/get_order_count_by_date")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<?> getOrderCountByDate(@RequestParam String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false); // Thiết lập để không cho phép định dạng không chính xác
            Date dateSelected;
            if (date != null ) {
                dateSelected = sdf.parse(date);
            } else {
                throw new RuntimeException("At least one date parameter is required.");
            }
            // Lấy tổng số lượng đơn hàng và phần trăm thay đổi so với ngày hôm qua
            Map<String, Object> result = reportService.getTotalOrderCountAndPercentageChangeByDate(dateSelected);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // Xử lý khi có lỗi xảy ra
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Có lỗi xảy ra trong quá trình tính toán");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @GetMapping("/get_order_count_by_date_for_partner/{supplierId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<?> getOrderCountByDateForPartner(@PathVariable Long supplierId,
                                                           @RequestParam String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false); // Thiết lập để không cho phép định dạng không chính xác
            Date dateSelected;
            if (date != null ) {
                dateSelected = sdf.parse(date);
            } else {
                throw new RuntimeException("At least one date parameter is required.");
            }
            // Lấy tổng số lượng đơn hàng và phần trăm thay đổi so với ngày hôm qua
            Map<String, Object> result = reportService.getTotalOrderCountAndPercentageChangeByDateForPartner(supplierId,dateSelected);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // Xử lý khi có lỗi xảy ra
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Có lỗi xảy ra trong quá trình tính toán");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/get_revenue_by_date")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<?> getRevenueByDate(@RequestParam String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false); // Thiết lập để không cho phép định dạng không chính xác
            Date dateSelected;
            if (date != null ) {
                dateSelected = sdf.parse(date);
            } else {
                throw new RuntimeException("At least one date parameter is required.");
            }
            // Lấy tổng doanh thu và phần trăm thay đổi so với ngày hôm qua
            Map<String, Object> result = reportService.getTotalRevenueAndPercentageChangeByDate(dateSelected);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // Xử lý khi có lỗi xảy ra
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Có lỗi xảy ra trong quá trình tính toán");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @GetMapping("/get_revenue_by_date_for_partner/{supplierId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<?> getRevenueByDateForPartner(@PathVariable Long supplierId,
                                                        @RequestParam String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false); // Thiết lập để không cho phép định dạng không chính xác
            Date dateSelected;
            if (date != null ) {
                dateSelected = sdf.parse(date);
            } else {
                throw new RuntimeException("At least one date parameter is required.");
            }
            // Lấy tổng doanh thu và phần trăm thay đổi so với ngày hôm qua
            Map<String, Object> result = reportService.getTotalRevenueAndPercentageChangeByDateForPartner(supplierId,dateSelected);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // Xử lý khi có lỗi xảy ra
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Có lỗi xảy ra trong quá trình tính toán");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @GetMapping("/get_revenue_by_date_range_for_admin")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<?> getRevenueByDateRange(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false); // Thiết lập để không cho phép định dạng không chính xác
            Date start;
            Date end;

            // Kiểm tra và phân tích ngày bắt đầu và ngày kết thúc
            if (startDate != null) {
                start = sdf.parse(startDate);
            } else {
                throw new IllegalArgumentException("Start date is required.");
            }

            if (endDate != null) {
                end = sdf.parse(endDate);
            } else {
                throw new IllegalArgumentException("End date is required.");
            }

            // Kiểm tra xem ngày bắt đầu có trước ngày kết thúc không
            if (start.after(end)) {
                throw new IllegalArgumentException("Start date must be before or equal to end date.");
            }

            // Gọi hàm để lấy tổng doanh thu
            Map<String, Map<String, Double>> revenueMap = reportService.getTotalRevenueForAdminByDateRange(start, end);

            return ResponseEntity.ok(revenueMap);
        } catch (ParseException e) {
            // Xử lý lỗi định dạng ngày
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid date format. Please use yyyy-MM-dd.");
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (IllegalArgumentException e) {
            // Xử lý các lỗi liên quan đến tham số đầu vào
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            // Xử lý khi có lỗi khác xảy ra
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while calculating revenue.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/get_top_supplier_by_orders_for_admin")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<List<SupplierOrderPercentageResponse>> getSupplierOrderPercentage(
            @RequestParam(value = "limit", defaultValue = "5") int n) {
        List<SupplierOrderPercentageResponse> result = reportService.getOrderCountAndPercentageBySupplier(n);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/top_selling_food_item/{supplierInfoId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PARTNER', 'ROLE_CUSTOMER')")
    public ResponseEntity<?> getTopSellingProducts(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @PathVariable Long supplierInfoId,
            @RequestParam(defaultValue = "5") int limit) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false); // Thiết lập để không cho phép định dạng không chính xác
            Date start;
            Date end;

            // Kiểm tra và phân tích ngày bắt đầu và ngày kết thúc
            if (startDate != null) {
                start = sdf.parse(startDate);
            } else {
                throw new IllegalArgumentException("Start date is required.");
            }

            if (endDate != null) {
                end = sdf.parse(endDate);
            } else {
                throw new IllegalArgumentException("End date is required.");
            }

            // Kiểm tra xem ngày bắt đầu có trước ngày kết thúc không
            if (start.after(end)) {
                throw new IllegalArgumentException("Start date must be before or equal to end date.");
            }
            List<FoodItemReportResponse> topSellingProducts = reportService.getTopSellingProductsByDateRange(start, end, supplierInfoId, limit);

            // Trả về JSON với status thành công
            return ResponseEntity.ok(topSellingProducts);
        } catch (Exception e) {
            // Trả về lỗi nếu có
            Map<String, String> response = new HashMap<>();
            response.put("status", "failed");
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}
