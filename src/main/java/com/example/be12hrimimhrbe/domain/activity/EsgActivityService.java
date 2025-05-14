package com.example.be12hrimimhrbe.domain.activity;

import com.example.be12hrimimhrbe.domain.activity.model.Activity;
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

    public BaseResponse<String> delete(Member member, String id) {
        EsgActivity esgActivity = esgActivityRepository.findById(id);

        if (!esgActivity.getMemberIdx().equals(member.getIdx())) {
            return new BaseResponse<>(BaseResponseMessage.INAPPROPRIATE_MEMBER_ACCESS_RIGHTS_FAILS, "접근 불가능한 권한입니다.");
        }

        activityRepository.delete(esgActivity);
        return new BaseResponse<>(BaseResponseMessage.ESG_ACTIVITY_DELETE_SUCCESS, "활동 삭제를 성공했습니다.");
    }

    @Transactional
    public BaseResponse<EsgActivity> detail(Member member, String id) {
        Long companyIdx = member.getCompany().getIdx();

        EsgActivity esgActivity = esgActivityRepository.findById(id);

        if (!companyIdx.equals(esgActivity.getCompanyIdx())) {
            return new BaseResponse<>(BaseResponseMessage.INAPPROPRIATE_MEMBER_ACCESS_RIGHTS_FAILS, null);
        }

        return new BaseResponse<>(BaseResponseMessage.ESG_ACTIVITY_DETAIL_SUCCESS, esgActivity);
    }

    @Transactional
    public BaseResponse<String> approvalAgree(Member member, String id) {
        EsgActivity esgActivity = esgActivityRepository.findById(id);
        Member activityMember = memberRepository.findByIdx(member.getIdx());

        if (!member.getIsAdmin()) {
            return new BaseResponse<>(BaseResponseMessage.INAPPROPRIATE_MEMBER_ACCESS_RIGHTS_FAILS, "접근 불가능한 권한입니다.");
        }

        if (esgActivity.getStatus() != null && !esgActivity.getStatus()) {
            return new BaseResponse<>(BaseResponseMessage.MY_ACTIVITY_PROCESSED, "이미 처리된 활동입니다.");
        }


        memberScoreAdd(esgActivity, activityMember);

        esgActivity.setStatus(true);
        esgActivityRepository.save(esgActivity);

        return new BaseResponse<>(BaseResponseMessage.ESG_ACTIVITY_APPROVAL_AGREE, "활등 승인이 완료되었습니다.");
    }

    @Transactional
    public BaseResponse<String> approvalOppose(Member member, String id) {
        EsgActivity esgActivity = esgActivityRepository.findById(id);

        if (!member.getIsAdmin()) {
            return new BaseResponse<>(BaseResponseMessage.INAPPROPRIATE_MEMBER_ACCESS_RIGHTS_FAILS, "접근 불가능한 권한입니다.");
        }

        if (esgActivity.getStatus() != null && esgActivity.getStatus()) {
            return new BaseResponse<>(BaseResponseMessage.MY_ACTIVITY_PROCESSED, "이미 처리된 활동입니다.");
        }

        System.out.println(esgActivity.getId());
        esgActivity.setStatus(false);
        esgActivityRepository.save(esgActivity);

        return new BaseResponse<>(BaseResponseMessage.ESG_ACTIVITY_APPROVAL_OPPOSE, "활동 반려가 완료되었습니다.");
    }

    @Transactional
    public void memberScoreAdd(EsgActivity esgActivity, Member member) {
        if(esgActivity.getEsgValue().equals("E")){
            member.setEScore(member.getEScore() + esgActivity.getEsgScore());
            memberRepository.save(member);
        }else if(esgActivity.getEsgValue().equals("S")){
            member.setSScore(member.getSScore() + esgActivity.getEsgScore());
            memberRepository.save(member);
        }else if(esgActivity.getEsgValue().equals("G")){
            member.setGScore(member.getGScore() + esgActivity.getEsgScore());
            memberRepository.save(member);
        }
    }
}
