package com.exe201.exe201be.controllers;

import com.exe201.exe201be.dtos.OrderRatingDTO;
import com.exe201.exe201be.entities.OrderRating;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.services.IOrderRatingService;
import com.exe201.exe201be.services.OrderRatingService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/order_ratings")
@RequiredArgsConstructor
public class OrderRatingController {
    private final IOrderRatingService orderRatingService;

    @PostMapping("/add_rating")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CUSTOMER')")
    public ResponseEntity<OrderRating> addRating(@RequestBody OrderRatingDTO orderRatingDTO) throws DataNotFoundException {
        OrderRating savedRating = orderRatingService.addRating(orderRatingDTO);
        return ResponseEntity.ok(savedRating);
    }
}
