package com.codepatissier.keki.cs.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class GetNoticeRes {
    private String noticeTitle;
    private String noticeContent;

    public GetNoticeRes(String noticeTitle, String noticeContent) {
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
    }
}
