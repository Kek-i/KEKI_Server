package com.codepatissier.keki.calendar.entity;

import com.codepatissier.keki.calendar.CalendarCategory;
import com.codepatissier.keki.common.BaseEntity;
import com.codepatissier.keki.common.entityListener.CalendarEntityListener;
import com.codepatissier.keki.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
@SQLDelete(sql = "UPDATE calendar SET status = 'inactive', last_modified_date = current_timestamp WHERE calendar_idx = ?")
@EntityListeners(CalendarEntityListener.class)
public class Calendar extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calendarIdx;

    @ManyToOne
    @JoinColumn(nullable = false, name = "userIdx")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CalendarCategory calendarCategory;

    @Column(nullable = false, length = 300)
    private String calendarTitle;

    @NotNull
    @Column(length = 300)
    private LocalDate calendarDate;

    public void setCalendarCategory(CalendarCategory calendarCategory) {
        this.calendarCategory = calendarCategory;
    }

    public void setCalendarTitle(String calendarTitle) {
        this.calendarTitle = calendarTitle;
    }

    public void setCalendarDate(LocalDate calendarDate) {
        this.calendarDate = calendarDate;
    }
    @Builder
    public Calendar(User user, CalendarCategory calendarCategory, String calendarTitle, LocalDate calendarDate) {
        this.user = user;
        this.calendarCategory = calendarCategory;
        this.calendarTitle = calendarTitle;
        this.calendarDate = calendarDate;
    }

}
