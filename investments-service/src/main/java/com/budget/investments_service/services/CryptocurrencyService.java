package com.budget.investments_service.services;

import com.budget.investments_service.models.dto.CryptoDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CryptocurrencyService {

    Page<CryptoDto> findListOfCryptocurrency(int pageSize, int pageNo);
}
