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
    // users(2000~2099)
    NULL_TOKEN(false, 2000, "토큰 값을 입력해주세요."),

    // stores(2100~2199)


    // posts(2200~2299)
    INVALID_POSTS_SIZE(false, 2200, "리스트 사이즈는 1 이상이어야 합니다."),
    NO_PARAMETER(false, 2201, "쿼리 파라미터 조건을 설정해주세요."),
    MANY_PARAMETER(false, 2202, "쿼리 파라미터 조건을 한개만 설정해주세요."),
    INVALID_REPORT_CATEGORY(false, 2203, "신고 카테고리를 찾을 수 없습니다."),

    // desserts(2300~2399)


    // calendars(2400~2499)
    NULL_TITLE(false, 2400, "캘린더 제목을 입력해주세요."),
    NULL_KIND_OF_CALENDARS(false, 2401, "캘린더 종류를 선택해주세요."),
    NULL_DATE(false, 2402, "날짜를 입력해주세요."),

    // histories(2500~2599)


    /**
     *  3000: Response 오류
     */
    // users(3000~3099)
    INVALID_USER_IDX(false, 3000, "사용자를 찾을 수 없습니다."),
    EXIST_NICKNAME(false, 3001, "이미 사용 중인 닉네임입니다."),
    INVALID_EMAIL(false, 3002, "존재하지 않는 이메일입니다."),
    NO_STORE_ROLE(false, 3003, "판매자가 아닙니다."),
    INVALID_USER_STATUS(false, 3004, "비활성화된 사용자입니다."),

    // stores(3100~3199)
    INVALID_STORE_IDX(false, 3100, "존재하지 않는 스토어입니다."),

    // posts(3200~3299)
    INVALID_POST_IDX(false, 3200, "존재하지 않는 피드입니다."),
    NO_MATCH_POST_STORE(false, 3201, "해당 피드의 작성자가 아닙니다."),

    // desserts(3300~3399)
    INVALID_DESSERT_IDX(false, 3300, "존재하지 않는 디저트입니다."),

    // calendars(3400~3499)
    INVALID_CALENDAR_TAG(false, 3400, "캘린더 TAG를 찾을 수 없습니다."),
    INVALID_TAG(false, 3401, "TAG를 찾을 수 없습니다."),
    INVALID_USER_AND_STATUS(false, 3402, "캘린더 접근이 불가능합니다."),
    INVALID_CALENDAR_IDX(false, 3403, "존재하지 않는 캘린더 입니다."),
    NO_MATCH_CALENDAR_USER(false, 3404, "캘린더를 생성한 사용자가 아닙니다."),

    // histories(3500~3599)

    // cs(3600~3699)
    NO_NOTICE(false, 3600, "공지사항이 없습니다."),
    INVALID_NOTICE_IDX(false, 3601, "존재하지 않는 공지사항 입니다."),

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