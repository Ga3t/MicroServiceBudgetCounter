package com.budget.investments_service.services;


import com.budget.investments_service.models.CryptocurrencyEntity;
import com.budget.investments_service.models.dto.CreateCryptoTransactionDto;
import com.budget.investments_service.models.dto.CryptoFromPortfolioDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface PortfolioService {

    String buyCryptoTransaction(CreateCryptoTransactionDto createCryptoTransactionDTO, String userId);
    List<CryptoFromPortfolioDto> getUserPortfolio(Long userId);
    String cellCryptoTransaction(CreateCryptoTransactionDto createCryptoTransactionDTO, String userId);
    void addToPortfolio(CryptocurrencyEntity crypto, BigDecimal amount, Long userId);
}
