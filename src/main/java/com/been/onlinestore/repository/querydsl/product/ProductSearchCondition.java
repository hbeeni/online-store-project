package com.been.onlinestore.repository.querydsl.product;

import javax.validation.constraints.Positive;

import com.been.onlinestore.domain.constant.SaleStatus;

public record ProductSearchCondition(
	@Positive
	Long categoryId,

	String name,

	SaleStatus saleStatus
) {

	public static ProductSearchCondition of(Long categoryId, String name, SaleStatus saleStatus) {
		return new ProductSearchCondition(categoryId, name, saleStatus);
	}
}
