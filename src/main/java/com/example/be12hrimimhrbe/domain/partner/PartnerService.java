package com.example.be12hrimimhrbe.domain.partner;

import com.example.be12hrimimhrbe.domain.company.CompanyRepository;
import com.example.be12hrimimhrbe.domain.company.model.Company;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

        Page<Partner> partners = partnerRepository.findAllByMainCompanyId(companyIdx, pageable);

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


    public BaseResponse<List<PartnerDto.PartnerRequest>> addPartner(Member member, Long companyIdx, List<PartnerDto.PartnerRequest> dtoList) {
        Long myCompanyIdx = memberRepository.findByIdx(member.getIdx()).getCompany().getIdx();

        if (!myCompanyIdx.equals(companyIdx)) {
            return new BaseResponse<>(BaseResponseMessage.PARTNER_ADD_FAILS, null);
        }

        Company myCompany = companyRepository.findByIdx(myCompanyIdx);

        List<Partner> partners = new ArrayList<>();
        List<PartnerDto.PartnerRequest> successList = new ArrayList<>();

        for (PartnerDto.PartnerRequest dto : dtoList) {
            boolean isExist = partnerRepository.existsByMainCompanyIdxAndPartnerIdx(
                    myCompanyIdx, dto.getPartnerIdx());
            if (isExist) continue;

            if (dto.getType().equals("company")) {
                Partner partner = Partner.builder()
                        .partnerIdx(dto.getPartnerIdx())
                        .name(dto.getName())
                        .companyCode(dto.getCompanyCode())
                        .mainCompany(myCompany)
                        .type(dto.getType())
                        .build();

                partners.add(partner);
                successList.add(dto);
            } else if (dto.getType().equals("esg")) {
                ESGCompany esgCompany = esgCompanyRepository.findByIdx(dto.getPartnerIdx());
                Partner partner = Partner.builder()
                        .partnerIdx(dto.getPartnerIdx())
                        .name(dto.getName())
                        .companyCode(dto.getCompanyCode())
                        .type(dto.getType())
                        .mainCompany(myCompany)
                        .esgCompany(esgCompany)
                        .build();
                partners.add(partner);
                successList.add(dto);
            }
        }
        partnerRepository.saveAll(partners);
        return new BaseResponse<>(BaseResponseMessage.PARTNER_ADD_SUCCESS, successList);
    }

    @Transactional
    public boolean deletePartner(Member member, Long partnerIdx) {
        Long myCompanyIdx = memberRepository.findByIdx(member.getIdx()).getCompany().getIdx();
        Long mainCompanyIdx = partnerRepository.findMainCompanyIdxByPartnerIdx(partnerIdx);

        if (!myCompanyIdx.equals(mainCompanyIdx)) {
            return false;
        }

        partnerRepository.deletePartnerByIdx(partnerIdx);
        partnerRepository.deleteById(partnerIdx);
        return true;
    }
}
