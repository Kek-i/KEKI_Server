package com.codepatissier.keki.calendar.contoller;

import com.codepatissier.keki.calendar.dto.CalendarReq;
import com.codepatissier.keki.calendar.service.CalendarService;
import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.common.BaseResponseStatus;
import com.codepatissier.keki.user.service.AuthService;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
            if(calendarReq.getHashTags().size() < 1 || calendarReq.getHashTags() == null) return new BaseResponse<>(BaseResponseStatus.INVALID_POSTS_SIZE);
            if(calendarReq.getTitle().equals("") || calendarReq.getTitle() == null) return new BaseResponse<>(BaseResponseStatus.NULL_TITLE);
            // 캘린더 LocalDataFormat 에러가 나면 해결하는 api 생성 필요
            if(calendarReq.getDate() == null || calendarReq.getDate().toString().equals("")) return new BaseResponse<>(BaseResponseStatus.NULL_DATE);
            if(calendarReq.getKindOfCalendar() == null) return new BaseResponse<>(BaseResponseStatus.NULL_KIND_OF_CALENDARS);

            this.calendarService.createCalendar(this.authService.getUserIdx(), calendarReq);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 캘린더 삭제
    @ResponseBody
    @PatchMapping("/{calendarIdx}")
    public BaseResponse<String> deleteCalendar(@PathVariable("calendarIdx") Long calendarIdx){
        try{
            this.calendarService.deleteCalendar(calendarIdx, this.authService.getUserIdx());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
