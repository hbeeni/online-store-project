package com.been.onlinestore.controller.api.seller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.been.onlinestore.controller.dto.ApiResponse;
import com.been.onlinestore.controller.dto.security.PrincipalDetails;
import com.been.onlinestore.repository.querydsl.order.OrderSearchCondition;
import com.been.onlinestore.service.OrderService;
import com.been.onlinestore.service.response.OrderResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/seller/orders")
@RestController
public class SellerOrderApiController {

	private final OrderService orderService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrders(
		@AuthenticationPrincipal PrincipalDetails principalDetails,
		@ModelAttribute @Validated OrderSearchCondition cond,
		@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return ResponseEntity.ok(
			ApiResponse.pagination(orderService.findOrdersBySeller(principalDetails.id(), cond, pageable)));
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<ApiResponse<OrderResponse>> getOrder(
		@AuthenticationPrincipal PrincipalDetails principalDetails,
		@PathVariable Long orderId
	) {
		return ResponseEntity.ok(ApiResponse.success(orderService.findOrderBySeller(orderId, principalDetails.id())));
	}
}