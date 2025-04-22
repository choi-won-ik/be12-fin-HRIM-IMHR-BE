package com.example.be12hrimimhrbe.domain.partner;

import com.example.be12hrimimhrbe.domain.company.CompanyRepository;
import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.company.model.CompanyDto;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.score.ScoreRepository;
import com.example.be12hrimimhrbe.domain.score.model.Score;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.be12hrimimhrbe.domain.partner.model.Partner;
import com.example.be12hrimimhrbe.domain.partner.model.PartnerDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final ScoreRepository scoreRepository;

    public BaseResponse<Page<PartnerDto.PartnerListResp>> pageList(Member member, Long companyIdx, Pageable pageable) {
        String companyCode = companyRepository.findByIdx(companyIdx).getRegistrationNumber();
        String myCompanyCode = memberRepository.findByIdx(member.getIdx()).getCompany().getRegistrationNumber();

        if (!myCompanyCode.equals(companyCode)) {
            return new BaseResponse<>(BaseResponseMessage.PARTNER_LIST_FAILS, null);
        }

        Page<Partner> partners = partnerRepository.findAllByMainCompanyIdx(companyIdx, pageable);

        Page<PartnerDto.PartnerListResp> result = partners.map(partner -> {
            Company partnerCompany = partner.getPartnerCompany();
            Score score = scoreRepository.findByCompanyIdx(partnerCompany.getIdx()).stream()
                    .sorted(Comparator.comparing(Score::getYear).reversed()) // 최신 점수
                    .findFirst()
                    .orElse(new Score()); // 없으면 빈 Score 리턴

            return PartnerDto.PartnerListResp.fromEntity(partnerCompany, score);
        });

        return new BaseResponse<>(BaseResponseMessage.PARTNER_LIST_SUCCESS, result);
    }


    public BaseResponse<List<Partner>> addPartner(Member member, Long companyIdx, List<CompanyDto.CompanyListResponse> dtoList) {
        Long myCompanyIdx = memberRepository.findByIdx(member.getIdx()).getCompany().getIdx();
        boolean admin = memberRepository.findByIdx(member.getIdx()).getIsAdmin();

        if (!myCompanyIdx.equals(companyIdx) && !admin) {
            return new BaseResponse<>(BaseResponseMessage.PARTNER_ADD_FAILS, null);
        }

        Company myCompany = companyRepository.findByIdx(myCompanyIdx);
        if (myCompany == null) {
            return new BaseResponse<>(BaseResponseMessage.PARTNER_ADD_FAILS, null);
        }

        List<Long> partnerIds = dtoList.stream().map(CompanyDto.CompanyListResponse::getIdx).toList();

        List<Company> partners = partnerIds.stream().map(companyRepository::findByIdx).filter(Objects::nonNull).toList();

        // 중복 파트너 방지 (선택사항)
        Set<Long> existing = partnerRepository.findAllByMainCompany_Idx(myCompanyIdx)
                .stream().map(p -> p.getPartnerCompany().getIdx()).collect(Collectors.toSet());

        List<Partner> toSave = partners.stream()
                .filter(p -> !existing.contains(p.getIdx()))
                        .map(partner -> Partner.builder()
                                .mainCompany(myCompany)
                                .partnerCompany(partner)
                                .build())
                .collect(Collectors.toList());

        partnerRepository.saveAll(toSave);
        return new BaseResponse<>(BaseResponseMessage.PARTNER_ADD_SUCCESS, toSave);
    }

    @Transactional
    public boolean deletePartner(Member member, Long partnerIdx) {
        boolean admin = memberRepository.findByIdx(member.getIdx()).getIsAdmin();
        Long myCompanyIdx = memberRepository.findByIdx(member.getIdx()).getCompany().getIdx();
        Long mainCompanyIdx = partnerRepository.findByPartnerCompanyIdx(partnerIdx).getMainCompany().getIdx();

        if (!myCompanyIdx.equals(mainCompanyIdx) && !admin) {
            return false;
        }

        partnerRepository.deleteByPartnerCompanyIdxAndMainCompanyIdx(partnerIdx, myCompanyIdx);
        return true;
    }
}
