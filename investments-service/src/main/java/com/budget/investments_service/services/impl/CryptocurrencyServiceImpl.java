package com.budget.investments_service.services.impl;


import com.budget.investments_service.models.dto.CryptoDto;
import com.budget.investments_service.services.CryptocurrencyService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;

@Service
@Primary
public class CryptocurrencyServiceImpl implements CryptocurrencyService {

    @Value("${api.coingecko.url}")
    private String apiUrl;

    @Value("${app.coingeko-api-key}")
    private String apiKey;

    private RestTemplate restTemplate;

    @Autowired
    public CryptocurrencyServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    @Cacheable(value = "cryptoList")
    public Page<CryptoDto> findListOfCryptocurrency(int pageSize, int pageNo) {
        String url = apiUrl +"markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1";
        System.out.println("URL in service: " + url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-cg-demo-api-key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<CryptoDto>> response = restTemplate.exchange(url, HttpMethod.GET,
                entity, new ParameterizedTypeReference<List<CryptoDto>>(){});
        if(response.getStatusCode()== HttpStatus.OK && response.getBody() != null){
            List<CryptoDto> cryptoList= response.getBody();
            int total = cryptoList.size();
            int fromIndex = Math.min(pageNo * pageSize, total);
            int toIndex = Math.min(fromIndex + pageSize, total);

            List<CryptoDto> pageContent = cryptoList.subList(fromIndex, toIndex);

            return new PageImpl<>(pageContent, PageRequest.of(pageNo, pageSize), total);
        }
        return new PageImpl<>(Collections.emptyList(), PageRequest.of(pageNo, pageSize), 0);
    }
}
