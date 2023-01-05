package com.example.keki.dessert.service;

import com.example.keki.dessert.repository.DessertRepository;
import org.springframework.stereotype.Service;

@Service
public class DessertService {
    private DessertRepository dessertRepository;

    public DessertService(DessertRepository dessertRepository){
        this.dessertRepository = dessertRepository;
    }
}
