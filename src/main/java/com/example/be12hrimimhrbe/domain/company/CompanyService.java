package com.example.be12hrimimhrbe.domain.company;

import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.partner.PartnerRepository;
import com.example.be12hrimimhrbe.domain.partner.model.Partner;
import com.example.be12hrimimhrbe.domain.partner.model.PartnerDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final ESG_CompanyRepository esgCompanyRepository;
    private final PartnerRepository partnerRepository;
    private final MemberRepository memberRepository;

    public BaseResponse<Page<PartnerDto.AllCompanyListResponse>> allList(Pageable pageable, Member member) {
        Long myCompanyIdx = memberRepository.findByIdx(member.getIdx()).getCompany().getIdx();
        List<Partner> registerPartners = partnerRepository.findByMainCompanyIdx(myCompanyIdx);

        Set<Long> registerCompanyIds = registerPartners.stream()
                .map(p -> p.getMainCompany().getIdx())
                .collect(Collectors.toSet());

        Set<Long> registerESGCompanyIds = registerPartners.stream()
                .map(p -> p.getMainCompany().getIdx())
                .collect(Collectors.toSet());

        // 테이블 필터링 조회
        List<PartnerDto.AllCompanyListResponse> companies = companyRepository.findAll().stream()
                .filter(c -> !registerCompanyIds.contains(c.getIdx()) && !c.getIdx().equals(myCompanyIdx)) // 내 회사도 제외
                .map(c -> PartnerDto.AllCompanyListResponse.builder()
                        .companyIdx(c.getIdx())
                        .type("main")
                        .companyName(c.getName())
                        .companyCode(c.getCode())
                        .build()
                ).collect(Collectors.toList());

        List<PartnerDto.AllCompanyListResponse> esgCompanies = esgCompanyRepository.findAll().stream()
                .filter(e -> !registerESGCompanyIds.contains(e.getIdx()))
                .map(e -> PartnerDto.AllCompanyListResponse.builder()
                        .companyIdx(e.getIdx())
                        .type("esg")
                        .companyName(e.getCompany_name())
                        .companyCode(e.getCompany_code())
                        .build())
                .collect(Collectors.toList());

        List<PartnerDto.AllCompanyListResponse> mergedList = new ArrayList<>();
        mergedList.addAll(companies);
        mergedList.addAll(esgCompanies);

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), mergedList.size());
        List<PartnerDto.AllCompanyListResponse> pagedList = mergedList.subList(start, end);

        Page<PartnerDto.AllCompanyListResponse> page = new PageImpl<>(pagedList, pageable, mergedList.size());

        return new BaseResponse<>(BaseResponseMessage.COMPANY_ALL_LIST_SUCCESS, page);
    }
}
