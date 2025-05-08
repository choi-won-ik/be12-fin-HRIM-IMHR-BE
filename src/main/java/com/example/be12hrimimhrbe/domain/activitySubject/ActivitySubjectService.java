package com.example.be12hrimimhrbe.domain.activitySubject;

import com.example.be12hrimimhrbe.domain.activitySubject.model.ActivitySubject;
import com.example.be12hrimimhrbe.domain.activitySubject.model.ActivitySubjectDto;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivitySubjectService {
    private final ActivitySubjectRepository activitySubjectRepository;
    private final MemberRepository memberRepository;

    public BaseResponse<String> create(ActivitySubjectDto.ActivitySubjectRequest dto, Member member) {
        Member nowmember = memberRepository.findByIdx(member.getIdx());

        if (nowmember == null) {
            return new BaseResponse<>(BaseResponseMessage.MEMBER_SEARCH_NOT_FOUND, null);
        }

        Long companyIdx = nowmember.getCompany().getIdx();

        ActivitySubject activitySubject = ActivitySubject.builder()
                .companyIdx(companyIdx)
                .subjects(
                        dto.getSubjects().stream()
                                .map(s -> ActivitySubject.Subject.builder()
                                        .subject(s.getSubject())
                                        .inputs(
                                                s.getInputs().stream()
                                                        .map(inputDto -> ActivitySubject.Subject.input.builder()
                                                                .text(inputDto.getText())
                                                                .type(inputDto.getType())
                                                                .build()
                                                        ).toList()
                                                ).build()
                                        ).toList()
                                ).build();
        activitySubjectRepository.save(activitySubject);

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
}