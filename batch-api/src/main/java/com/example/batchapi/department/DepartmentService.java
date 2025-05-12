package com.example.batchapi.department;

import com.example.batchapi.company.model.Company;
import com.example.batchapi.department.model.Department;
import com.example.batchapi.department.model.DepartmentScore;
import com.example.batchapi.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    public DepartmentScore score(Department item) {
        List<Member> members = item.getMembers();

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

        return DepartmentScore.builder()
                .company(item.getCompany())
                .department(item)
                .total(total/members.size())
                .environment(environment/members.size())
                .governance(governance/members.size())
                .social(social/members.size())
                .year(LocalDateTime.now().getYear())
                .month(LocalDateTime.now().getMonthValue())
                .build();
    }
}
