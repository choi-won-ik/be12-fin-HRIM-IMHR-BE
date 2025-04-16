package com.example.be12hrimimhrbe.domain.event;

import com.example.be12hrimimhrbe.domain.event.model.Event;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByIdxAndCompanyIdx(Long idx, Long companyIdx);
    List<Event> findByCompanyIdxAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Long companyIdx, LocalDate startDate, LocalDate endDate);

    Event findByCompanyIdxAndIdx(Long companyIdx, Long idx);
}