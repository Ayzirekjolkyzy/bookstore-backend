package com.okuylu_back.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DiscountRequest {

    @NotBlank(message = "Discount name is required")
    private String discountName;

    @NotNull(message = "Discount percentage is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount percentage must be greater than 0")
    @DecimalMax(value = "100.0", message = "Discount percentage cannot exceed 100")
    private BigDecimal discountPercentage;

    @NotBlank(message = "Discount image URL is required")
    private String discImage;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    public DiscountRequest() {
    }

    public @NotNull(message = "Discount percentage is required") @DecimalMin(value = "0.0", inclusive = false, message = "Discount percentage must be greater than 0") @DecimalMax(value = "100.0", message = "Discount percentage cannot exceed 100") BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(@NotNull(message = "Discount percentage is required") @DecimalMin(value = "0.0", inclusive = false, message = "Discount percentage must be greater than 0") @DecimalMax(value = "100.0", message = "Discount percentage cannot exceed 100") BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public @NotBlank(message = "Discount image URL is required") String getDiscImage() {
        return discImage;
    }

    public void setDiscImage(@NotBlank(message = "Discount image URL is required") String discImage) {
        this.discImage = discImage;
    }

    public @NotNull(message = "Start date is required") LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(@NotNull(message = "Start date is required") LocalDate startDate) {
        this.startDate = startDate;
    }

    public @NotNull(message = "End date is required") LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(@NotNull(message = "End date is required") LocalDate endDate) {
        this.endDate = endDate;
    }

    public @NotBlank(message = "Discount name is required") String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(@NotBlank(message = "Discount name is required") String discountName) {
        this.discountName = discountName;
    }

    public DiscountRequest(String discountName, BigDecimal discountPercentage, String discImage, LocalDate startDate, LocalDate endDate) {
        this.discountName = discountName;
        this.discountPercentage = discountPercentage;
        this.discImage = discImage;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}