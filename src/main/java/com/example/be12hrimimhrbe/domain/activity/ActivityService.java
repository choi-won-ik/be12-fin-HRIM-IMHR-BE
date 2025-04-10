package com.example.be12hrimimhrbe.domain.activity;

import com.example.be12hrimimhrbe.domain.activity.model.Activity;
import com.example.be12hrimimhrbe.domain.activity.model.ActivityDto;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final MemberRepository memberRepository;

    public BaseResponse<ActivityDto.ActivityListResponse> getMyActivity(ActivityDto.ActivityListRequest dto) {
        System.out.println(dto.getMemberIdx());
        Member member = memberRepository.findById(dto.getMemberIdx())
                .orElseThrow(() -> new RuntimeException("해당 멤버의 Activity가 없습니다."));
        System.out.println(member.getMemberId());
        List<Activity> list=activityRepository.findByMember(member);

        return new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, new ActivityDto.ActivityListResponse(list));
    }
}
