package com.example.be12hrimimhrbe.domain.partner;

import org.springframework.stereotype.Service;

import com.example.be12hrimimhrbe.domain.partner.model.Partner;
import com.example.be12hrimimhrbe.domain.partner.model.PartnerDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;

    public BaseResponse<List<PartnerDto.PartnerListResp>> getPartnerList(Long idx) {
        List<Partner> partners = partnerRepository.findAllByIdx(idx);
        return new BaseResponse<>(BaseResponseMessage.PARTNER_LIST_SUCCESS, 
            partners.stream().map(PartnerDto.PartnerListResp::fromEntity).collect(Collectors.toList())
        );
    }

}
