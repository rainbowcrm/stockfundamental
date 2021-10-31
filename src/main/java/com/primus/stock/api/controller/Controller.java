package com.primus.stock.api.controller;

import com.primus.stock.api.service.APIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:20450", maxAge = 3600)
@RequestMapping("/api/application")
@RestController
public class Controller  {

    @Autowired
    APIService apiService;

    public ResponseEntity<Map> readFundamentals(@RequestParam String scripCode)
    {

        //apiService.

    }
}
