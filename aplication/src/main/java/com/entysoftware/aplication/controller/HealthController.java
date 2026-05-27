package com.entysoftware.aplication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api")
public class HealthController {
    
    @GetMapping("/health")
    public Map<String,String> health() {

        Map<String,String> request = Map.of("Api", "Succesful");

        return  request;
    }
    


}
