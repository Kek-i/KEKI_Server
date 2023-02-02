package com.codepatissier.keki.common;

public class Constant {
    public static final String ACTIVE_STATUS = "active";
    public static final String INACTIVE_STATUS = "inactive";
    public static final String LOGOUT_STATUS = "logout";

    public static class Posts{
        public static final Integer DEFAULT_SIZE = 12;
        public static final String NEW_SORT_TYPE= "최신순";
        public static final String POPULAR_SORT_TYPE= "인기순";
        public static final String LOW_PRICE_SORT_TYPE= "가격낮은순";
    }

    public static class Auth{
        public static final String CLAIM_NAME = "userIdx";
        public static final String REQUEST_HEADER_NAME = "Authorization";
        public static final Long ADMIN_USERIDX = 0L;
        public static final String TOKEN_REGEX = "^Bearer( )*";
        public static final String TOKEN_REPLACEMENT = "";
    }

    public static class Home{
        public static final int HOME_RETURN_TAG_COUNT = 3;
        public static final int HOME_RETURN_POST_COUNT = 5;

    }
}
