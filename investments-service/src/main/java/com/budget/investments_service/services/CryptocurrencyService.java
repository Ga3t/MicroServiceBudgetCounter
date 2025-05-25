package com.budget.investments_service.services;

import com.budget.investments_service.models.dto.CryptoDto;
import com.budget.investments_service.models.dto.DailyPriceDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CryptocurrencyService {

    List<CryptoDto> findFullCryptoList();
    Page<CryptoDto> getListOfCryptocurrency(int pageSize, int pageNo);
    List<DailyPriceDto> findDailyPriceList(String cryptoId);
    List<DailyPriceDto> getDailyCryptoPrice(String cryptoId);
    DailyPriceDto findCryptoPriceNow(String cryptoId);

}
