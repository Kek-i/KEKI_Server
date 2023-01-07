package com.codepatissier.keki.stores.service;

import com.codepatissier.keki.stores.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {
    private StoreRepository storeRepository;
}
