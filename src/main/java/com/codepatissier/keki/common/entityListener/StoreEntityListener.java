package com.codepatissier.keki.common.entityListener;

import com.codepatissier.keki.common.BeanUtils;
import com.codepatissier.keki.dessert.repository.DessertRepository;
import com.codepatissier.keki.store.entity.Store;

import javax.persistence.PreRemove;

public class StoreEntityListener {

    @PreRemove
    public void onUpdate(Store store){
        DessertRepository dessertRepository = BeanUtils.getBean(DessertRepository.class);
        dessertRepository.deleteByStore(store);
    }
}
