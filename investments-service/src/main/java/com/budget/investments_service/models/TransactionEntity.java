package com.budget.investments_service.models;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="TRANSACTIONS")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="CRYPTO_NAME", nullable = false)
    private String cryptoName;
    @Column(name="PRICE_USD", nullable = false)
    private BigDecimal price;
    @Column(name="DATE", nullable = false)
    private LocalDateTime dateTime;
    @Column(name="CRYPTO_AMOUNT", nullable = false)
    private BigDecimal amount;

}
