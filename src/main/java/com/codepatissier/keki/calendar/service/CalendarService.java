package com.codepatissier.keki.calendar.service;

import com.codepatissier.keki.calendar.CalendarCategory;
import com.codepatissier.keki.calendar.dto.CalendarHashTag;
import com.codepatissier.keki.calendar.dto.CalendarReq;
import com.codepatissier.keki.calendar.entity.Calendar;
import com.codepatissier.keki.calendar.entity.CalendarTag;
import com.codepatissier.keki.calendar.repository.CalendarRepository;
import com.codepatissier.keki.calendar.repository.CalendarTagRepository;
import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponseStatus;
import com.codepatissier.keki.common.Tag.TagRepository;
import com.codepatissier.keki.user.entity.User;
import com.codepatissier.keki.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final CalendarTagRepository calendarTagRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public void createCalendar(Long userIdx, CalendarReq calendarReq) throws BaseException {
        try {
            User user = this.userRepository.findById(userIdx).
                    orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER_IDX));
            Calendar calendar = Calendar.builder()
                    .calendarCategory(CalendarCategory.getCalendarCategoryByName(calendarReq.getKindOfCalendar()))
                    .calendarTitle(calendarReq.getTitle())
                    .calendarDate(calendarReq.getDate())
                    .user(user)
                    .build();
            this.calendarRepository.save(calendar);

            // for문으로 추가하기
            for(CalendarHashTag hashTag: calendarReq.getHashTags()){
                saveHashTags(calendar, hashTag);
            }
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    private void saveHashTags(Calendar calendar, CalendarHashTag hashtag) throws BaseException {
        this.calendarTagRepository.save(CalendarTag.builder()
                .tag(this.tagRepository.findByTagName(hashtag.getCalendarHashTag()).orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_TAG)))
                .calendar(calendar)
                .build());
    }

}
