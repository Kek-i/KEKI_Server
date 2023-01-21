package com.codepatissier.keki.calendar.service;

import com.codepatissier.keki.calendar.CalendarCategory;
import com.codepatissier.keki.calendar.dto.CalendarHashTag;
import com.codepatissier.keki.calendar.dto.CalendarReq;
import com.codepatissier.keki.calendar.dto.CalendarRes;
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

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.codepatissier.keki.common.Constant.INACTIVE_STATUS;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final CalendarTagRepository calendarTagRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public void createCalendar(Long userIdx, CalendarReq calendarReq) throws BaseException {
        try {
            User user = findUserByUserIdx(userIdx);
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

    public void deleteCalendar(Long calendarIdx, Long userIdx) throws BaseException{
        User user = findUserByUserIdx(userIdx);

        Calendar calendar = findCalendarByCalendarIdx(calendarIdx);

        if(!calendar.getUser().equals(user) || user.getStatus().equals(INACTIVE_STATUS)){
            throw new BaseException(BaseResponseStatus.INVALID_USER_AND_STATUS);
        }
        changeCalendarStatus(calendar, INACTIVE_STATUS);
        changeCalendarTagStatus(calendar, INACTIVE_STATUS);
    }

    public CalendarRes getCalendar(Long calendarIdx, Long userIdx) throws BaseException {
        try{
            User user = findUserByUserIdx(userIdx);
            Calendar calendar = findCalendarByCalendarIdx(calendarIdx);
            List<CalendarTag> tag = this.calendarTagRepository.findByCalendar(calendar);

            if(calendar.getUser() != user) throw new BaseException(BaseResponseStatus.NO_MATCH_CALENDAR_USER);

            int day = (int) Duration.between(calendar.getCalendarDate().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();

            CalendarRes returnCalendar = new CalendarRes(calendar.getCalendarCategory().getName(),
                    calendar.getCalendarTitle(),
                    null,
                    tag.stream().map(tags -> new CalendarHashTag(tags.getTag().getTagName())).collect(Collectors.toList()));

            if(calendar.getCalendarCategory().equals(CalendarCategory.D_DAY)){
                if(day == 0) returnCalendar.setDate("D-DAY");
                else if(day > 0) returnCalendar.setDate("D+" + day);
                else returnCalendar.setDate("D-" + day);
            }else if(calendar.getCalendarCategory().equals(CalendarCategory.DATE_COUNT)){
                returnCalendar.setDate(day+1+"");
            }else{
                // TODO 매년 반복은 스케줄러 사용 후 코드 변경을 해둘 것임.
                returnCalendar.setDate(day+1+"");
            }
            return returnCalendar;
        } catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }


    private User findUserByUserIdx(Long userIdx) throws BaseException {
        return this.userRepository.findById(userIdx).
                orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER_IDX));
    }

    private Calendar findCalendarByCalendarIdx(Long calendarIdx) throws BaseException {
        return this.calendarRepository.findById(calendarIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_CALENDAR_IDX));
    }


    private void changeCalendarTagStatus(Calendar calendar, String status){
        this.calendarTagRepository.findByCalendar(calendar).stream()
                .forEach(tag -> {
                    tag.setStatus(status);
                    this.calendarTagRepository.save(tag);
                });
    }

    private void changeCalendarStatus(Calendar calendar, String status) {
        calendar.setStatus(status);
        this.calendarRepository.save(calendar);
    }
}
