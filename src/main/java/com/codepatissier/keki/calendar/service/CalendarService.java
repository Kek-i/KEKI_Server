package com.codepatissier.keki.calendar.service;

import com.codepatissier.keki.calendar.CalendarCategory;
import com.codepatissier.keki.calendar.DateCountCategory;
import com.codepatissier.keki.calendar.dto.*;
import com.codepatissier.keki.calendar.entity.Calendar;
import com.codepatissier.keki.calendar.entity.CalendarTag;
import com.codepatissier.keki.calendar.repository.Calendar.CalendarRepository;
import com.codepatissier.keki.calendar.repository.CalendarTag.CalendarTagRepository;
import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponseStatus;
import com.codepatissier.keki.common.Constant;
import com.codepatissier.keki.common.tag.Tag;
import com.codepatissier.keki.common.tag.TagRepository;
import com.codepatissier.keki.user.entity.User;
import com.codepatissier.keki.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
            this.calendarRepository.delete(calendar);
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

    // 날짜 수 기념일 계산 (100일, 200일, 300일 등등)
    // 현재 2000일 미만으로 불러오도록 생성
    private CalendarDateReturn calculateDateForDateCount(Calendar cal) {
        int day = this.calculateDate(cal) - 1; // 날짜수를 구함 -> 기존에 +1 을 해서 return 하기 때문에 +1을 해줌
        for(DateCountCategory count: DateCountCategory.values()){
            if(day <= count.getCount()) return new CalendarDateReturn(count.getCount()-day, count);
        }
        return new CalendarDateReturn(day, null);
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

    // 카테고리 생성 api
    @Transactional(rollbackFor= Exception.class)
    public void createTag(CalendarHashTag tag) throws BaseException{
        try{
            this.tagRepository.save(Tag.builder()
                    .tagName(tag.getCalendarHashTag())
                    .build());
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }

    // 카테고리 상태 변경
    @Transactional(rollbackFor= Exception.class)
    public void patchTag(TagStatus tag) throws BaseException{
        Tag findTag = this.tagRepository.findByTagName(tag.getCalendarHashTag())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_TAG));
        try{
            if(tag.getStatus()) findTag.setStatus(ACTIVE_STATUS);
            else findTag.setStatus(INACTIVE_STATUS);
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    // 홈 기념일 조회
    public HomeRes getHomeCalendar(Long userIdx) throws BaseException{
        User user = this.findUserByUserIdx(userIdx);
        try{
            int day = Integer.MAX_VALUE;
            Calendar calendar = this.calendarRepository.getRecentDateCalendar(user); // 현재 가장 가까운 캘린더 불러오기
            if(calendar != null){
                day = this.calculateDate(calendar);
            }
            List<Calendar> listEYCalendars = this.calendarRepository.findByUserAndCalendarCategoryAndStatus(user, CalendarCategory.EVERY_YEAR, ACTIVE_STATUS);


            // 사용자의 매년 반복 캘린더 불러와서 하나씩 비교해보고, 값이 더 가까우면? 매년 반복으로 홈 화면 기념일 불러오기
            for(Calendar cal: listEYCalendars){
                int calDay = this.calculateDate(cal);
                if(calDay < Math.abs(day)){
                    calendar = cal;
                    day = calDay;
                }
            }

            // 날짜 수인 경우 100일, 200일 불러오기
            List<Calendar> listDCCalendars = this.calendarRepository.findByUserAndCalendarCategoryAndStatus(user, CalendarCategory.DATE_COUNT, ACTIVE_STATUS);
            for (Calendar cal : listDCCalendars) {
                CalendarDateReturn calDateReturn = this.calculateDateForDateCount(cal);
                if (calDateReturn.getCalDate()<Math.abs(day) && calDateReturn.getDateCountCategory() != null) {
                    calendar = cal;
                    calendar.setCalendarTitle(calendar.getCalendarTitle()+"의 " + calDateReturn.getDateCountCategory().getDate());
                    day = calDateReturn.getCalDate();
                }
            }

            if(calendar != null){
                return new HomeRes(user.getUserIdx(), user.getNickname(), calendar.getCalendarTitle(), Math.abs(day), null, calendar);
            }
            return new HomeRes(user.getUserIdx(), user.getNickname(), null, 0, null, null);
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }



    // 로그 아웃 된 상태에서 홈 정보 불러오기
    public HomeRes getHomeCalendarAndPostLogout() throws BaseException{
        try{
            return new HomeRes(null, null, null, 0,
                    getPostByTag(this.calendarTagRepository.getPopularCalendarTag()),null);
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }


    // home api
    public HomeRes getHomeTagPost(HomeRes home) throws BaseException{
        try{
            if(home.getCalendar() == null) home.setHomeTagResList(this.getPostByTag(this.calendarTagRepository.getPopularCalendarTag()));
            else{
                List<PopularTagRes> tags = this.calendarTagRepository.findByCalendarAndStatus(home.getCalendar(), ACTIVE_STATUS).stream()
                        .map(cal -> new PopularTagRes(cal.getTag().getTagIdx(), cal.getTag().getTagName())).collect(Collectors.toList());
                // 기념일의 태그가 3개 미만이면 다 랜덤으로 불러오고
                if(tags.size()==0){
                    home.setHomeTagResList(this.getPostByTag(this.calendarTagRepository.getPopularCalendarTag()));
                }else{ // 태그가 3개 이상이면 태그별로 랜덤하게 불러오기
                    home.setHomeTagResList(this.getPostByTag(tags));
                }
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
            this.calendarTagRepository.deleteByCalendar(calendar);
            if (calendarReq.getHashTags() != null && calendarReq.getHashTags().size() != 0) {
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
    public CalendarEditRes getEditCalendar(Long userIdx, Long calendarIdx) throws BaseException{
        CalendarRes calendar = this.getCalendar(calendarIdx, userIdx);
        return new CalendarEditRes(calendar.getKindOfCalendar(), calendar.getTitle(), calendar.getDate(),
                calendar.getHashTags(), this.tagRepository.findByStatus(ACTIVE_STATUS).stream()
                .map(tag -> new CalendarHashTag(tag.getTagName())).collect(Collectors.toList()));
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

    // tag 별 게시물 찾기
    private List<HomeTagRes> getPostByTag(List<PopularTagRes> popularTagRes) {
        return popularTagRes.stream()
                .map(tag -> new HomeTagRes(tag.getTagIdx(), tag.getTagName(),
                        this.calendarRepository.getTagByPostLimit5(tag.getTagIdx())))
                .collect(Collectors.toList());
    }

    // tag 저장
    private void saveHashTags(Calendar calendar, CalendarHashTag hashtag) throws BaseException {
        this.calendarTagRepository.save(CalendarTag.builder()
                .tag(findByTagName(hashtag))
                .calendar(calendar)
                .build());
    }



}
