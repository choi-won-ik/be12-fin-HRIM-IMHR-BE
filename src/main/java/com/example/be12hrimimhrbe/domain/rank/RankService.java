package com.example.be12hrimimhrbe.domain.rank;

import com.example.be12hrimimhrbe.domain.company.CompanyRepository;
import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.department.DepartmentRepository;
import com.example.be12hrimimhrbe.domain.department.DepartmentScoreRepository;
import com.example.be12hrimimhrbe.domain.department.model.Department;
import com.example.be12hrimimhrbe.domain.department.model.DepartmentScore;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.rank.model.Rank;
import com.example.be12hrimimhrbe.domain.rank.model.RankDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankService {
    private final RankRepository rankRepository;
    private final CompanyRepository companyRepository;
    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;
    private final DepartmentScoreRepository departmentScoreRepository;

    @Transactional
    public void batch1() {
        List<Company> companies = companyRepository.findAll();
        List<RankDto.BatchRankResp> list = new ArrayList<>();
        for (Company company : companies) {
            List<Member> members = company.getMembers();
            for (Member member : members) {
                list.add(RankDto.BatchRankResp.builder()
                        .average((member.getEScore()+member.getSScore()+ member.getGScore())/3)
                        .member(member)
                        .build());
            }
            list.sort(Comparator.comparing(RankDto.BatchRankResp::getAverage).reversed());
            for (int i=0; i<3; i++){
                Rank rank=Rank.builder()
                        .member(list.get(i).getMember())
                        .company(company)
                        .average(list.get(i).getAverage())
                        .ranking(i+1)
                        .year(LocalDateTime.now().getYear())
                        .month(LocalDateTime.now().getMonthValue())
                        .build();

                rankRepository.save(rank);
            }
        }
    }

    @Transactional
    public void batch2() {
        List<Member> members=memberRepository.findAll();

        for (Member member : members) {
            member.setEScore(0);
            member.setSScore(0);
            member.setGScore(0);

            memberRepository.save(member);
        }
    }

    public void batch() {
        List<Department> departments=departmentRepository.findAll();

        for (Department department : departments) {
            List<Member> members = department.getMembers();

            int environment=0;
            int social=0;
            int governance= 0;
            int total=0;
            for (Member member : members) {
                int average=(member.getEScore()+member.getSScore()+member.getGScore())/3;
                environment+=member.getEScore();
                social+=member.getSScore();
                governance+=member.getGScore();
                total+=average;
            }

            if(!members.isEmpty()){
                DepartmentScore departmentScore = DepartmentScore.builder()
                        .company(department.getCompany())
                        .department(department)
                        .total(total/members.size())
                        .environment(environment/members.size())
                        .governance(governance/members.size())
                        .social(social/members.size())
                        .year(LocalDateTime.now().getYear())
                        .month(LocalDateTime.now().getMonthValue())
                        .build();

                departmentScoreRepository.save(departmentScore);
            }

        }
    }
}
