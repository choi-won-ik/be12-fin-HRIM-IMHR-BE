package com.example.be12hrimimhrbe.domain.activity;

import com.example.be12hrimimhrbe.domain.activity.model.Activity;
import com.example.be12hrimimhrbe.domain.activity.model.ActivityDto;
import com.example.be12hrimimhrbe.domain.education.model.EducationDto;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.global.LocalImageService;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import com.example.be12hrimimhrbe.global.utils.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final LocalImageService localImageService;
    private final FileService fileService;
    private final MemberRepository memberRepository;
    @Value("${file.upload-path}")
    private String uploadPath;

    public BaseResponse<ActivityDto.PageActivityListResp> activityList(Member member, int page, int size) {
        List<ActivityDto.ActivityListResp> result = new ArrayList<>();

        // 관리자가 활동 리스트 확인
        if (member.getIsAdmin()) {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "idx"));
            Page<Activity> list = activityRepository.findAllAndMemberNotEducation(member.getCompany().getIdx(),pageable);
            for (Activity activity : list) {
                ActivityDto.ActivityListResp index = ActivityDto.ActivityListResp.to(activity, activity.getMember());
                // 프론트에 출력되는 이름 변경
                index = ActivityDto.ActivityListResp.findType(activity, index);
                index = ActivityDto.ActivityListResp.findStatus(activity, index);

                result.add(index);
            }

            return new BaseResponse<>(BaseResponseMessage.ADMIN_ACTIVITYLIST_FIND, ActivityDto.PageActivityListResp.builder()
                    .total(list.getTotalPages())
                    .activityList(result)
                    .build());

        }
        // 개인 유저가 활동 리스트 확인
        else {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "idx"));
            Page<Activity> list = activityRepository.findAllByMembernotEducation(member, pageable);
            for (Activity activity : list) {
                ActivityDto.ActivityListResp index = ActivityDto.ActivityListResp.to(activity, member);
                // 프론트에 출력되는 이름 변경
                index = ActivityDto.ActivityListResp.findType(activity, index);
                index = ActivityDto.ActivityListResp.findStatus(activity, index);

                result.add(index);
            }

            return new BaseResponse<>(BaseResponseMessage.USER_ACTIVITYLIST_FIND, ActivityDto.PageActivityListResp.builder()
                    .total(list.getTotalPages())
                    .activityList(result)
                    .build());
        }
    }

    public BaseResponse<ActivityDto.PageActivityListResp> activitySearch(Member member, int page, int size, String search) {
        List<ActivityDto.ActivityListResp> result = new ArrayList<>();

        // 관리자가 활동 리스트 확인
        if (member.getIsAdmin()) {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "idx"));
            Page<Activity> list = activityRepository.findAllAndMemberNotEducationSearch(member.getCompany().getIdx(),pageable,search);
            for (Activity activity : list) {
                ActivityDto.ActivityListResp index = ActivityDto.ActivityListResp.to(activity, activity.getMember());
                // 프론트에 출력되는 이름 변경
                index = ActivityDto.ActivityListResp.findType(activity, index);
                index = ActivityDto.ActivityListResp.findStatus(activity, index);

                result.add(index);
            }

            return new BaseResponse<>(BaseResponseMessage.ADMIN_ACTIVITYLIST_FIND, ActivityDto.PageActivityListResp.builder()
                    .total(list.getTotalPages())
                    .activityList(result)
                    .build());

        }
        // 개인 유저가 활동 리스트 확인
        else {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "idx"));
            Page<Activity> list = activityRepository.findAllByMembernotEducationSearch(member, pageable,search);
            for (Activity activity : list) {
                ActivityDto.ActivityListResp index = ActivityDto.ActivityListResp.to(activity, member);
                // 프론트에 출력되는 이름 변경
                index = ActivityDto.ActivityListResp.findType(activity, index);
                index = ActivityDto.ActivityListResp.findStatus(activity, index);

                result.add(index);
            }

            return new BaseResponse<>(BaseResponseMessage.USER_ACTIVITYLIST_FIND, ActivityDto.PageActivityListResp.builder()
                    .total(list.getTotalPages())
                    .activityList(result)
                    .build());
        }
    }

    public BaseResponse<ActivityDto.ActivityItemResponse> getDetail(Long idx, Member member) {
        Activity activity = activityRepository.findByIdAndMember(idx);

        // 이미지 url 설정
        String servedUrl = "http://localhost:8080/img" + activity.getFileUrl();


        ActivityDto.ActivityItemResponse result = ActivityDto.ActivityItemResponse.builder()
                .activityIdx(activity.getIdx())
                .title(activity.getTitle())
                .startDate(activity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .status(activity.getStatus())
                .type(activity.getType())
                .content(activity.getDescription())
                .fileUrl(servedUrl)
                .member(new ActivityDto.ActivityMember(activity.getMember()))
                .build();

        return new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, result);

    }

    @Transactional
    public BaseResponse<Activity> Regist(
            ActivityDto.ActivityRegistReq dto, MultipartFile imgFile, Member member) {
        Activity activity = null;

        // 파일 업로드
        String uploadFilePath = localImageService.upload(imgFile);

        // 기부 시
        if (dto.getType().equals("기부")) {
            activity = ActivityDto.ActivityRegistReq.toEntity(member, dto, Activity.Type.DONATION,uploadFilePath);
        }
        // 봉사 시
        else if(dto.getType().equals("봉사")){
            activity = ActivityDto.ActivityRegistReq.toEntity(member, dto, Activity.Type.VOLUNTEER,uploadFilePath);
        }
        // 교육
        else{
            if(dto.getType().equals("환경")){
                activity = ActivityDto.ActivityRegistReq.toEntityEdu(member, dto, Activity.Type.EDUCATION,uploadFilePath,Activity.EducationType.ENVIRONMENTAL_EDUCATION);
            }else if(dto.getType().equals("사회")){
                activity = ActivityDto.ActivityRegistReq.toEntityEdu(member, dto, Activity.Type.EDUCATION,uploadFilePath,Activity.EducationType.SOCIAL_EDUCATION);
            }else if(dto.getType().equals("지배구조")){
                activity = ActivityDto.ActivityRegistReq.toEntityEdu(member, dto, Activity.Type.EDUCATION,uploadFilePath,Activity.EducationType.GOVERNANCE_EDUCATION);
            }

        }
        try {
            Activity result = activityRepository.save(activity);
            return new BaseResponse<>(BaseResponseMessage.ACTIVITY_REGIST_SUCCESS, result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public BaseResponse<Long> ativityApprovalAgree(Member member, Long idx) {
        Activity activity = activityRepository.findById(idx).get();

        if (member.getIsAdmin()) {
            // 활동을 승인 함
            if (activity.getStatus().equals(Activity.Status.PENDING)) {
                activity = new Activity(activity, Activity.Status.APPROVED);
                try {
                    Activity result = activityRepository.save(activity);
                    return new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, result.getIdx());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            // 활동이 이미 처리 되어 있음
            return new BaseResponse<>(BaseResponseMessage.MY_ACTIVITY_PROCESSED);
        }
        // 활동 처리 권한이 없음
        return new BaseResponse<>(BaseResponseMessage.ACTIVITY_APPROVAL_FALSE);
    }

    @Transactional
    public BaseResponse<Long> ativityApprovalOppose(Member member, Long idx) {
        Activity activity = activityRepository.findById(idx).get();
        if (member.getIsAdmin()) {
            // 활동을 반려함
            if (activity.getStatus().equals(Activity.Status.PENDING)) {
                activity = new Activity(activity, Activity.Status.REJECTED);
                try {
                    Activity result = activityRepository.save(activity);
                    return new BaseResponse<>(BaseResponseMessage.ACTIVITY_APPROVAL_OPPOSE, result.getIdx());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            // 이미 활동 처리가 되었음.
            else {
                return new BaseResponse<>(BaseResponseMessage.MY_ACTIVITY_PROCESSED);
            }
        }
        // 활동 처리 권한이 없음
        else {
            return new BaseResponse<>(BaseResponseMessage.ACTIVITY_APPROVAL_FALSE);
        }
    }

    @Transactional
    public BaseResponse<Long> ativityDelete(Member member, Long idx) {
        Activity activity = activityRepository.findByIdAndMember(idx);
        if (member.getIdx() == activity.getMember().getIdx()) {
            try {
                activityRepository.delete(activity);

                return new BaseResponse<>(BaseResponseMessage.ACTIVITY_DELETE_SUCCESS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return new BaseResponse<>(BaseResponseMessage.ACTIVITY_DELETE_FALSE);
        }
    }


}
