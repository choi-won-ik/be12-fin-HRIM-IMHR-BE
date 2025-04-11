package com.example.be12hrimimhrbe.domain.event;

import com.example.be12hrimimhrbe.domain.event.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByCompanyIdx(Long companyIdx, Pageable pageable);
}
