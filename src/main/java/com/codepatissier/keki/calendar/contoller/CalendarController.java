package com.codepatissier.keki.calendar.contoller;

import com.codepatissier.keki.calendar.dto.*;
import com.codepatissier.keki.calendar.service.CalendarService;
import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.common.BaseResponseStatus;
import com.codepatissier.keki.common.Constant;
import com.codepatissier.keki.user.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO STATUS 처리를 해줘야 함.

@SecurityRequirement(name = "Bearer")
@Tag(name = "calendars", description = "기념일 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/calendars")
public class CalendarController {
    private final CalendarService calendarService;
    private final AuthService authService;

    // 캘린더 추가
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> createCalendar(@RequestBody CalendarReq calendarReq){
        try{
            if(calendarReq.getTitle().equals("") || calendarReq.getTitle() == null) return new BaseResponse<>(BaseResponseStatus.NULL_TITLE);
            if(calendarReq.getDate() == null || calendarReq.getDate().toString().equals("")) return new BaseResponse<>(BaseResponseStatus.NULL_DATE);
            if(calendarReq.getKindOfCalendar() == null) return new BaseResponse<>(BaseResponseStatus.NULL_KIND_OF_CALENDARS);

            this.calendarService.createCalendar(this.authService.getUserIdx(), calendarReq);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 캘린더 수정
    @ResponseBody
    @PatchMapping("/{calendarIdx}")
    public BaseResponse<String> modifyCalendar(@RequestBody CalendarReq calendarReq, @PathVariable("calendarIdx") Long calendarIdx){
        try{
            this.calendarService.modifyCalendar(this.authService.getUserIdx(), calendarReq, calendarIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 캘린더 수정 조회
    @ResponseBody
    @GetMapping("/{calendarIdx}/edit")
    public BaseResponse<CalendarEditRes> getEditCalendar(@PathVariable("calendarIdx") Long calendarIdx){
        try{
            return new BaseResponse<>(this.calendarService.getEditCalendar(this.authService.getUserIdx(), calendarIdx));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 캘린더 삭제
    @ResponseBody
    @DeleteMapping("/{calendarIdx}")
    public BaseResponse<String> deleteCalendar(@PathVariable("calendarIdx") Long calendarIdx){
        try{
            this.calendarService.deleteCalendar(calendarIdx, this.authService.getUserIdx());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 기념일 하나 조회
    @ResponseBody
    @GetMapping("/{calendarIdx}")
    public BaseResponse<CalendarRes> getCalendar(@PathVariable("calendarIdx") Long calendarIdx){
        try{
            return new BaseResponse<>(this.calendarService.getCalendar(calendarIdx, this.authService.getUserIdx()));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 기념일 리스트 조회
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<CalendarListRes>> getCalendarList(){
        try{
            return new BaseResponse<>(this.calendarService.getCalendarList(this.authService.getUserIdx()));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
    
    // 카테고리 조회
    @ResponseBody
    @GetMapping("/categories")
    public BaseResponse<List<TagRes>> getCategories(){
        try{
            return new BaseResponse<>(this.calendarService.getCategories());
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 홈 api
    @ResponseBody
    @GetMapping("/home")
    public BaseResponse<HomeRes> getHome(){
        try {
            if(this.authService.getUserIdxOptional().equals(Constant.Auth.ADMIN_USERIDX)){
                return new BaseResponse<>(this.calendarService.getHomeCalendarAndPostLogout());
            }
            // 아닌 경우에는 가장 가까운 기념일을 불러오고
            HomeRes home = this.calendarService.getHomeCalendar(this.authService.getUserIdx());
            // 기념일의 태그가 없으면 랜덤으로
            // 아니면 기념일 태그의 게시물만
            return new BaseResponse<>(this.calendarService.getHomeTagPost(home));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 태그 추가 api
    @ResponseBody
    @PostMapping("/categories")
    public BaseResponse<String> createTag(@RequestBody CalendarHashTag tag){
        try{
            if(tag.getCalendarHashTag() == null) return new BaseResponse<>(BaseResponseStatus.NULL_TAG);
            this.calendarService.createTag(tag);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 태그 active, inactive 처리 api
    @ResponseBody
    @PatchMapping("/categories")
    public BaseResponse<String> patchTag(@RequestBody TagStatus tag){
        try{
            if(tag.getCalendarHashTag() == null) return new BaseResponse<>(BaseResponseStatus.NULL_TAG);
            this.calendarService.patchTag(tag);

            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
