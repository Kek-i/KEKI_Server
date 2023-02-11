package com.codepatissier.keki.store.dto;

import com.codepatissier.keki.common.EmptyStringToNullConverter;
import lombok.Data;

import javax.persistence.Convert;
import javax.validation.constraints.NotBlank;

@Data
public class PostStoreReq {
    @NotBlank(message = "닉네임은 필수 항목입니다.")
    private String nickname;
    @Convert(converter = EmptyStringToNullConverter.class)
    private String storeImgUrl;
    @NotBlank(message = "가게 주소는 필수 항목입니다.")
    private String address;
    @Convert(converter = EmptyStringToNullConverter.class)
    private String introduction;
    @NotBlank(message = "주문 링크는 필수 항목입니다.")
    private String orderUrl;
    @NotBlank(message = "대표자명은 필수 항목입니다.")
    private String businessName;
    @NotBlank(message = "상호명은 필수 항목입니다.")
    private String brandName;
    @NotBlank(message = "사업자 주소는 필수 항목입니다.")
    private String businessAddress;
    @NotBlank(message = "사업자 번호는 필수 항목입니다.")
    private String businessNumber;
}
