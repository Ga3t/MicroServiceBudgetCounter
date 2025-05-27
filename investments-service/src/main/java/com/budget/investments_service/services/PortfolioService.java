package com.budget.investments_service.services;


import com.budget.investments_service.models.dto.CreateCryptoTransactionDto;
import com.budget.investments_service.models.dto.CryptoFromPortfolioDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PortfolioService {

    String buyCryptoTransaction(CreateCryptoTransactionDto createCryptoTransactionDTO, Long userId);
    List<CryptoFromPortfolioDto> getUserPortfolio(String userId);
    String cellCryptoTransaction(CreateCryptoTransactionDto createCryptoTransactionDTO, Long userId);



}
