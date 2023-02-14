package com.codepatissier.keki.store.dto;

import com.codepatissier.keki.common.EmptyStringToNullConverter;
import lombok.Data;

import javax.persistence.Convert;

@Data
public class PatchProfileReq {
    @Convert(converter = EmptyStringToNullConverter.class)
    private String storeImgUrl;
    private String nickname;
    private String address;
    @Convert(converter = EmptyStringToNullConverter.class)
    private String introduction;
    private String orderUrl;
    private String businessName;
    private String brandName;
    private String businessAddress;
    private String businessNumber;
}