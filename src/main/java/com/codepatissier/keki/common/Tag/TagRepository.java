package com.codepatissier.keki.common.Tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTagName(String calendarHashTag);
    List<Tag> findByStatus(String activeStatus);
}
