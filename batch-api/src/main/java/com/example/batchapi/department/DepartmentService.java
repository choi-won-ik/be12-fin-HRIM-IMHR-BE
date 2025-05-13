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

        int nowYear = LocalDateTime.now().getYear();
        int nowMonth = LocalDateTime.now().getMonthValue();

        // 1월이면 작년 12월로 보정
        if (nowMonth == 1) {
            nowMonth = 12;
            nowYear -= 1;
        } else {
            nowMonth -= 1;
        }

        int size = members.size();
        int safeSize = size == 0 ? 1 : size;

        return DepartmentScore.builder()
                .company(item.getCompany())
                .department(item)
                .total(size == 0 ? 0 : total / safeSize)
                .environment(size == 0 ? 0 : environment / safeSize)
                .governance(size == 0 ? 0 : governance / safeSize)
                .social(size == 0 ? 0 : social / safeSize)
                .year(nowYear)
                .month(nowMonth)
                .build();
    }
}
