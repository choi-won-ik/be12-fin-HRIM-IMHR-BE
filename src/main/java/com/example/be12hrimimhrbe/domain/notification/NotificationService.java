package com.example.be12hrimimhrbe.domain.notification;

import com.example.be12hrimimhrbe.domain.activity.model.Activity;
import com.example.be12hrimimhrbe.domain.campaign.CampaignRepository;
import com.example.be12hrimimhrbe.domain.campaign.model.Campaign;
import com.example.be12hrimimhrbe.domain.company.CompanyRepository;
import com.example.be12hrimimhrbe.domain.member.MemberRepository;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.notification.model.Notification;
import com.example.be12hrimimhrbe.domain.notification.model.NotificationDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CampaignRepository campaignRepository;

    @Transactional
    public void activityApprove(NotificationDto.ActivityApproveReq dto) {
        Member member = dto.getMember();

        member.setNotificationCount(member.getNotificationCount() + 1);
        memberRepository.save(member);

        Notification notification = Notification.builder()
                .isRead(false)
                .member(member)
                .content(dto.getContent())
                .title(dto.getTitle())
                .createdAt(LocalDateTime.now())
                .url(dto.getUrl())
                .build();
        notificationRepository.save(notification);

        simpMessagingTemplate.convertAndSend("/topic/notification/" + member.getIdx(), NotificationDto.NotificationResp.from(notification, member));
    }

    @Transactional(readOnly = true)
    public BaseResponse<List<NotificationDto.NotificationResp>> getMyNotifications(Member member, int page, int size) {
        List<NotificationDto.NotificationResp> result = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "idx"));
        Page<Notification> notis = notificationRepository.findAllByMember(member, pageable);

        for (Notification notification : notis) {
            result.add(NotificationDto.NotificationResp.from(notification, notification.getMember()));
        }

        return new BaseResponse<>(BaseResponseMessage.SWGGER_SUCCESS, result);
    }


    public void signupApprove(String companyCode, NotificationDto.SignupApproveReq dto) {
        List<Member> list = memberRepository.findAllByCompanyCode(companyCode);

        for (Member member : list) {
            if (member.getIsAdmin()) {
                member.setNotificationCount(member.getNotificationCount() + 1);
                memberRepository.save(member);
                String url = "/staffSearch/" + member.getCompany().getIdx();

                Notification notification = Notification.builder()
                        .isRead(false)
                        .member(member)
                        .content(dto.getContent())
                        .title(dto.getTitle())
                        .createdAt(LocalDateTime.now())
                        .url(url)
                        .build();
                notificationRepository.save(notification);

                simpMessagingTemplate.convertAndSend("/topic/notification/" + member.getIdx(), NotificationDto.NotificationResp.from(notification, member));
            }
        }
    }

    public void activityApproveReq(NotificationDto.ActivityApproveRequest dto, Long companyIdx) {
        List<Member> list = memberRepository.findAllByCompanyIdx(companyIdx);

        for (Member member : list) {
            if (member.getIsAdmin()) {
                member.setNotificationCount(member.getNotificationCount() + 1);
                memberRepository.save(member);
                String url = "/activeDetails/" + dto.getActivityIdx();

                Notification notification = Notification.builder()
                        .isRead(false)
                        .member(member)
                        .content(dto.getContent())
                        .title(dto.getTitle())
                        .createdAt(LocalDateTime.now())
                        .url(url)
                        .build();
                notificationRepository.save(notification);

                simpMessagingTemplate.convertAndSend("/topic/notification/" + member.getIdx(), NotificationDto.NotificationResp.from(notification, member));
            }
        }
    }

    public void EventRegist(Long companyIdx, NotificationDto.SignupApproveReq dto) {
        List<Member> members = memberRepository.findAllByCompanyIdx(companyIdx);

        for (Member member : members) {
            if (!member.getIsAdmin()) {
                member.setNotificationCount(member.getNotificationCount() + 1);
                memberRepository.save(member);

                String url = "/calendar";

                Notification notification = Notification.builder()
                        .isRead(false)
                        .member(member)
                        .content(dto.getTitle())
                        .title("새로운 회사 일정이 있습니다.")
                        .createdAt(LocalDateTime.now())
                        .url(url)
                        .build();
                notificationRepository.save(notification);

                simpMessagingTemplate.convertAndSend("/topic/notification/" + member.getIdx(), NotificationDto.NotificationResp.from(notification, member));
            }
        }
    }

    public void eventcampaignMemberAddRegist(Long eventIdx, NotificationDto.eventcampaignMemberAddReq dto) {
        List<Campaign> campaigns=campaignRepository.findAllByEventIdx(eventIdx);
        System.out.println("서비스 실행");
        System.out.println(campaigns.size());
        for (Campaign campaign : campaigns) {
            Member member = campaign.getMember();
            System.out.println(member.getIdx());
            member.setNotificationCount(member.getNotificationCount() + 1);
            memberRepository.save(member);

            String url = "/calendar";

            Notification notification = Notification.builder()
                    .isRead(false)
                    .member(member)
                    .content(dto.getTitle())
                    .title("회사 캠패인 활동이 승인 되었습니다.")
                    .createdAt(LocalDateTime.now())
                    .url(url)
                    .build();
            notificationRepository.save(notification);

            simpMessagingTemplate.convertAndSend("/topic/notification/" + member.getIdx(), NotificationDto.NotificationResp.from(notification, member));
        }
    }
}
