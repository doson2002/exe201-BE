package com.exe201.exe201be.controllers;

import com.exe201.exe201be.dtos.OrderRequestDetailDTO;
import com.exe201.exe201be.entities.FoodOrder;
import com.exe201.exe201be.entities.FoodOrderItem;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.repositories.FoodOrderItemRepository;
import com.exe201.exe201be.repositories.FoodOrderRepository;
import com.exe201.exe201be.responses.*;
import com.exe201.exe201be.services.IFoodOrderItemService;
import com.exe201.exe201be.services.IFoodOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/food_orders")
@RequiredArgsConstructor
public class FoodOrderController {
    private final IFoodOrderService foodOrderService;
    private final FoodOrderRepository foodOrderRepository;
    private final IFoodOrderItemService foodOrderItemService;
    private final FoodOrderItemRepository foodOrderItemRepository;


    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequestDetailDTO request,
                                         BindingResult result) {

        try {
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            FoodOrder foodOrder = foodOrderService.createOrder(
                    request.getOrderRequests(),
                    request.getFoodOrderDTO()
            );
            CreateOrderResponse response = new CreateOrderResponse("Order Created successfully", foodOrder);
            // Log thêm thông tin người dùng và quyền
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("User: " + authentication.getName());
            System.out.println("Authorities: " + authentication.getAuthorities());

            return ResponseEntity.ok(response);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get_food_order_by_id/{orderId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<?> getFoodOrder(@Valid @PathVariable Long orderId) throws DataNotFoundException {
        FoodOrderDetailResponse foodOrderDetailResponse = foodOrderService.getFoodOrderDetailById(orderId);
        return ResponseEntity.ok(foodOrderDetailResponse);
    }


    @GetMapping("/get_all_food_orders")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<FoodOrderResponseList> getAllOrders() {

        List<FoodOrder> orders = foodOrderService.getAllFoodOrders();

        return getFoodOrderResponseListResponseEntity(orders);
    }

    @GetMapping("/get_food_order_by_userId/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<?> getFoodOrdersByUser(@Valid @PathVariable Long userId) {

        List<FoodOrder> orders = foodOrderService.getFoodOrdersByUserId(userId);
        // Sắp xếp danh sách theo thuộc tính pickup_time giảm dần
        List<FoodOrderResponse> foodOrderResponseList = orders.stream()
                .sorted(Comparator.comparing(FoodOrder::getId).reversed()) // Sắp xếp giảm dần theo pickup_time
                .map(FoodOrderResponse::fromFoodOrders)
                .collect(Collectors.toList());
         return ResponseEntity.ok(foodOrderResponseList);
    }



    @GetMapping("/get_food_order_by_supplier_for_admin/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<?> getFoodOrdersByUserIdPaging(
            @PathVariable Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "orderTime") String sortBy, // Field to sort by
            @RequestParam(defaultValue = "desc") String sortDirection, // Sorting direction (asc/desc)
            @RequestParam(defaultValue = "0") int page, // Page number (0-indexed)
            @RequestParam(defaultValue = "10") int size) throws ParseException { // Page size

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


        // Convert sortDirection to Sort.Direction
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        // Create Pageable object with sorting
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        // Get paginated and sorted food orders
        Page<FoodOrder> foodOrders = foodOrderService.getFoodOrdersByUserIdPaging(userId, status, start, end, pageable);

        // Map từ FoodOrder sang FoodOrderResponseAdmin
        Page<FoodOrderResponseAdmin> response = foodOrders.map(FoodOrderResponseAdmin::fromFoodOrders);

        return ResponseEntity.ok(response);
    }


    @PutMapping("/update_order_status/{orderId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<?> updateFoodOrderStatus(@Valid @PathVariable Long orderId,
                                                   @RequestBody String orderStatus){
        try {
            foodOrderService.updateOrderStatus(orderId, orderStatus);
            return ResponseEntity.ok("order status updated successfully.");
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/update_payment_status/{orderId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<?> updatePaymentStatus(@Valid @PathVariable Long orderId,
                                                   @RequestBody int paymentStatus){
        try {
            foodOrderService.updatePaymentStatus(orderId, paymentStatus);
            return ResponseEntity.ok("payment status updated successfully.");
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    private ResponseEntity<FoodOrderResponseList> getFoodOrderResponseListResponseEntity(List<FoodOrder> orders) {
        List<FoodOrderResponse> orderResponses = orders.stream()
                .map(order -> {
                    List<FoodOrderItem> orderItems = foodOrderItemRepository.findByFoodOrderId(order.getId());
                    return FoodOrderResponse.fromFoodOrders(order);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(FoodOrderResponseList.builder()
                .orders(orderResponses)
                .build());
    }

    // API để xóa order theo orderId
    @DeleteMapping("/delete/{orderId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<Map<String, String>> deleteOrder(@PathVariable long orderId) {
        try {
            foodOrderService.deleteOrder(orderId);
            // Trả về JSON với status
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "order deleted successfully");
            return ResponseEntity.ok(response);
        } catch (DataNotFoundException e) {
            // Trả về lỗi nếu order không tồn tại
            Map<String, String> response = new HashMap<>();
            response.put("status", "failed");
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            // Trả về lỗi chung nếu có lỗi khác
            Map<String, String> response = new HashMap<>();
            response.put("status", "failed");
            response.put("errorMessage", "An error occurred while deleting the order");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
