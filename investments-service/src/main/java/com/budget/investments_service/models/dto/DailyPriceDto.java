package com.budget.investments_service.models.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DailyPriceDto {

    private BigDecimal price;
    private LocalDateTime dateTime;

    public DailyPriceDto(BigDecimal price, LocalDateTime dateTime) {
        this.price = price;
        this.dateTime = dateTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
