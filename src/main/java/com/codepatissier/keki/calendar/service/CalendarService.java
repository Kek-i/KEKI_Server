package com.codepatissier.keki.calendar.service;

import com.codepatissier.keki.calendar.CalendarCategory;
import com.codepatissier.keki.calendar.dto.*;
import com.codepatissier.keki.calendar.entity.Calendar;
import com.codepatissier.keki.calendar.entity.CalendarTag;
import com.codepatissier.keki.calendar.repository.Calendar.CalendarRepository;
import com.codepatissier.keki.calendar.repository.CalendarTag.CalendarTagRepository;
import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponseStatus;
import com.codepatissier.keki.common.Constant;
import com.codepatissier.keki.common.Tag.TagRepository;
import com.codepatissier.keki.user.entity.User;
import com.codepatissier.keki.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @Transactional(rollbackFor= Exception.class)
    public void createCalendar(Long userIdx, CalendarReq calendarReq) throws BaseException {
        try {
            int day = (int) Duration.between(calendarReq.getDate().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
            if(calendarReq.getKindOfCalendar().equals(CalendarCategory.DATE_COUNT.getName()) && day<0){
                throw new BaseException(BaseResponseStatus.INVALID_CALENDAR_DATE_COUNT);
            }
            User user = findUserByUserIdx(userIdx);
            Calendar calendar = Calendar.builder()
                    .calendarCategory(CalendarCategory.getCalendarCategoryByName(calendarReq.getKindOfCalendar()))
                    .calendarTitle(calendarReq.getTitle())
                    .calendarDate(calendarReq.getDate())
                    .user(user)
                    .build();
            this.calendarRepository.save(calendar);

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

    @Transactional(rollbackFor= Exception.class)
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
        User user = findUserByUserIdx(userIdx);
        Calendar calendar = findCalendarByCalendarIdx(calendarIdx);
        List<CalendarTag> tag = this.calendarTagRepository.findByCalendar(calendar);

        if (calendar.getUser() != user) throw new BaseException(BaseResponseStatus.NO_MATCH_CALENDAR_USER);

        try {
            String day = calculateDate(calendar);
            calendar = findCalendarByCalendarIdx(calendarIdx); // 변경된 날짜로
            return new CalendarRes(calendar.getCalendarCategory().getName(),
                    calendar.getCalendarTitle(),
                    calendar.getCalendarDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    day,
                    tag.stream().map(tags -> new CalendarHashTag(tags.getTag().getTagName())).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    private String calculateDate(Calendar calendar) {
        String returnCalendar;
        int day = (int) Duration.between(calendar.getCalendarDate().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
        if(calendar.getCalendarCategory().equals(CalendarCategory.DATE_COUNT)){ // 날짜수
            returnCalendar = "D+"+(day +1);
        }else{
            if(calendar.getCalendarCategory().equals(CalendarCategory.EVERY_YEAR)){
                if(day>0){
                    // TODO : 매년 반복 시, 작성한 날짜에서 1년을 더하는 것이 아니라 이번 년도에서 1을 더하는 것으로 코드르 변경하는 것이 필요
                    calendar.setCalendarDate(calendar.getCalendarDate().plusYears(1));
                    calendarRepository.save(calendar);
                    day = (int) Duration.between(calendar.getCalendarDate().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
                }
            }
            if(day == 0) returnCalendar = "D-DAY";
            else if(day > 0) returnCalendar = "D+" + day;
            else returnCalendar = "D" + day;
        }
        return returnCalendar;
    }

    public List<CalendarListRes> getCalendarList(Long userIdx) throws BaseException {
        User user = this.findUserByUserIdx(userIdx);

        List<Calendar> calList = this.calendarRepository.findByUser(user);
        if(calList.isEmpty()) throw new BaseException(BaseResponseStatus.INVALID_CALENDAR_IDX);

        return calList.stream().
                map(calendar -> new CalendarListRes(calendar.getCalendarIdx(),
                        calendar.getCalendarTitle(),
                        calendar.getCalendarDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        calculateDate(calendar))).collect(Collectors.toList());
    }

    public List<TagRes> getCategories() throws BaseException{
        return this.tagRepository.findByStatus(Constant.ACTIVE_STATUS).stream()
                .map(tag -> new TagRes(tag.getTagIdx(), tag.getTagName()))
                .collect(Collectors.toList());
    }

    public HomeRes getHomeCalendar(Long userIdx) throws BaseException{
        User user = this.findUserByUserIdx(userIdx);
        try{
            Calendar calendar = this.calendarRepository.getRecentDateCalendar(user);
            int day = 0;
            String title = null;
            if(calendar != null){
                title = calendar.getCalendarTitle();
               day = (int) Duration.between(calendar.getCalendarDate().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
            }
            return new HomeRes(user.getUserIdx(), user.getNickname(), title, Math.abs(day), null);

        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    // 로그 아웃 된 상태에서 홈 정보 불러오기
    public HomeRes getHomeCalendarAndPostLogout() throws BaseException{
        try{
            return new HomeRes(null, null, null, 0,
                    getPostByTag(this.calendarTagRepository.getPopularCalendarTag()));
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    private List<HomeTagRes> getPostByTag(List<PopularTagRes> popularTagRes) {
        return popularTagRes.stream()
                .map(tag -> new HomeTagRes(tag.getTagIdx(), tag.getTagName(),
                        this.calendarRepository.getPagByPostLimit5(tag.getTagIdx())))
                .collect(Collectors.toList());
    }

    public HomeRes getHomeTagPost(HomeRes home) throws BaseException{
        User user = this.findUserByUserIdx(home.getUserIdx());
        List<PopularTagRes> tags = this.calendarTagRepository.getPopularCalendarTagByUser(user);
        // 기념일의 태그가 3개 미만이면 다 랜덤으로 불러오고
        if(tags.size()< Constant.Home.HOME_RETURN_TAG_COUNT){
            home.setHomeTagResList(this.getPostByTag(this.calendarTagRepository.getPopularCalendarTag()));
        }else{ // 태그가 3개 이상이면 태그별로 랜덤하게 불러오기
            home.setHomeTagResList(this.getPostByTag(tags));
        }
        return home;
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
