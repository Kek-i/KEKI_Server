package com.codepatissier.keki.calendar.entity;

import com.codepatissier.keki.common.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class CalendarTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calendarTagIdx;

    @ManyToOne
    @JoinColumn(nullable = false, name = "calendarIdx")
    private Calendar calendar;

    @ManyToOne
    @JoinColumn(nullable = false, name = "tagIdx")
    private Tag tag;

    @Builder
    public CalendarTag(Calendar calendar, Tag tag) {
        this.calendar = calendar;
        this.tag = tag;
    }
}
