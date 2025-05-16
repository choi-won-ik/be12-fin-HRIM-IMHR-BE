package com.example.be12hrimimhrbe.domain.company;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.company.model.CompanyDto;
import com.example.be12hrimimhrbe.domain.department.DepartmentRepository;
import com.example.be12hrimimhrbe.domain.department.DepartmentScoreRepository;
import com.example.be12hrimimhrbe.domain.department.model.Department;
import com.example.be12hrimimhrbe.domain.department.model.DepartmentDto;
import com.example.be12hrimimhrbe.domain.department.model.DepartmentScore;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.member.model.MemberDto;
import com.example.be12hrimimhrbe.domain.partner.PartnerRepository;
import com.example.be12hrimimhrbe.domain.partner.model.Partner;
import com.example.be12hrimimhrbe.domain.rank.RankRepository;
import com.example.be12hrimimhrbe.domain.rank.model.Rank;
import com.example.be12hrimimhrbe.domain.score.ScoreRepository;
import com.example.be12hrimimhrbe.domain.score.model.Score;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final PartnerRepository partnerRepository;
    private final MemberRepository memberRepository;
    private final ScoreRepository scoreRepository;
    private final RankRepository rankRepository;
    private final DepartmentRepository departmentRepository;
    private final DepartmentScoreRepository departmentScoreRepository;

    public BaseResponse<CompanyDto.CompanyResponse> fetchMyCompany(Member member) {
        Company myCompany = companyRepository.findByIdx(member.getCompany().getIdx());
        return new BaseResponse<>(BaseResponseMessage.COMPANY_MY_COMPANY_SEARCH_SUCCESS, CompanyDto.CompanyResponse.of(myCompany));
    }

    public BaseResponse<String> updateScore(Member member, CompanyDto.CompanyResponse dto) {
        Long myCompanyIdx = member.getCompany().getIdx();

        if (!myCompanyIdx.equals(dto.getIdx())) {
            return new BaseResponse<>(BaseResponseMessage.INAPPROPRIATE_MEMBER_ACCESS_RIGHTS_FAILS, null);
        }
        if (!member.getIsAdmin()) {
            return new BaseResponse<>(BaseResponseMessage.INAPPROPRIATE_MEMBER_ACCESS_RIGHTS_FAILS, null);
        }

        Company company = companyRepository.findByIdx(dto.getIdx());

        company.setTargetEScore(dto.getTargetEScore());
        company.setTargetSScore(dto.getTargetSScore());
        company.setTargetGScore(dto.getTargetGScore());
        companyRepository.save(company);

        return new BaseResponse<>(BaseResponseMessage.COMPANY_SCORE_UPDATE_SUCCESS, "기업 목표 점수가 성공적으로 수정되었습니다.");
    }

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
        //  가장 최신 점수 조회
        Page<CompanyDto.CompanyListResponse> resultPage = pagedResult.map( c -> {
            Score score = scoreRepository.findByCompanyIdx(c.getIdx()).stream()
                    .sorted(Comparator.comparing(Score::getYear).reversed())
                    .findFirst()
                    .orElse(new Score());
            return CompanyDto.CompanyListResponse.of(c, score);
        });

        return new BaseResponse<>(BaseResponseMessage.COMPANY_ALL_LIST_SUCCESS, resultPage);
    }

//    // 하나의 APi만 호출할때에 주석처리 해야함
//    // 내 회사의 월별 부서들의 esg 현황 조회 기능
    @Transactional
    public BaseResponse<CompanyDto.CompanyYearResponse> monthDashboard(Member member, int year, int month) {

        List<Rank> list = rankRepository.findByCompanyIdx(member.getCompany().getIdx(), year, month)
                .stream()
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));

        // 1~3등
        List<MemberDto.MemberScoreResponse> top3 = new ArrayList<>();
        List<Rank> ranks = new ArrayList<>();

        list.sort(Comparator.comparing(Rank::getAverage).reversed());
        for (Rank r : list) {
            if(r.getYear() == year && r.getMonth() == month) {
                ranks.add(r);
            }
        }

        for (int i = 0; i < 3; i++) {
            if (ranks.get(i).getMember() == null) continue;
            top3.add(MemberDto.MemberScoreResponse.builder()
                    .memberIdx(ranks.get(i).getMember().getIdx())
                    .memberName(ranks.get(i).getMember().getName())
                    .ranking(i+1)
                    .averageScore(ranks.get(i).getAverage())
                    .build());

        }

        List<Department> departments = departmentRepository.findAllByCompany(member.getCompany());
        List<Object[]> result = departmentRepository.findAllDepartmentsWithScore(departments , year, month);

        List<DepartmentDto.SimpleDepartmentDto> resp = result.stream()
                .map(arr -> {
                    Department d = (Department) arr[0];
                    DepartmentScore ds = (DepartmentScore) arr[1];

                    return DepartmentDto.SimpleDepartmentDto.builder()
                            .idx(d.getIdx())
                            .name(d.getName())
                            .eScore(ds != null && ds.getEnvironment() != null ? ds.getEnvironment() : 0)
                            .gScore(ds != null && ds.getGovernance() != null ? ds.getGovernance() : 0)
                            .sScore(ds != null && ds.getSocial() != null ? ds.getSocial() : 0)
                            .build();
                }).collect(Collectors.toList());


        CompanyDto.CompanyYearResponse response = CompanyDto.CompanyYearResponse.builder()
                .companyName(ranks.get(0).getCompany().getName())
                .idx(ranks.get(0).getCompany().getIdx())
                .memberScores(top3)
                .departments(resp)
                .build();

        return new BaseResponse<>(BaseResponseMessage.COMPANY_DEPARTMENT_MONTH_SUCCESS, response);
    }
}
