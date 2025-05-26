package com.budget.investments_service.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name="PORTFOLIO")
public class PortfolioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CRYPTO_ID", nullable = false)
    private CryptocurrencyEntity cryptocurrency;

    @Column(name="AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name="USER_ID", nullable = false)
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CryptocurrencyEntity getCryptocurrency() {
        return cryptocurrency;
    }

    public void setCryptocurrency(CryptocurrencyEntity cryptocurrency) {
        this.cryptocurrency = cryptocurrency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
