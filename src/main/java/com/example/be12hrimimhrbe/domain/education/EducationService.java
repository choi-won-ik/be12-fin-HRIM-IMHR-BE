package com.example.be12hrimimhrbe.domain.education;

import com.example.be12hrimimhrbe.domain.activity.ActivityRepository;
import com.example.be12hrimimhrbe.domain.activity.model.Activity;
import com.example.be12hrimimhrbe.domain.activity.model.ActivityDto;
import com.example.be12hrimimhrbe.domain.education.model.EducationDto;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationService {
    private final ActivityRepository activityRepository;

    public BaseResponse<EducationDto.PageEducationListResp> educationService(Member member, int page, int size) {
        List<EducationDto.EducationListResp> result = new ArrayList<>();

        // 관리자가 활동 리스트 확인
        if (member.getIsAdmin()) {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "idx"));
            Page<Activity> list = activityRepository.findAllAndMemberEducation(member.getCompany().getIdx(),pageable);
            for (Activity activity : list) {
                EducationDto.EducationListResp index = EducationDto.EducationListResp.to(activity, activity.getMember());
                // 프론트에 출력되는 이름 변경
                index = EducationDto.EducationListResp.findType(activity, index);
                index = EducationDto.EducationListResp.findStatus(activity, index);

                result.add(index);
            }

            return new BaseResponse<>(BaseResponseMessage.ADMIN_ACTIVITYLIST_FIND, EducationDto.PageEducationListResp.builder()
                    .total(list.getTotalPages())
                    .educationList(result)
                    .build());
        }else{
            Page<Activity> list = activityRepository.findAllByMemberEducation(member, PageRequest.of(page, size));
            
            for (Activity activity : list) {
                System.out.println("포문 돎");
                EducationDto.EducationListResp index = EducationDto.EducationListResp.to(activity, member);
                // 프론트에 출력되는 이름 변경
                index = EducationDto.EducationListResp.findType(activity, index);
                index = EducationDto.EducationListResp.findStatus(activity, index);

                result.add(index);
            }
            
            return new BaseResponse<>(BaseResponseMessage.USER_ACTIVITYLIST_FIND, EducationDto.PageEducationListResp.builder()
                    .total(list.getTotalPages())
                    .educationList(result)
                    .build());
        }
    }
}
