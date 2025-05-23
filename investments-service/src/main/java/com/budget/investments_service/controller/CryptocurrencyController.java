package com.budget.investments_service.controller;


import com.budget.investments_service.models.dto.CryptoDto;
import com.budget.investments_service.services.CryptocurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cryptocurrency")
public class CryptocurrencyController {

    private CryptocurrencyService cryptocurrencyService;

    @Autowired
    public CryptocurrencyController(CryptocurrencyService cryptocurrencyService) {
        this.cryptocurrencyService = cryptocurrencyService;
    }

    @GetMapping("/getlist")
    public ResponseEntity<?> getListofCrypto(@RequestParam(value="pageNo", defaultValue = "0", required = false) int pageNo,
                                             @RequestParam(value="pageSize", defaultValue = "10", required = false) int pageSize){

        Page<CryptoDto> cryptoList = cryptocurrencyService.findListOfCryptocurrency(pageSize, pageNo);

        return new ResponseEntity<>(cryptoList, HttpStatus.OK);

    }
}
