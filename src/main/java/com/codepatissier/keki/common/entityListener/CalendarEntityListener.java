package com.codepatissier.keki.common.entityListener;

import com.codepatissier.keki.calendar.entity.Calendar;
import com.codepatissier.keki.calendar.repository.CalendarTag.CalendarTagRepository;
import com.codepatissier.keki.common.BeanUtils;

import javax.persistence.PreRemove;

public class CalendarEntityListener {

    @PreRemove
    public void onUpdate(Calendar calendar){
        CalendarTagRepository calendarTagRepository = BeanUtils.getBean(CalendarTagRepository.class);
        calendarTagRepository.deleteByCalendar(calendar);
    }
}
