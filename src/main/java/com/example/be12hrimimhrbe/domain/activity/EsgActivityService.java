package com.example.be12hrimimhrbe.domain.activity;

import com.example.be12hrimimhrbe.domain.activity.model.EsgActivityDto;
import com.example.be12hrimimhrbe.domain.activitySubject.ActivitySubjectRepository;
import com.example.be12hrimimhrbe.domain.activitySubject.model.ActivitySubject;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EsgActivityService {
    private final ActivitySubjectRepository subjectRepository;
    private final EsgActivityRepository activityRepository;

    public BaseResponse<String> createActivity(EsgActivityDto.ActivityRequest dto, CustomUserDetails member) {
        ActivitySubject subject = subjectRepository.findById(dto.getSubjectId()).orElseThrow();
        for(ActivitySubject.input input : subject.getInputs()) {
            if(!dto.getInputs().containsKey(input.getText())) {
                return new BaseResponse<>(BaseResponseMessage.ESG_ACTIVITY_INPUT_LEFT, "필수 입력값 누락: "+input.getText());
            }
        }
        activityRepository.save(dto.toEntity(member.getMember().getIdx(), member.getMember().getCompany().getIdx()));
        return new BaseResponse<>(BaseResponseMessage.ESG_ACTIVITY_SUBMIT_SUCCESS, "활동 등록 완료");
    }
}
