package com.codepatissier.keki.calendar.entity;

import com.codepatissier.keki.calendar.CalendarCategory;
import com.codepatissier.keki.common.BaseEntity;
import com.codepatissier.keki.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class Calendar extends BaseEntity {
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
    private LocalDate calendarDate;

    @Builder
    public Calendar(User user, CalendarCategory calendarCategory, String calendarTitle, LocalDate calendarDate) {
        this.user = user;
        this.calendarCategory = calendarCategory;
        this.calendarTitle = calendarTitle;
        this.calendarDate = calendarDate;
    }
}
