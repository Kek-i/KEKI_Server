package com.example.keki.calendar.entity;

import com.example.keki.calendar.CalendarCategory;
import com.example.keki.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int calendarIdx;

    @ManyToOne
    @JoinColumn(nullable = false, name = "userIdx")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CalendarCategory calendarCategory;

    @Column(nullable = false, length = 300)
    private String calendarTitle;

    @Column(nullable = false, length = 300)
    private Date calendarDate;

    @Builder
    public Calendar(User user, CalendarCategory calendarCategory, String calendarTitle, Date calendarDate) {
        this.user = user;
        this.calendarCategory = calendarCategory;
        this.calendarTitle = calendarTitle;
        this.calendarDate = calendarDate;
    }
}
