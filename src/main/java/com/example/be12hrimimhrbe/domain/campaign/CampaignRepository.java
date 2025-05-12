package com.example.be12hrimimhrbe.domain.campaign;

import com.example.be12hrimimhrbe.domain.campaign.model.Campaign;
import com.example.be12hrimimhrbe.domain.event.model.Event;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findAllByMember(Member member);
    Boolean existsByEventAndMember(Event event, Member member);
    List<Campaign> findByEventIdx(Long eventIdx);

    void deleteByEventIdxAndMember_IdxIn(Long eventIdx, Set<Long> memberIdxes);

    void deleteByEventIdx(Long idx);

    @EntityGraph(attributePaths = {"member","event","member.company"})
    @Query("SELECT c FROM Campaign c " +
            "LEFT JOIN c.member m " +
            "LEFT JOIN c.event e " +
            "LEFT JOIN m.company c2 " +
            "where e.idx=:eventIdx ")
    List<Campaign> findAllByEventIdx(Long eventIdx);
}