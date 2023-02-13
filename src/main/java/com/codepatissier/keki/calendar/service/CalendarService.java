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
import com.codepatissier.keki.common.Tag.Tag;
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

import static com.codepatissier.keki.common.Constant.ACTIVE_STATUS;
import static com.codepatissier.keki.common.Constant.INACTIVE_STATUS;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final CalendarTagRepository calendarTagRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    // 캘린더 생성
    @Transactional(rollbackFor= Exception.class)
    public void createCalendar(Long userIdx, CalendarReq calendarReq) throws BaseException {
        try {
            CalendarCategory category = categoryMandatoryException(calendarReq);
            User user = findUserByUserIdx(userIdx);
            Calendar calendar = Calendar.builder()
                    .calendarCategory(category)
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

    // 캘린더 별 날짜 예외 처리 + 캘린더 태그 예외 처리
    private CalendarCategory categoryMandatoryException(CalendarReq calendarReq) throws BaseException {
        int day = (int) Duration.between(calendarReq.getDate().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
        if(calendarReq.getKindOfCalendar().equals(CalendarCategory.DATE_COUNT.getName()) && day<0){
            throw new BaseException(BaseResponseStatus.INVALID_CALENDAR_DATE_COUNT);
        }
        CalendarCategory category = CalendarCategory.getCalendarCategoryByName(calendarReq.getKindOfCalendar());
        if(category== null){
            throw new BaseException(BaseResponseStatus.INVALID_CALENDAR_TAG);
        }
        return category;
    }


    /**
     * tag 찾기
     * active로 하지 않은 이유는
     * inactive인 tag여도 active인 상태일 때 저장한 것이 있을 수도 있기 때문
     */
    private Tag findByTagName(CalendarHashTag hashtag) throws BaseException {
        return this.tagRepository.findByTagName(hashtag.getCalendarHashTag()).orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_TAG));
    }

    // 캘린더 삭제
    @Transactional(rollbackFor= Exception.class)
    public void deleteCalendar(Long calendarIdx, Long userIdx) throws BaseException{
        User user = findUserByUserIdx(userIdx);
        Calendar calendar = findCalendarByCalendarIdx(calendarIdx);
        try{
            if(!calendar.getUser().equals(user) || user.getStatus().equals(INACTIVE_STATUS)){
                throw new BaseException(BaseResponseStatus.INVALID_USER_AND_STATUS);
            }
            changeCalendarStatus(calendar, INACTIVE_STATUS);
            changeCalendarTagStatus(calendar, INACTIVE_STATUS);
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    // 캘린더 하나 조회
    public CalendarRes getCalendar(Long calendarIdx, Long userIdx) throws BaseException {
        User user = findUserByUserIdx(userIdx);
        Calendar calendar = findCalendarByCalendarIdx(calendarIdx);
        List<CalendarTag> tag = this.calendarTagRepository.findByCalendarAndStatus(calendar, ACTIVE_STATUS);

        if (calendar.getUser() != user) throw new BaseException(BaseResponseStatus.NO_MATCH_CALENDAR_USER);

        try {
            return new CalendarRes(calendar.getCalendarCategory().getName(),
                    calendar.getCalendarTitle(),
                    calendar.getCalendarDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    calculateDateReturnString(calculateDate(calendar)),
                    tag.stream().map(tags -> new CalendarHashTag(tags.getTag().getTagName())).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    // 기념일 계산
    private int calculateDate(Calendar calendar) {
        int returnCalendar;
        int day = (int) Duration.between(calendar.getCalendarDate().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
        if(calendar.getCalendarCategory().equals(CalendarCategory.DATE_COUNT)){ // 날짜수
            returnCalendar = day +1;
        }else{ // 디데이, 매년 반복
            if(calendar.getCalendarCategory().equals(CalendarCategory.EVERY_YEAR)){
                if(day>0){
                    LocalDate date = calendar.getCalendarDate().withYear(LocalDate.now().getYear()); // 현재 년도로 변경
                    day = (int) Duration.between(date.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
                    if(day>0){
                        date = date.plusYears(1);
                        day = (int) Duration.between(date.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
                    }
                }
            }
            if(day == 0) returnCalendar = 0;
            else if(day > 0) returnCalendar = day;
            else returnCalendar = day;
        }
        return returnCalendar;
    }

    // 기념일 계산 return String
    private String calculateDateReturnString(int day){
        String returnCalendar;
        if(day == 0) returnCalendar = "D-DAY";
        else if(day > 0) returnCalendar = "D+" + day;
        else returnCalendar = "D" + day;
        return returnCalendar;
    }

    // 캘린더 리스트 조회
    public List<CalendarListRes> getCalendarList(Long userIdx) throws BaseException {
        User user = this.findUserByUserIdx(userIdx);
        try{
            return calendarRepository.findByUserAndStatus(user, Constant.ACTIVE_STATUS).stream().
                    map(calendar -> new CalendarListRes(calendar.getCalendarIdx(),
                            calendar.getCalendarTitle(),
                            calendar.getCalendarDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            calculateDateReturnString(calculateDate(calendar)))).collect(Collectors.toList());
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    // 카테고리 list 불러오기
    public List<TagRes> getCategories() throws BaseException{
        try{
            return this.tagRepository.findByStatus(Constant.ACTIVE_STATUS).stream()
                    .map(tag -> new TagRes(tag.getTagIdx(), tag.getTagName()))
                    .collect(Collectors.toList());
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public HomeRes getHomeCalendar(Long userIdx) throws BaseException{
        User user = this.findUserByUserIdx(userIdx);
        try{
            Calendar calendar = this.calendarRepository.getRecentDateCalendar(user); // 현재 가장 가까운 캘린더 불러오기
            int day = 0;
            String title = null;
            if(calendar != null){
                title = calendar.getCalendarTitle();
                day = (int) Duration.between(calendar.getCalendarDate().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
            }
            // 사용자의 매년 반복 캘린더 불러와서 하나씩 비교해보고, 값이 더 가까우면? 매년 반복으로 홈 화면 기념일 불러오기
            List<Calendar> listCalendars = this.calendarRepository.findByUserAndCalendarCategoryAndStatus(user, CalendarCategory.EVERY_YEAR, ACTIVE_STATUS);
            for(Calendar cal: listCalendars){
                if(this.calculateDate(cal) > day){
                    title = cal.getCalendarTitle();
                    day = this.calculateDate(cal);
                }
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


    // home api
    public HomeRes getHomeTagPost(HomeRes home) throws BaseException{
        User user = this.findUserByUserIdx(home.getUserIdx());
        try{
            List<PopularTagRes> tags = this.calendarTagRepository.getPopularCalendarTagByUser(user);
            // 기념일의 태그가 3개 미만이면 다 랜덤으로 불러오고
            if(tags.size()< Constant.Home.HOME_RETURN_TAG_COUNT){
                home.setHomeTagResList(this.getPostByTag(this.calendarTagRepository.getPopularCalendarTag()));
            }else{ // 태그가 3개 이상이면 태그별로 랜덤하게 불러오기
                home.setHomeTagResList(this.getPostByTag(tags));
            }
            return home;
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }


    // 캘린더 수정
    @Transactional(rollbackFor= Exception.class)
    public void modifyCalendar(Long userIdx, CalendarReq calendarReq, Long calendarIdx) throws BaseException{
        try{
            User user = this.findUserByUserIdx(userIdx);
            Calendar calendar = this.findCalendarByCalendarIdx(calendarIdx);
            if (calendar.getUser() != user) throw new BaseException(BaseResponseStatus.NO_MATCH_CALENDAR_USER);


            // TODO: 현재 수정 시 TAG의 경우에는 INACTIVE 후 새로 받은 것을 ACTIVE로 함 => DELETE로 변경?
            if (calendarReq.getTitle() != null){
                if(calendarReq.getTitle().equals("") || calendarReq.getTitle().equals(" "))
                    throw new BaseException(BaseResponseStatus.NULL_CALENDAR_TITLE);
                calendar.setCalendarTitle(calendarReq.getTitle());
            }
            if (calendarReq.getDate() != null) {
                calendar.setCalendarDate(calendarReq.getDate());
            }
            if (calendarReq.getKindOfCalendar() != null){
                CalendarCategory category = categoryMandatoryException(calendarReq);
                if(calendarReq.getKindOfCalendar().equals("") || calendarReq.getKindOfCalendar().equals(" "))
                    throw new BaseException(BaseResponseStatus.NULL_CALENDAR_CATEGORY);
                calendar.setCalendarCategory(CalendarCategory.getCalendarCategoryByName(calendarReq.getKindOfCalendar()));
            }

            if (calendarReq.getHashTags() != null && calendarReq.getHashTags().size() != 0) {
                this.changeCalendarTagStatus(calendar, INACTIVE_STATUS);
                for (CalendarHashTag hashTag : calendarReq.getHashTags()) {
                    CalendarTag calendarTag =  this.calendarTagRepository.findByCalendarAndTag(calendar, this.findByTagName(hashTag));
                    if(calendarTag == null) saveHashTags(calendar, hashTag);
                    else calendarTag.setStatus(ACTIVE_STATUS);
                }
            }
        }catch (BaseException e) {
            throw e;
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }

    // 캘린더 수정 조회 api
    public CalendarRes getEditCalendar(Long userIdx, Long calendarIdx) throws BaseException{
        User user = findUserByUserIdx(userIdx);
        Calendar calendar = findCalendarByCalendarIdx(calendarIdx);
        List<Tag> tags = this.tagRepository.findByStatus(ACTIVE_STATUS);

        if (calendar.getUser() != user) throw new BaseException(BaseResponseStatus.NO_MATCH_CALENDAR_USER);

        try {
            return new CalendarRes(calendar.getCalendarCategory().getName(),
                    calendar.getCalendarTitle(),
                    calendar.getCalendarDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    calculateDateReturnString(calculateDate(calendar)),
                    tags.stream().map(tag -> new CalendarHashTag(tag.getTagName())).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    /**
     * extract method
     */

    // 사용자 찾기
    private User findUserByUserIdx(Long userIdx) throws BaseException {
        return this.userRepository.findByUserIdxAndStatusEquals(userIdx, Constant.ACTIVE_STATUS).
                orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER_IDX));
    }

    // 캘린더 번호로 캘린더 찾기
    private Calendar findCalendarByCalendarIdx(Long calendarIdx) throws BaseException {
        return this.calendarRepository.findByCalendarIdxAndStatus(calendarIdx, Constant.ACTIVE_STATUS)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_CALENDAR_IDX));
    }

    // 태그 상태 변경
    private void changeCalendarTagStatus(Calendar calendar, String status){
        String getStatus = null;
        if(status.equals(ACTIVE_STATUS)) getStatus = INACTIVE_STATUS;
        else getStatus = ACTIVE_STATUS;

        this.calendarTagRepository.findByCalendarAndStatus(calendar, getStatus).stream()
                .forEach(tag -> {
                    tag.setStatus(status);
                    this.calendarTagRepository.save(tag);
                });
    }

    // tag 별 게시물 찾기
    private List<HomeTagRes> getPostByTag(List<PopularTagRes> popularTagRes) {
        return popularTagRes.stream()
                .map(tag -> new HomeTagRes(tag.getTagIdx(), tag.getTagName(),
                        this.calendarRepository.getTagByPostLimit5(tag.getTagIdx())))
                .collect(Collectors.toList());
    }

    // 캘린더 상태 변경 [삭제 시]
    private void changeCalendarStatus(Calendar calendar, String status) {
        calendar.setStatus(status);
        this.calendarRepository.save(calendar);
    }

    // tag 저장
    private void saveHashTags(Calendar calendar, CalendarHashTag hashtag) throws BaseException {
        this.calendarTagRepository.save(CalendarTag.builder()
                .tag(findByTagName(hashtag))
                .calendar(calendar)
                .build());
    }

}
