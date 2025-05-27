package com.budget.investments_service.models.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class CryptoFromPortfolioDto {

    private String cryptoName;
    private BigDecimal amount;
    private BigDecimal currentPrice;

}
