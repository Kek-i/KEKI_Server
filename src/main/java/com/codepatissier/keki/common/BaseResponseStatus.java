package com.codepatissier.keki.common;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    EXIST_NICKNAME(false, 2000, "이미 사용 중인 닉네임입니다."),
    /**
     * users: RESPONSE
     */
    INVALID_USER_IDX(false, 3000, "사용자를 찾을 수 없습니다."),

    /**
     * histories: REQUEST
     */
    
    
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패했습니다."),;

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}