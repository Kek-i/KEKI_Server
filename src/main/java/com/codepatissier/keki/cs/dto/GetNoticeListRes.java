package com.codepatissier.keki.cs.dto;

import lombok.Data;

@Data
public class GetNoticeListRes {
    private Long noticeIdx;
    private String noticeTitle;

    public GetNoticeListRes(Long noticeIdx, String noticeTitle) {
        this.noticeIdx = noticeIdx;
        this.noticeTitle = noticeTitle;
    }
}
