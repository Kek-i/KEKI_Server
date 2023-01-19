package com.codepatissier.keki.calendar.contoller;

import com.codepatissier.keki.calendar.dto.CalendarReq;
import com.codepatissier.keki.calendar.service.CalendarService;
import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.common.BaseResponse;
import com.codepatissier.keki.common.BaseResponseStatus;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Bearer")
@Tag(name = "calendars", description = "기념일 API")
@RestController
@RequestMapping(value = "/calendars")
public class CalendarController {
    private CalendarService calendarService;
    public CalendarController(CalendarService calendarService){
        this.calendarService = calendarService;
    }

    /**
     * token 변경후에 pathVariable 삭제 할 예정이에요!
     */
    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<String> createCalendar(@PathVariable("userIdx") Long userIdx, @RequestBody CalendarReq calendarReq){
        try{
            if(calendarReq.getHashTags().size() < 1 || calendarReq.getHashTags() == null) return new BaseResponse<>(BaseResponseStatus.INVALID_POSTS_SIZE);
            if(calendarReq.getTitle().equals("") || calendarReq.getTitle() == null) return new BaseResponse<>(BaseResponseStatus.NULL_TITLE);
            // 캘린더 LocalDataFormat 에러가 나면 해결하는 api 생성 필요
            if(calendarReq.getDate() == null || calendarReq.getDate().toString().equals("")) return new BaseResponse<>(BaseResponseStatus.NULL_DATE);
            if(calendarReq.getKindOfCalendar() == null) return new BaseResponse<>(BaseResponseStatus.NULL_KIND_OF_CALENDARS);

            this.calendarService.createCalendar(userIdx, calendarReq);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * token 변경후에 pathVariable 삭제 할 예정이에요!
     */
    @ResponseBody
    @PatchMapping("/{calendarIdx}/users/{userIdx}")
    public BaseResponse<String> deleteCalendar(@PathVariable("calendarIdx") Long calendarIdx, @PathVariable("userIdx") Long userIdx){
        try{
            this.calendarService.deleteCalendar(calendarIdx, userIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
