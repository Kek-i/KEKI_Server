package com.codepatissier.keki.common;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * 1000: 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2000: Request 오류
     */
    // [POST] /stores
    POST_STORES_EMPTY_ADDRESS(false, 2015, "주소를 입력해주세요."),
    POST_STORES_EMPTY_BUSINESSNAME(false, 2016, "대표자명을 입력해주세요."),
    POST_STORES_EMPTY_BRANDNAME(false, 2017, "상호명을 입력해주세요."),
    POST_STORES_EMPTY_BUSINESSADDRESS(false, 2018, "사업자 주소를 입력해주세요."),
    POST_STORES_EMPTY_BUSINESSNUMBER(false, 2019, "사업자 등록 번호를 입력해주세요."),

    /**
     * 4000: DB, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
