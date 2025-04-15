package com.example.be12hrimimhrbe.domain.activity;

import com.example.be12hrimimhrbe.domain.activity.model.Activity;
import com.example.be12hrimimhrbe.domain.activity.model.ActivityDto;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.global.LocalImageService;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import com.example.be12hrimimhrbe.global.utils.FileService;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final LocalImageService localImageService;
    private final FileService fileService;
    @Value("${file.upload-path}")
    private String uploadPath;

    public BaseResponse<List<ActivityDto.ActivityListResp>> getMyActivity(Member member, int page, int size) {
        List<ActivityDto.ActivityListResp> result = new ArrayList<>();
//        if(member.getRole==Member.Role.MANAGER){
//            List<ActivityDto.ActivityListResp> result = new ArrayList<>();
//            List<Activity> list = activityRepository.findAllAndMember();
//            for (Activity activity : list) {
//
//        ActivityDto.ActivityListResp index = ActivityDto.ActivityListResp.to(activity,activity.getMember());
//        index = ActivityDto.ActivityListResp.findType(activity,index);
//        index = ActivityDto.ActivityListResp.findStatus(activity,index);
//        result.add(index);
//            }
//
//
//            return new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, result);
//
//        }else{
//
        Page<Activity> list = activityRepository.findByMember(member, PageRequest.of(page, size));
        for (Activity activity : list) {
            ActivityDto.ActivityListResp index = ActivityDto.ActivityListResp.to(activity, member);
            index = ActivityDto.ActivityListResp.findType(activity, index);
            index = ActivityDto.ActivityListResp.findStatus(activity, index);

            result.add(index);
        }


//        }
        return new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, result);
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
                .memberId(activity.getMember().getMemberId())
                .memberName(activity.getMember().getName())
//                .memberRole(activity.getMember().getRole)
                .build();

        return new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, result);

    }

    public BaseResponse<Activity> Regist(
            ActivityDto.ActivityRegistReq dto, MultipartFile imgFile, Member member) {
        Activity activity = null;

        Activity.Type activityType = null;
        // 파일 업로드
        String uploadFilePath = localImageService.upload(imgFile);

        // 기부 시
        if (dto.getType().equals("기부")) {
            activityType = Activity.Type.DONATION;

            activity = Activity.builder()
                    .member(member)
                    .type(activityType)
                    .title(dto.getTitle())
                    .description(dto.getDescription())
                    .fileUrl(uploadFilePath)
                    .donation(dto.getPerformance())
                    .createdAt(LocalDateTime.now())
                    .status(Activity.Status.PENDING)
                    .build();
        }
        // 봉사 시
        else {
            if (dto.getType().equals("봉사")) {
                activityType = Activity.Type.VOLUNTEER;
            } else if (dto.getType().equals("교육")) {
                activityType = Activity.Type.EDUCATION;
            }

            activity = Activity.builder()
                    .member(member)
                    .type(activityType)
                    .title(null)
                    .description(dto.getDescription())
                    .fileUrl(uploadFilePath)
                    .performedAt(dto.getPerformance())
                    .createdAt(LocalDateTime.now())
                    .status(Activity.Status.PENDING)
                    .build();
        }

        return new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, activityRepository.save(activity));
    }

    @Transactional
    public BaseResponse<Long> ativityApprovalAgree(Long idx) {
        Activity activity = activityRepository.findById(idx).get();

        if (activity.getStatus().equals(Activity.Status.PENDING)) {
            activity = new Activity(activity, Activity.Status.APPROVED);
            try {
                Activity result = activityRepository.save(activity);
                return new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, result.getIdx());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return new BaseResponse<>(BaseResponseMessage.MY_ACTIVITY_PROCESSED);
    }

    @Transactional
    public BaseResponse<Long> ativityApprovalOppose(Long idx) {
        Activity activity = activityRepository.findById(idx).get();
        if (activity.getStatus().equals(Activity.Status.PENDING)) {
            activity = new Activity(activity, Activity.Status.REJECTED);
            try {
                Activity result = activityRepository.save(activity);
                return new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, result.getIdx());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return new BaseResponse<>(BaseResponseMessage.MY_ACTIVITY_PROCESSED);
    }

}
