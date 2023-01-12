package com.codepatissier.keki.common;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * 1000: 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     *  2000: Request 오류
     */
    // post
    INVALID_POSTS_SIZE(false, 2100, "리스트 사이즈는 1 이상이어야 합니다."),

    /**
     *  3000: Response 오류
     */
    // store
    INVALID_STORE_IDX(false, 3000, "존재하지 않는 스토어입니다."),

    // post
    INVALID_POST_IDX(false, 3100, "존재하지 않는 피드입니다."),

    /**
     * 4000: DB, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패했습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
