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
    Page<Event> findByCompanyIdx(Long companyIdx, Pageable pageable);

//  이벤트 월별, 일별 조회 쿼리
    List<Event> findByCompanyIdxAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Long companyIdx, LocalDate startDate, LocalDate endDate);

//  이벤트 수정 쿼리
    Event findByCompanyIdxAndIdx(Long companyIdx, Long idx);

//  이벤트 상세 조회와 삭제 쿼리
    Optional<Event> findByIdxAndCompanyIdx(Long idx, Long companyIdx);
}