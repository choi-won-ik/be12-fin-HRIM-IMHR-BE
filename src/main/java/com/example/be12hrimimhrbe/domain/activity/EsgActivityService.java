package com.example.be12hrimimhrbe.domain.activity;

import com.example.be12hrimimhrbe.domain.activity.model.EsgActivityDto;
import com.example.be12hrimimhrbe.domain.activitySubject.ActivitySubjectRepository;
import com.example.be12hrimimhrbe.domain.activitySubject.model.ActivitySubject;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.global.LocalImageService;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EsgActivityService {
    private final ActivitySubjectRepository subjectRepository;
    private final EsgActivityRepository activityRepository;
    private final LocalImageService localImageService;

    @Transactional
    public BaseResponse<String> createActivity(EsgActivityDto.ActivityRequest dto,
                                               CustomUserDetails member,
                                               List<MultipartFile> files) {
        ActivitySubject subject = subjectRepository.findById(dto.getSubjectId()).orElseThrow();
        List<String> fileUrls = new ArrayList<>();
        for(ActivitySubject.input input : subject.getInputs()) {
            if(!input.getType().equals("file") && !dto.getInputs().containsKey(input.getText())) {
                return new BaseResponse<>(BaseResponseMessage.ESG_ACTIVITY_INPUT_LEFT, "필수 입력값 누락: "+input.getText());
            }
        }
        for(MultipartFile file : files) {
            String uploadFilePath = localImageService.upload(file);
            dto.getInputs().put(file.getName(), uploadFilePath);
        }
        activityRepository.save(dto.toEntity(member.getMember().getIdx(), member.getMember().getCompany().getIdx()));
        return new BaseResponse<>(BaseResponseMessage.ESG_ACTIVITY_SUBMIT_SUCCESS, "활동 등록 완료");
    }

    public BaseResponse<List<EsgActivityDto.ActivityResponse>> list(CustomUserDetails member) {

    }
}
