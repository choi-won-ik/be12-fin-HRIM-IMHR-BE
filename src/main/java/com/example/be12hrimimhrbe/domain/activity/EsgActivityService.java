package com.example.be12hrimimhrbe.domain.activity;

import com.example.be12hrimimhrbe.domain.activity.model.EsgActivity;
import com.example.be12hrimimhrbe.domain.activity.model.EsgActivityDto;
import com.example.be12hrimimhrbe.domain.activitySubject.ActivitySubjectRepository;
import com.example.be12hrimimhrbe.domain.activitySubject.model.ActivitySubject;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.global.LocalImageService;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EsgActivityService {
    private final ActivitySubjectRepository subjectRepository;
    private final EsgActivityRepository activityRepository;
    private final LocalImageService localImageService;
    private final MemberRepository memberRepository;
    private final EsgActivityRepository esgActivityRepository;

    @Transactional
    public BaseResponse<String> createActivity(EsgActivityDto.ActivityRequest dto,
                                               CustomUserDetails member,
                                               List<MultipartFile> files) {
        ActivitySubject subject = subjectRepository.findById(dto.getSubjectId()).orElseThrow();

        for(ActivitySubject.input input : subject.getInputs()) {
            if(!input.getType().equals("file") && !dto.getInputs().containsKey(input.getInputValue())) {
                return new BaseResponse<>(BaseResponseMessage.ESG_ACTIVITY_INPUT_LEFT, "필수 입력값 누락: "+input.getInputValue());
            }
        }
        for(MultipartFile file : files) {
            String uploadFilePath = localImageService.upload(file);
            dto.getInputs().put(file.getName(), uploadFilePath);
        }
        activityRepository.save(dto.toEntity(member.getMember().getMemberId(),member.getUsername(), member.getMember().getIdx(), member.getMember().getCompany().getIdx()));
        return new BaseResponse<>(BaseResponseMessage.ESG_ACTIVITY_SUBMIT_SUCCESS, "활동 등록 완료");
    }

    @Transactional
    public BaseResponse<Page<EsgActivityDto.ActivityResponse>> listSearch(Member member, Long myIdx, Pageable pageable, String search) {
        Member nowmember = memberRepository.findByIdx(member.getIdx());

        if (!nowmember.getIsAdmin() && !nowmember.getIdx().equals(myIdx)) {
            return new BaseResponse<>(BaseResponseMessage.INAPPROPRIATE_MEMBER_ACCESS_RIGHTS_FAILS, null);
        }

        Page<EsgActivity> esgActivities = null;

        // 관리자
        if (nowmember.getIsAdmin()) {
            if (search != null && !search.isBlank()) {
                esgActivities = esgActivityRepository.findByCompanyIdxAndSubjectContainingIgnoreCase(nowmember.getCompany().getIdx(), search, pageable);
            } else {
                esgActivities = esgActivityRepository.findAllByCompanyIdx(nowmember.getCompany().getIdx(), pageable);
            }
        }

        // 개인
        if (!nowmember.getIsAdmin()) {
            if (search != null && !search.isBlank()) {
                esgActivities = esgActivityRepository.findByMemberIdxAndSubjectContainingIgnoreCase(member.getIdx(), search, pageable);
            } else {
                esgActivities = esgActivityRepository.findAllByMemberIdx(member.getIdx(), pageable);
            }
        }

        Page<EsgActivityDto.ActivityResponse> responses = esgActivities
                .map(EsgActivityDto.ActivityResponse::fromEntity);

        return new BaseResponse<>(BaseResponseMessage.ESG_ACTIVITY_LIST_SEARCH_SUCCESS, responses);
    }

    public BaseResponse<EsgActivity> detail(Member member, String id) {
        Long companyIdx = member.getCompany().getIdx();

        EsgActivity esgActivity = esgActivityRepository.findById(id);

        if (!companyIdx.equals(esgActivity.getCompanyIdx())) {
            return new BaseResponse<>(BaseResponseMessage.INAPPROPRIATE_MEMBER_ACCESS_RIGHTS_FAILS, null);
        }

        return new BaseResponse<>(BaseResponseMessage.ESG_ACTIVITY_DETAIL_SUCCESS, esgActivity);
    }
}
