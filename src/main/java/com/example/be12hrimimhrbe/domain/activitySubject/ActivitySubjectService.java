package com.example.be12hrimimhrbe.domain.activitySubject;

import com.example.be12hrimimhrbe.domain.activitySubject.model.ActivitySubject;
import com.example.be12hrimimhrbe.domain.activitySubject.model.ActivitySubjectDto;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivitySubjectService {
    private final ActivitySubjectRepository activitySubjectRepository;
    private final MemberRepository memberRepository;

    public BaseResponse<String> create(List<ActivitySubjectDto.ActivitySubjectRequest> dto, Member member) {
        Member nowmember = memberRepository.findByIdx(member.getIdx());

        if (nowmember == null) {
            return new BaseResponse<>(BaseResponseMessage.MEMBER_SEARCH_NOT_FOUND, null);
        }

        Long companyIdx = nowmember.getCompany().getIdx();

        List<ActivitySubject> activitySubjects = new ArrayList<>();

        for (ActivitySubjectDto.ActivitySubjectRequest d : dto) {
            ActivitySubject activitySubject = ActivitySubject.builder()
                    .companyIdx(companyIdx)
                    .subject(d.getSubject())
                    .esgValue(d.getEsgValue())
                    .esgScore(d.getEsgScore())
                    .esgActivityItem(d.getEsgActivityItem())
                    .evaluationCriteria(d.getEvaluationCriteria())
                    .inputs(d.getInputs().stream()
                            .map( i -> ActivitySubject.input.builder()
                                    .text(i.getText())
                                    .type(i.getType())
                                    .build()
                            ).toList()
                    ).build();

            activitySubjects.add(activitySubject);
        }

        activitySubjectRepository.saveAll(activitySubjects);

        return new BaseResponse<>(BaseResponseMessage.ACTIVITYSUBJECT_CREATE_SUCCESS, "활동 주제 양식 생성 성공");
    }

    public BaseResponse<List<ActivitySubjectDto.ActivitySubjectResponse>> search(Member member) {
        Member nowmember = memberRepository.findByIdx(member.getIdx());

        if (nowmember == null) {
            return new BaseResponse<>(BaseResponseMessage.MEMBER_SEARCH_NOT_FOUND, null);
        }

        List<ActivitySubjectDto.ActivitySubjectResponse> subjects = activitySubjectRepository.findByCompanyIdx(nowmember.getCompany().getIdx()).stream()
                .map(ActivitySubjectDto.ActivitySubjectResponse::from).toList();

        return new BaseResponse<>(BaseResponseMessage.ACTIVITYSUBJECT_SEARCH_SUCCESS, subjects);
    }

    public BaseResponse<String> update(Member member, ActivitySubjectDto.ActivitySubjectResponse dto) {
        Member nowmember = memberRepository.findByIdx(member.getIdx());

        if (nowmember == null) {
            return new BaseResponse<>(BaseResponseMessage.MEMBER_SEARCH_NOT_FOUND, null);
        } else if (!nowmember.getIsAdmin()) {
            return new BaseResponse<>(BaseResponseMessage.INAPPROPRIATE_MEMBER_ACCESS_RIGHTS_FAILS, null);
        }

        Optional<ActivitySubject> beforeSubject = activitySubjectRepository.findById(dto.getId());
        if (beforeSubject.isEmpty()) {
            return new BaseResponse<>(BaseResponseMessage.ACTIVITYSUBJECT_NOT_FOUND, null);
        }

        ActivitySubject updated = ActivitySubject.builder()
                .id(dto.getId())
                .companyIdx(nowmember.getCompany().getIdx())
                .subject(dto.getSubject())
                .esgValue(dto.getEsgValue())
                .esgScore(dto.getEsgScore())
                .esgActivityItem(dto.getEsgActivityItem())
                .evaluationCriteria(dto.getEvaluationCriteria())
                .inputs(dto.getInputs().stream()
                        .map(i -> ActivitySubject.input.builder()
                                .type(i.getType())
                                .text(i.getText())
                                .build())
                        .collect(Collectors.toList())
                )
                .build();

        activitySubjectRepository.save(updated);

        return new BaseResponse<>(BaseResponseMessage.ACTIVITYSUBJECT_UPDATE_SUCCESS, "주제 양식 수정을 성공했습니다.");
    }

    public BaseResponse<String> delete(Member member, String id) {
        Member nowmember = memberRepository.findByIdx(member.getIdx());

        if (nowmember == null) {
            return new BaseResponse<>(BaseResponseMessage.MEMBER_SEARCH_NOT_FOUND, null);
        } else if (!nowmember.getIsAdmin()) {
            return new BaseResponse<>(BaseResponseMessage.INAPPROPRIATE_MEMBER_ACCESS_RIGHTS_FAILS, null);
        }

        Optional<ActivitySubject> beforeSubject = activitySubjectRepository.findById(id);
        if (beforeSubject.isEmpty()) {
            return new BaseResponse<>(BaseResponseMessage.ACTIVITYSUBJECT_NOT_FOUND, null);
        }

        activitySubjectRepository.deleteById(id);
        return new BaseResponse<>(BaseResponseMessage.ACTIVITYSUBJECT_DELETE_SUCCESS, "활동 주제 삭제를 성공했습니다");
    }
}