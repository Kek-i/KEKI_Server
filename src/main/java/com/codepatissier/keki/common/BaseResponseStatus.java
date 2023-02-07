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
    NULL_EMAIL(false, 2001, "이메일을 입력해주세요."),
    NULL_PROVIDER(false, 2002, "소셜 이름을 입력해주세요."),
    INVALID_PROVIDER(false, 2003, "잘못된 소셜 이름입니다."),
    ALREADY_WITHDRAW_USER(false, 2003, "이미 삭제된 회원입니다."),

    // stores(2100~2199)


    // posts(2200~2299)
    INVALID_POSTS_SIZE(false, 2200, "리스트 사이즈는 1 이상이어야 합니다."),
    NO_PARAMETER(false, 2201, "쿼리 파라미터 조건을 설정해주세요."),
    MANY_PARAMETER(false, 2202, "쿼리 파라미터 조건을 한개만 설정해주세요."),
    INVALID_REPORT_CATEGORY(false, 2203, "신고 카테고리를 찾을 수 없습니다."),
    INVALID_SORT_TYPE(false, 2204, "잘못된 정렬 카테고리입니다."),
    DO_NOT_STORE_SORT_TYPE(false, 2204, "스토어별 피드는 정렬 불가합니다."),
    NULL_POST_IDX(false, 2205, "postIdx를 입력해주세요."),
    NULL_CURSOR(false, 2206, "필요한 cursor를 모두 입력해주세요."),
    MANY_CURSOR_PARAMETER(false, 2207, "cursor 조건을 한개만 입력해주세요."),
    INVALID_SORT_TYPE_CURSOR(false, 2208, "정렬 조건과 cursor 조건을 맞춰서 입력해주세요."),

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
    INVALID_TOKEN(false, 3005, "잘못된 토큰 값입니다."),
    EXPIRED_TOKEN(false, 3006, "만료된 토큰 값입니다."),

    // stores(3100~3199)
    INVALID_STORE_IDX(false, 3100, "존재하지 않는 스토어입니다."),
    NULL_ADDRESS(false, 3101, "주소를 입력해주세요."),
    NULL_ORDER_URL(false, 3102, "주문 링크를 입력해주세요."),
    NULL_BUSINESS_NAME(false, 3103, "대표자명을 입력해주세요."),
    NULL_BRAND_NAME(false, 3104, "상호명을 입력해주세요."),
    NULL_BUSINESS_ADDRESS(false, 3105, "사업자 주소를 입력해주세요."),
    NULL_BUSINESS_NUMBER(false, 3106, "사업자 번호를 입력해주세요."),
    NULL_NICKNAME(false, 3107, "가게 이름을 입력해주세요."),

    // posts(3200~3299)
    INVALID_POST_IDX(false, 3200, "존재하지 않는 피드입니다."),
    NO_MATCH_POST_STORE(false, 3201, "해당 피드의 작성자가 아닙니다."),

    // desserts(3300~3399)
    INVALID_DESSERT_IDX(false, 3300, "존재하지 않는 디저트입니다."),
    NULL_DESSERT_IMG(false, 3301, "디저트 이미지를 추가해주세요."),
    NULL_DESSERT_NAME(false, 3302, "디저트 이름을 입력해주세요."),
    INVALID_DESERT_PRICE(false, 3303, "디저트 가격은 0원 이상이어야 합니다."),
    NULL_DESSERT_DESCRIPTION(false, 3304, "디저트 설명을 입력해주세요."),

    // calendars(3400~3499)
    INVALID_CALENDAR_TAG(false, 3400, "캘린더 TAG를 찾을 수 없습니다."),
    INVALID_TAG(false, 3401, "TAG를 찾을 수 없습니다."),
    INVALID_USER_AND_STATUS(false, 3402, "캘린더 접근이 불가능합니다."),
    INVALID_CALENDAR_IDX(false, 3403, "존재하지 않는 캘린더 입니다."),
    NO_MATCH_CALENDAR_USER(false, 3404, "캘린더를 생성한 사용자가 아닙니다."),
    INVALID_CALENDAR_DATE_COUNT(false, 3405, "날짜 수는 미래 생성이 불가능 합니다."),

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