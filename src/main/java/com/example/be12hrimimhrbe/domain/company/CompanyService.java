package com.example.be12hrimimhrbe.domain.company;

import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.company.model.CompanyDto;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.partner.PartnerRepository;
import com.example.be12hrimimhrbe.domain.partner.model.Partner;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final PartnerRepository partnerRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public BaseResponse<Page<CompanyDto.CompanyListResponse>> allList(Pageable pageable, Member member) {
        Long myCompanyIdx = memberRepository.findByIdx(member.getIdx()).getCompany().getIdx();

        // 등록한 협력사들
        List<Partner> partners = partnerRepository.findAllByMainCompany_Idx(myCompanyIdx);

        Set<Long> registerCompanyIds = partners.stream()
                .map(p -> p.getPartnerCompany().getIdx())
                .collect(Collectors.toSet());

        registerCompanyIds.add(myCompanyIdx);
        List<Long> excludedIds = new ArrayList<>(registerCompanyIds);

        Page<Company> pagedResult = companyRepository.findAllExcludingIds(excludedIds, pageable);

        // DTO 변환
        Page<CompanyDto.CompanyListResponse> resultPage = pagedResult.map(CompanyDto.CompanyListResponse::of);

        return new BaseResponse<>(BaseResponseMessage.COMPANY_ALL_LIST_SUCCESS, resultPage);
    }
}
