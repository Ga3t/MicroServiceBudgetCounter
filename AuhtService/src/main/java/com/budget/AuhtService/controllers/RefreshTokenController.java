package com.budget.AuhtService.controllers;


import com.budget.AuhtService.models.RefreshToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/refreshtoken/")
public class RefreshTokenController {

    @GetMapping("/generatetoken")
    public ResponseEntity<?> generateRefreshToken(String userId){



        return null;
    }
}
