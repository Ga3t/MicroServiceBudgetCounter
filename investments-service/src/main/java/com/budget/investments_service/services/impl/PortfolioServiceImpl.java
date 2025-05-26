package com.budget.investments_service.services.impl;


import com.budget.investments_service.models.CryptocurrencyEntity;
import com.budget.investments_service.models.PortfolioEntity;
import com.budget.investments_service.models.TransactionEntity;
import com.budget.investments_service.models.TransactionType;
import com.budget.investments_service.models.dto.CreateCryptoTransactionDto;
import com.budget.investments_service.models.dto.CryptoFromPortfolioDto;
import com.budget.investments_service.models.dto.DailyPriceDto;
import com.budget.investments_service.repository.CryptocurrencyRepository;
import com.budget.investments_service.repository.PortfolioRepository;
import com.budget.investments_service.repository.TransactionRepository;
import com.budget.investments_service.services.CryptocurrencyService;
import com.budget.investments_service.services.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Primary
public class PortfolioServiceImpl implements PortfolioService{

    private PortfolioRepository portfolioRepository;
    private CryptocurrencyService cryptocurrencyService;
    private CryptocurrencyRepository cryptocurrencyRepository;
    private TransactionRepository transactionRepository;

    @Autowired
    public PortfolioServiceImpl(PortfolioRepository portfolioRepository,
                                CryptocurrencyService cryptocurrencyService,
                                CryptocurrencyRepository cryptocurrencyRepository,
                                TransactionRepository transactionRepository) {
        this.portfolioRepository = portfolioRepository;
        this.cryptocurrencyService = cryptocurrencyService;
        this.cryptocurrencyRepository = cryptocurrencyRepository;
        this.transactionRepository = transactionRepository;
    }


    @Override
    @Transactional
    public String buyCryptoTransaction(CreateCryptoTransactionDto createCryptoTransactionDTO, Long userId) {

       // Long userLongId= Long.parseLong(userId);
        Long userLongId= userId;
        TransactionEntity newTransaction = new TransactionEntity();

        newTransaction.setUserId(userLongId);

        String cryptoId= createCryptoTransactionDTO.getCryptoId();
        BigDecimal cryptoAmount = createCryptoTransactionDTO.getAmount();
        LocalDateTime timeTransaction= createCryptoTransactionDTO.getDateTime();

        List<DailyPriceDto> dailyPriceDtos = cryptocurrencyService.getDailyCryptoPrice(cryptoId);
        System.out.println(dailyPriceDtos);
        BigDecimal priceCryptoForOne = cryptocurrencyService.findPriceAtTime24h(dailyPriceDtos,timeTransaction);

        if(Objects.equals(priceCryptoForOne, BigDecimal.ZERO)){
            LocalDateTime now = LocalDateTime.now();
            Duration maxAllowedOffset = Duration.ofHours(24);
            Duration actualOffset = Duration.between(timeTransaction, now).abs();

            if (actualOffset.compareTo(maxAllowedOffset) > 0) {
                priceCryptoForOne= cryptocurrencyService.findPriceAtTimeOld(cryptoId,timeTransaction);
                BigDecimal priceCrypto = priceCryptoForOne.multiply(cryptoAmount);
                newTransaction.setPrice(priceCrypto);
            }
        }else{
            BigDecimal priceCrypto = priceCryptoForOne.multiply(cryptoAmount);
            newTransaction.setPrice(priceCrypto);
        }

            newTransaction.setDateTime(timeTransaction);
            newTransaction.setAmount(cryptoAmount);
            newTransaction.setType(TransactionType.BUY);
            Optional<CryptocurrencyEntity> crypto = cryptocurrencyRepository.findByCryptoId(cryptoId);
        if(crypto.isEmpty()){
            CryptocurrencyEntity newCrypto = new CryptocurrencyEntity();
            newCrypto.setCryptoId(cryptoId);
            cryptocurrencyRepository.save(newCrypto);
            newTransaction.setCryptocurrency(newCrypto);
            addToPortfolio(newCrypto, cryptoAmount,userLongId);
        }else {
            CryptocurrencyEntity newCrypto = crypto.orElseThrow(()->new RuntimeException("Temporary solution"));
            newTransaction.setCryptocurrency(newCrypto);
            addToPortfolio(newCrypto, cryptoAmount,userLongId);
        }
        transactionRepository.save(newTransaction);

        return "Transaction successfully recorded";
    }

    @Override
    public List<CryptoFromPortfolioDto> getUserPortfolio(String userId) {
        return List.of();
    }

    public void addToPortfolio(CryptocurrencyEntity crypto, BigDecimal amount, Long userId){
        Optional<PortfolioEntity> optionalPortfolio = portfolioRepository.findByUserIdAndCryptocurrency(userId, crypto);

        PortfolioEntity portfolio;
        if (optionalPortfolio.isPresent()) {
            portfolio = optionalPortfolio.get();
            BigDecimal oldAmount = portfolio.getAmount();
            BigDecimal newAmount = oldAmount.add(amount);
            portfolio.setAmount(newAmount);
        } else {
            portfolio = new PortfolioEntity();
            portfolio.setUserId(userId);
            portfolio.setCryptocurrency(crypto);
            portfolio.setAmount(amount);
        }

        portfolioRepository.save(portfolio);
    }
}

