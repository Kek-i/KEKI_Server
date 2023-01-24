package com.codepatissier.keki.common;

public class Constant {
    public static final String ACTIVE_STATUS = "active";
    public static final String INACTIVE_STATUS = "inactive";

    public static class Posts{
        public static final Integer DEFAULT_SIZE = 12;
    }

    public static class Auth{
        public static final String CLAIM_NAME = "userIdx";
        public static final String REQUEST_HEADER_NAME = "Authorization";
        public static final Long ADMIN_USERIDX = 0L;
        public static final String TOKEN_REGEX = "^Bearer( )*";
        public static final String TOKEN_REPLACEMENT = "";
    }
}
