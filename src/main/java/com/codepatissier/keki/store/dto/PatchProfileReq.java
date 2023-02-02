package com.codepatissier.keki.store.dto;

import com.codepatissier.keki.common.EmptyStringToNullConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;

@Data
@NoArgsConstructor
@AllArgsConstructor
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