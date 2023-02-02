package com.codepatissier.keki.store.dto;

import com.codepatissier.keki.common.EmptyStringToNullConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostStoreReq {
    @NotBlank
    private String nickname;
    @Convert(converter = EmptyStringToNullConverter.class)
    private String storeImgUrl;
    @NotBlank
    private String address;
    @Convert(converter = EmptyStringToNullConverter.class)
    private String introduction;
    @NotBlank
    private String orderUrl;
    @NotBlank
    private String businessName;
    @NotBlank
    private String brandName;
    @NotBlank
    private String businessAddress;
    @NotBlank
    private String businessNumber;
}
