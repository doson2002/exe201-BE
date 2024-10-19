package com.exe201.exe201be.services;

import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.entities.FoodOrder;
import com.exe201.exe201be.entities.Users;
import com.exe201.exe201be.repositories.FoodItemRepository;
import com.exe201.exe201be.repositories.FoodOrderRepository;
import com.exe201.exe201be.repositories.UserRepository;
import com.exe201.exe201be.responses.FoodItemReportResponse;
import com.exe201.exe201be.responses.SupplierOrderPercentageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {

    private final FoodItemRepository foodItemRepository;
    private final FoodOrderRepository foodOrderRepository;
    private final UserRepository userRepository;

    public List<FoodItemReportResponse> getTopSellingProducts(int limit) {
        // Gọi phương thức findTopSellingProducts từ repository để lấy danh sách sản phẩm bán chạy
        List<FoodItem> foodItems = foodItemRepository.findTopSellingProducts(limit);

        // Chuyển đổi danh sách FoodItem thành danh sách FoodItemReportResponse
        return foodItems.stream()
                .map(FoodItemReportResponse::fromFoodItem)
                .collect(Collectors.toList());
    }
    public Map<String, Map<String, Double>> getTotalRevenueForAdminByDateRange(Date startDate, Date endDate) {
        // Tạo các Map để lưu trữ doanh thu theo phương thức thanh toán
        Map<String, Double> onlineRevenueMap = new TreeMap<>();
        Map<String, Double> offlineRevenueMap = new TreeMap<>();

        // Tạo một Calendar để lặp qua từng ngày trong khoảng thời gian
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        while (!calendar.getTime().after(endDate)) {
            Date currentDate = calendar.getTime();
            String currentDateString = sdf.format(currentDate); // Định dạng ngày thành chuỗi

            // Tính tổng doanh thu cho ngày hiện tại theo phương thức thanh toán
            double onlineTotalRevenue = foodOrderRepository.findByOrderTime(currentDate).stream()
                    .filter(order -> "transfer".equals(order.getPaymentMethod()) && order.getPaymentStatus() == 1)
                    .mapToDouble(FoodOrder::getTotalPrice)
                    .sum();

            double offlineTotalRevenue = foodOrderRepository.findByOrderTime(currentDate).stream()
                    .filter(order -> "cash".equals(order.getPaymentMethod()) && order.getPaymentStatus() == 1)
                    .mapToDouble(FoodOrder::getTotalPrice)
                    .sum();

            // Lưu trữ tổng doanh thu vào Map
            onlineRevenueMap.put(currentDateString, onlineTotalRevenue);
            offlineRevenueMap.put(currentDateString, offlineTotalRevenue);

            // Chuyển đến ngày tiếp theo
            calendar.add(Calendar.DATE, 1);
        }

        // Tạo một Map tổng hợp để trả về kết quả
        Map<String, Map<String, Double>> result = new HashMap<>();
        result.put("online", onlineRevenueMap);
        result.put("offline", offlineRevenueMap);

        return result;
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
        // Tìm tất cả đơn hàng trong thời gian date
        List<FoodOrder> orders = foodOrderRepository.findByOrderTimeAndSupplierInfo_Id(date, supplierId);
        return orders.stream().mapToDouble(FoodOrder::getTotalPrice).sum();
    }
    public int getTotalOrderCountByDate(Date date,Long supplierId) {
        // Tính số lượng đơn hàng trong khoảng thời gian từ startDate đến endDate
        List<FoodOrder> orders = new ArrayList<>();

        orders = foodOrderRepository.findByOrderTimeAndSupplierInfo_Id(date, supplierId);

        return  orders.size();
    }
    public Map<String, Object> getTotalOrderCountAndPercentageChangeByDateForPartner(Long supplierId, Date date) {
        Map<String, Object> result = new HashMap<>();
        // Tính tổng số lượng đơn hàng của ngày hiện tại
        int todayOrderCount = getTotalOrderCountByDate(date, supplierId);

        // Lấy ngày hôm qua
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();

        // Tính tổng số lượng đơn hàng của ngày hôm qua
        int yesterdayOrderCount = getTotalOrderCountByDate(yesterday, supplierId);

        // Tính phần trăm thay đổi
        double percentageChange;
        if (yesterdayOrderCount == 0) {
            percentageChange = 100.0;  // Giả sử là tăng trưởng 100% nếu ngày hôm qua không có đơn hàng nào
        } else {
            percentageChange = ((double) (todayOrderCount - yesterdayOrderCount) / yesterdayOrderCount) * 100;
        }

        // Thêm dữ liệu vào kết quả trả về
        result.put("todayOrderCount", todayOrderCount);
        result.put("percentageChange", percentageChange);

        return result;
    }
    public Map<String, Object> getTotalRevenueAndPercentageChangeByDateForPartner(Long supplierId, Date date) {
        Map<String, Object> result = new HashMap<>();

        // Tính tổng doanh thu của ngày hiện tại
        double todayRevenue = getTotalRevenueByDate(date, supplierId);

        // Lấy ngày hôm qua
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();

        // Tính tổng doanh thu của ngày hôm qua
        double yesterdayRevenue = getTotalRevenueByDate(yesterday, supplierId);

        // Tính phần trăm thay đổi
        double percentageChange;
        if (yesterdayRevenue == 0) {
            percentageChange = 100.0;  // Giả sử là tăng trưởng 100% nếu ngày hôm qua không có doanh thu nào
        } else {
            percentageChange = ((todayRevenue - yesterdayRevenue) / yesterdayRevenue) * 100;
        }

        // Thêm dữ liệu vào kết quả trả về
        result.put("todayRevenue", todayRevenue);
        result.put("percentageChange", percentageChange);

        return result;
    }


    public Map<String, Object> getTotalRevenueAndPercentageChangeByDate(Date date) {
        Map<String, Object> result = new HashMap<>();

        // Tính tổng doanh thu của ngày hiện tại
        double todayRevenue = getTotalRevenueByDateForAdmin(date);

        // Lấy ngày hôm qua
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();

        // Tính tổng doanh thu của ngày hôm qua
        double yesterdayRevenue = getTotalRevenueByDateForAdmin(yesterday);

        // Tính phần trăm thay đổi
        double percentageChange;
        if (yesterdayRevenue == 0) {
            percentageChange = 100.0;  // Giả sử là tăng trưởng 100% nếu ngày hôm qua không có doanh thu nào
        } else {
            percentageChange = ((todayRevenue - yesterdayRevenue) / yesterdayRevenue) * 100;
        }

        // Thêm dữ liệu vào kết quả trả về
        result.put("todayRevenue", todayRevenue);
        result.put("percentageChange", percentageChange);

        return result;
    }

    public Double getTotalRevenueByDateForAdmin(Date date) {
        // Tìm tất cả đơn hàng trong thời gian date
        List<FoodOrder> orders = foodOrderRepository.findByOrderTime(date);
        return orders.stream().mapToDouble(FoodOrder::getTotalPrice).sum();
    }

    public Map<String, Object> getTotalOrderCountAndPercentageChangeByDate(Date date) {
        Map<String, Object> result = new HashMap<>();

        // Tính tổng số lượng đơn hàng của ngày hiện tại
        int todayOrderCount = getTotalOrderCountByDateForAdmin(date);

        // Lấy ngày hôm qua
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();

        // Tính tổng số lượng đơn hàng của ngày hôm qua
        int yesterdayOrderCount = getTotalOrderCountByDateForAdmin(yesterday);

        // Tính phần trăm thay đổi
        double percentageChange;
        if (yesterdayOrderCount == 0) {
            percentageChange = 100.0;  // Giả sử là tăng trưởng 100% nếu ngày hôm qua không có đơn hàng nào
        } else {
            percentageChange = ((double) (todayOrderCount - yesterdayOrderCount) / yesterdayOrderCount) * 100;
        }

        // Thêm dữ liệu vào kết quả trả về
        result.put("todayOrderCount", todayOrderCount);
        result.put("percentageChange", percentageChange);

        return result;
    }

    public int getTotalOrderCountByDateForAdmin(Date date) {
        // Tính số lượng đơn hàng trong khoảng thời gian từ startDate đến endDate
        List<FoodOrder> orders = new ArrayList<>();
        orders = foodOrderRepository.findByOrderTime(date);
        return  orders.size();
    }




    private int getTotalProductSoldByDateForAdmin(Date date) {
        // Tìm tất cả đơn hàng trong thời gian date
        List<FoodOrder> orders = foodOrderRepository.findByOrderTime(date);
        return orders.stream().mapToInt(FoodOrder::getTotalItems).sum();
    }
    public Map<String, Object> getTotalProductSoldAndPercentageChangeByDate(Date date) {
        Map<String, Object> result = new HashMap<>();

        // Tính tổng số lượng sản phẩm bán được của ngày hiện tại
        int todayProductCount = getTotalProductSoldByDateForAdmin(date);

        // Lấy ngày hôm qua
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();

        // Tính tổng số lượng sản phẩm bán được của ngày hôm qua
        int yesterdayProductCount = getTotalProductSoldByDateForAdmin(yesterday);

        // Tính phần trăm thay đổi
        double percentageChange;
        if (yesterdayProductCount == 0) {
            percentageChange = 100.0;  // Giả sử là tăng trưởng 100% nếu ngày hôm qua không bán được sản phẩm nào
        } else {
            percentageChange = ((double) (todayProductCount - yesterdayProductCount) / yesterdayProductCount) * 100;
        }

        result.put("todayProductCount", todayProductCount);
        result.put("percentageChange", percentageChange);

        return result;
    }



    public Map<String, Object> getUserCountAndPercentageChangeByDate(Date date) {
        Map<String, Object> resultMap = new HashMap<>();

        // Lấy tổng số lượng user của ngày hiện tại
        List<Users> users = userRepository.findByCreatedDate(date);
        int currentDayCount = users.size();

        // Tính toán ngày hôm trước
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date previousDate = calendar.getTime();

        // Lấy tổng số lượng user của ngày hôm trước
        List<Users> previousDayUsers = userRepository.findByCreatedDate(previousDate);
        int previousDayCount = previousDayUsers.size();

        // Tính phần trăm tăng trưởng so với ngày hôm trước
        double percentageChange = 0;
        if (previousDayCount > 0) {
            percentageChange = ((double) (currentDayCount - previousDayCount) / previousDayCount) * 100;
        }else {
            percentageChange = 100;
        }

        // Đưa dữ liệu vào Map kết quả
        resultMap.put("totalUserCount", currentDayCount);
        resultMap.put("percentageChange", percentageChange);

        return resultMap;
    }

    public List<SupplierOrderPercentageResponse> getOrderCountAndPercentageBySupplier(int n) {
        List<Object[]> result = foodOrderRepository.countOrdersBySupplier();
        Long totalOrders = foodOrderRepository.countTotalOrders();

        List<SupplierOrderPercentageResponse> supplierOrderList = new ArrayList<>();
        for (Object[] row : result) {
            Long supplierId = (Long) row[0];
            Long orderCount = (Long) row[1];
            String supplierName = (String) row[2];  // Lấy tên supplier
            String imgUrl = (String) row[3];        // Lấy imgUrl của supplier
            double percentage = (double) orderCount / totalOrders * 100;


            // Làm tròn percentage và chuyển sang kiểu int để loại bỏ phần thập phân
            int roundedPercentage = (int) Math.round(percentage);

            supplierOrderList.add(new SupplierOrderPercentageResponse(supplierId, orderCount, roundedPercentage,supplierName, imgUrl));
        }

        // Sắp xếp theo số lượng đơn hàng giảm dần
        supplierOrderList.sort((o1, o2) -> o2.getOrderCount().compareTo(o1.getOrderCount()));

        // Giới hạn kết quả theo số lượng supplier n muốn hiển thị
        return supplierOrderList.stream().limit(n).collect(Collectors.toList());
    }

    public List<FoodItemReportResponse> getTopSellingProductsByDateRange(Date startDate, Date endDate, Long supplierInfoId, int limit) {
        List<Object[]> results = foodOrderRepository.findTopSellingFoodItems(startDate, endDate, supplierInfoId);

        // Giới hạn kết quả theo tham số limit
        List<FoodItemReportResponse> topSellingProducts = new ArrayList<>();
        for (int i = 0; i < Math.min(results.size(), limit); i++) {
            Object[] result = results.get(i);
            Long foodId = (Long) result[0];
            String foodName = (String) result[1];
            String imgUrl = (String) result[2]; // Lấy imgUrl từ kết quả
            int quantitySold = ((Number) result[3]).intValue(); // Chuyển đổi thành int

            // Tạo FoodItemReportResponse
            FoodItemReportResponse foodItemReportResponse = FoodItemReportResponse.builder()
                    .id(foodId)
                    .foodName(foodName)
                    .imageUrl(imgUrl)
                    .quantitySold(quantitySold)
                    .build();

            topSellingProducts.add(foodItemReportResponse);
        }

        return topSellingProducts;
    }
}
