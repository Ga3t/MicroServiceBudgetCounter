package com.budget.investments_service.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="PORTFOLIO")
public class PortfolioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @Column(name="CRYPTOCURRENCY_NAME", nullable = false)
    private String cryprocurrency_name;

    @Column(name="QUANTITY", nullable = false)
    private BigDecimal quantity;

    @Column(name="DATE_OF_TRANSACTION",nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S]")
    private LocalDateTime date;

    @Column(name="USER_ID", nullable = false)
    private Long userId;

    @Column(name = "PRICE_AT_PURCHASE", nullable = false)
    private BigDecimal priceAtPurchase;

}
