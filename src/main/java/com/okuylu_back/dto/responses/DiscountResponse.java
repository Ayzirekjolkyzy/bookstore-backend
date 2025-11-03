package com.okuylu_back.dto.responses;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DiscountResponse {

    private Long discountId;
    private String discountName;
    private BigDecimal discountPercentage;
    private String discImage;
    private LocalDate startDate;
    private LocalDate endDate;

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDiscImage() {
        return discImage;
    }

    public void setDiscImage(String discImage) {
        this.discImage = discImage;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public DiscountResponse(Long discountId, String discountName, BigDecimal discountPercentage, String discImage, LocalDate startDate, LocalDate endDate) {
        this.discountId = discountId;
        this.discountName = discountName;
        this.discountPercentage = discountPercentage;
        this.discImage = discImage;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
