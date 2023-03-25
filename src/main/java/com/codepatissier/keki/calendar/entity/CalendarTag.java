package com.codepatissier.keki.calendar.entity;

import com.codepatissier.keki.common.BaseEntity;
import com.codepatissier.keki.common.tag.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
@SQLDelete(sql = "UPDATE calendar_tag SET status = 'inactive', last_modified_date = current_timestamp WHERE calendar_tag_idx = ?")
public class CalendarTag extends BaseEntity {
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
