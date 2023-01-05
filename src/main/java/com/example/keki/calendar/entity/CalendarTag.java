package com.example.keki.calendar.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class CalendarTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calendarTagIdx;

}
