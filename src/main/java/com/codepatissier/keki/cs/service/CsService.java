package com.codepatissier.keki.cs.service;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.Constant;
import com.codepatissier.keki.cs.dto.GetNoticeListRes;
import com.codepatissier.keki.cs.dto.GetNoticeRes;
import com.codepatissier.keki.cs.entity.Notice;
import com.codepatissier.keki.cs.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.codepatissier.keki.common.BaseResponseStatus.INVALID_NOTICE_IDX;
import static com.codepatissier.keki.common.BaseResponseStatus.NO_NOTICE;

@Service
@RequiredArgsConstructor
public class CsService {
    private final NoticeRepository noticeRepository;

    // 공지사항 전체 조회
    public List<GetNoticeListRes> getNoticeList() throws BaseException {
        List<Notice> noticeList = noticeRepository.findAllByStatusEquals(Constant.ACTIVE_STATUS, Sort.by(Sort.Direction.DESC, "createdDate"));
        if(noticeList.isEmpty()) throw new BaseException(NO_NOTICE);
        return noticeList.stream()
                .map(notice -> new GetNoticeListRes(notice.getNoticeIdx(), notice.getNoticeTitle()))
                .collect(Collectors.toList());
    }

    // 공지사항 상세 조회
    public GetNoticeRes getNotice(Long noticeIdx) throws BaseException {
        Notice notice = noticeRepository.findByNoticeIdxAndStatusEquals(noticeIdx, Constant.ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_NOTICE_IDX));
        return new GetNoticeRes(notice.getNoticeTitle(), notice.getNoticeContent());
    }
}
