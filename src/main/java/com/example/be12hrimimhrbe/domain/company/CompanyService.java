package com.example.be12hrimimhrbe.domain.company;

import com.example.be12hrimimhrbe.domain.activity.ActivityRepository;
import com.example.be12hrimimhrbe.domain.activity.model.Activity;
import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.company.model.CompanyDto;
import com.example.be12hrimimhrbe.domain.department.DepartmentRepository;
import com.example.be12hrimimhrbe.domain.department.model.Department;
import com.example.be12hrimimhrbe.domain.department.model.DepartmentDto;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.member.model.MemberDto;
import com.example.be12hrimimhrbe.domain.partner.PartnerRepository;
import com.example.be12hrimimhrbe.domain.partner.model.Partner;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final DepartmentRepository departmentRepository;
    private final ActivityRepository activityRepository;
    private final CompanyRepository companyRepository;
    private final PartnerRepository partnerRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public BaseResponse<Page<CompanyDto.CompanyListResponse>> allList(Pageable pageable, Member member, String keyword) {
        Long myCompanyIdx = memberRepository.findByIdx(member.getIdx()).getCompany().getIdx();

        // 등록한 협력사들
        List<Partner> partners = partnerRepository.findAllByMainCompany_Idx(myCompanyIdx);

        Set<Long> registerCompanyIds = partners.stream()
                .map(p -> p.getPartnerCompany().getIdx())
                .collect(Collectors.toSet());

        registerCompanyIds.add(myCompanyIdx);
        List<Long> excludedIds = new ArrayList<>(registerCompanyIds);

        Page<Company> pagedResult;

        if (keyword != null && !keyword.isBlank()) {
            pagedResult = companyRepository.findByNameContainingIgnoreCaseAndIdxNotIn(keyword, excludedIds, pageable);
        } else {
            pagedResult = companyRepository.findAllExcludingIds(excludedIds, pageable);
        }

        // DTO 변환
        Page<CompanyDto.CompanyListResponse> resultPage = pagedResult.map(CompanyDto.CompanyListResponse::of);

        return new BaseResponse<>(BaseResponseMessage.COMPANY_ALL_LIST_SUCCESS, resultPage);
    }

    // 내 회사의 월별 부서들의 esg 현황 조회 기능
    @Transactional
    public BaseResponse<CompanyDto.CompanyYearResponse> monthDashboard(Member member, int year, int month) {
        Long myCompanyIdx = memberRepository.findByIdx(member.getIdx()).getCompany().getIdx();
        Company company = companyRepository.findByIdx(myCompanyIdx);

        // 1. 기업에 해당하는 모든 사원 Idx 조회
        List<Long> companyMemberIdxs = memberRepository.findAllByCompanyIdx(myCompanyIdx)
                .stream().map(m -> m.getIdx()).collect(Collectors.toList());

        // 2. 모든 사원 ID -> 사원 객체 Map (이름 등 매핑용)
        Map<Long, Member> memberMap = memberRepository.findAllByCompanyIdx(myCompanyIdx).stream()
                .collect(Collectors.toMap(Member::getIdx, m -> m));

        // 3. 모든 사원 활동 필터링 및 점수 계산
        Map<Long, int[]> memberScoreMap = companyMemberIdxs.stream()
                .flatMap(idx -> activityRepository.findAllByMemberIdx(idx).stream())
                .filter(a -> a.getCreatedAt().getYear() == year && a.getCreatedAt().getMonthValue() == month)
                .collect(Collectors.groupingBy(
                        a -> a.getMember().getIdx(),
                        Collectors.reducing(
                                new int[3], // [E, S, G]
                                a -> {
                                    int[] score = new int[3];
                                    if (a.getType() != null) {
                                        switch (a.getType()) {
                                            case VOLUNTEER, DONATION -> score[0] = a.getScore(); // E
                                        }
                                    }
                                    if (a.getEducationType() != null) {
                                        switch (a.getEducationType()) {
                                            case ENVIRONMENTAL_EDUCATION -> score[0] = a.getScore(); // E
                                            case SOCIAL_EDUCATION -> score[1] = a.getScore(); // S
                                            case GOVERNANCE_EDUCATION -> score[2] = a.getScore(); // G
                                        }
                                    }
                                    return score;
                                },
                                (a1, a2) -> new int[]{
                                        a1[0] + a2[0],
                                        a1[1] + a2[1],
                                        a1[2] + a2[2]
                                }
                        )
                ));

        //4. 총합 기준 정렬 후 상위 3명 뽑기
        List<MemberDto.MemberScoreResponse> top3 = memberScoreMap.entrySet().stream()
                .sorted((e1, e2) -> {
                    int total1 = Arrays.stream(e1.getValue()).sum();
                    int total2 = Arrays.stream(e2.getValue()).sum();
                    return Integer.compare(total2, total1); // 내림차순
                })
                .limit(3)
                .map(entry -> {
                    Long memberId = entry.getKey();
                    int[] scoreArr = entry.getValue();
                    Member member1 = memberMap.get(memberId);
                    return MemberDto.MemberScoreResponse.from(member1, scoreArr[0], scoreArr[1], scoreArr[2]);
                })
                .collect(Collectors.toList());

//        // 5. 회사에 속한 부서들 조회 및 부서 Idx -> 부서 객체 Map
//        Map<Long, Department> departmentMap = departmentRepository.findAllByCompany(company)
//                .stream().collect(Collectors.toMap(Department::getIdx, d -> d));
//
//
//        // 7. 각 부서의 사원 별 각 점수를  계산
//        List<DepartmentDto.DepartmentScoreResponse> departments = departmentMap.entrySet().stream()
//                .map(entry -> {
//                    Long deptIdx = entry.getKey();
//                    Department d = entry.getValue();
//
//
//                    // 7 - 1 해당 부서 소속 사원 목록 조회
//                    List<Member> dMembers = memberMap.values().stream()
//                            .filter(m -> m.getDepartment() != null && m.getDepartment().getIdx().equals(deptIdx))
//                            .collect(Collectors.toList());
//
//                    // 사원수
//                    int memberCount = dMembers.size();
//
//                    // E, S, G 점수 총합
//                    int totalE = 0;
//                    int totalS = 0;
//                    int totalG = 0;
//
//                    for (Member m : dMembers) {
//                        List<Activity> activities = activityRepository.findAllByMember(m).stream()
//                                .filter(a -> a.getCreatedAt().getYear() == year &&
//                                        a.getCreatedAt().getMonthValue() == month)
//                                .collect(Collectors.toList());
//
//                        // 활동 점수 계산
//                        for (Activity a : activities) {
//                            switch (a.getType()) {
//                                case VOLUNTEER, DONATION -> totalE += a.getScore();
//                            }
//
//                            switch (a.getEducationType()) {
//                                case ENVIRONMENTAL_EDUCATION -> totalE += a.getScore();
//                                case SOCIAL_EDUCATION -> totalS += a.getScore();
//                                case GOVERNANCE_EDUCATION -> totalG += a.getScore();
//                            }
//                        }
//                    }
//
//                    // 평균 계산 (0으로 나눔 방지)
//                    double avgE = memberCount > 0 ? (double) totalE / memberCount : 0.0;
//                    double avgS = memberCount > 0 ? (double) totalS / memberCount : 0.0;
//                    double avgG = memberCount > 0 ? (double) totalG / memberCount : 0.0;
//
//                    // 반환
//                    return DepartmentDto.DepartmentScoreResponse.fromEntity(d, avgE, avgS, avgG);
//                })
//                .collect(Collectors.toList());


        // Dto 변환
        CompanyDto.CompanyYearResponse response = CompanyDto.CompanyYearResponse.of(company, top3);
        return new BaseResponse<>(BaseResponseMessage.COMPANY_DEPARTMENT_MONTH_SUCCESS, response);
    }
}
