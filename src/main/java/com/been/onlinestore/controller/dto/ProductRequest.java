package com.been.onlinestore.controller.dto;

import com.been.onlinestore.domain.constant.SaleStatus;
import com.been.onlinestore.service.request.ProductServiceRequest;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record ProductRequest() {

    /**
     * 옵션 데이터는 입력하지 않을 시 기본값 또는 null 값이 저장됩니다.
     * @param categoryId 필수
     * @param name 필수
     * @param price 필수
     * @param description 옵션
     * @param stockQuantity 필수
     * @param saleStatus 옵션 - 기본값: WAIT
     * @param deliveryFee 필수
     * @param imageUrl 옵션
     */
    @Builder
    public record Create(
            @NotNull
            Long categoryId,

            @NotBlank @Size(max = 100)
            String name,

            @NotNull
            Integer price,

            @Size(max = 255)
            String description,

            @NotNull
            Integer stockQuantity,

            SaleStatus saleStatus,

            @NotNull
            Integer deliveryFee,

            @Size(max = 200)
            String imageUrl
    ) {

        public ProductServiceRequest.Create toServiceRequest() {
            return ProductServiceRequest.Create.of(
                    categoryId,
                    name,
                    price,
                    description,
                    stockQuantity,
                    saleStatus,
                    deliveryFee,
                    imageUrl
            );
        }
    }

    /**
     * 옵션 데이터는 입력하지 않을 시 null 값이 저장됩니다.
     * @param categoryId 필수
     * @param name 필수
     * @param price 필수
     * @param description 옵션
     * @param stockQuantity 필수
     * @param saleStatus 필수
     * @param deliveryFee 필수
     * @param imageUrl 옵션
     */
    @Builder
    public record Update(
            @NotNull
            Long categoryId,

            @NotBlank @Size(max = 100)
            String name,

            @NotNull
            Integer price,

            @Size(max = 255)
            String description,

            @NotNull
            Integer stockQuantity,

            @NotNull
            SaleStatus saleStatus,

            @NotNull
            Integer deliveryFee,

            @Size(max = 200)
            String imageUrl
    ) {

        public ProductServiceRequest.Update toServiceRequest() {
            return ProductServiceRequest.Update.of(
                    categoryId,
                    name,
                    price,
                    description,
                    stockQuantity,
                    saleStatus,
                    deliveryFee,
                    imageUrl
            );
        }
    }
}