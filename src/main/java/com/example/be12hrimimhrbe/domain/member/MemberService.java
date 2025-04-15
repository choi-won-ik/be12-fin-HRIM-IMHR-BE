package com.example.be12hrimimhrbe.domain.member;

import com.example.be12hrimimhrbe.domain.activity.ActivityRepository;
import com.example.be12hrimimhrbe.domain.activity.model.Activity;
import com.example.be12hrimimhrbe.domain.authority.HrAuthorityRepository;
import com.example.be12hrimimhrbe.domain.authority.model.HrAuthority;
import com.example.be12hrimimhrbe.domain.campaign.CampaignRepository;
import com.example.be12hrimimhrbe.domain.campaign.model.Campaign;
import com.example.be12hrimimhrbe.domain.company.CompanyRepository;
import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.department.DepartmentRepository;
import com.example.be12hrimimhrbe.domain.department.model.Department;
import com.example.be12hrimimhrbe.domain.department.model.DepartmentDto;
import com.example.be12hrimimhrbe.domain.feedback.FeedbackResponseRepository;
import com.example.be12hrimimhrbe.domain.feedback.model.FeedbackResponse;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.member.model.MemberDto;
import com.example.be12hrimimhrbe.domain.member.model.PasswordReset;
import com.example.be12hrimimhrbe.domain.notification.NotificationRepository;
import com.example.be12hrimimhrbe.domain.notification.model.Notification;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final CampaignRepository campaignRepository;
    private final FeedbackResponseRepository feedbackResponseRepository;
    private final ActivityRepository activityRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final HrAuthorityRepository hrAuthorityRepository;
    private final JavaMailSender mailSender;
    @Value("${project.upload.path}")
    private String defaultUploadPath;

    public void sendIDInfo(String email, String id) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[ID 찾기] IMHR ID 정보");
        message.setText(
                "회원님의 ID는 "+id+" 입니다."
        );

        mailSender.send(message);
    }

    public void sendPasswordReset(String uuid, String email, String origin) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[비밀번호 재설정] IMHR 비밀번호 재설정 안내");
        message.setText(
                "아래 링크를 통해 비밀번호 재설정을 진행해주세요.\n "+origin+"/changepassword/" + uuid
        );

        mailSender.send(message);
    }

    public BaseResponse<String> findId(MemberDto.FindIdRequest dto) {
        memberRepository.findByNameAndEmailAndIsAdmin(dto.getName(),
                dto.getEmail(),
                dto.getWay().equals("0")).ifPresent(member -> sendIDInfo(dto.getEmail(), member.getMemberId()));
        return new BaseResponse<>(BaseResponseMessage.FIND_ID_SUCCESS, "ID 찾기 성공");
    }

    @Transactional
    public BaseResponse<String> findPassword(MemberDto.FindPWRequest dto, String origin) {
        String uuid = UUID.randomUUID().toString();
        LocalDateTime time = LocalDateTime.now().plusHours(1L);
        Member member = memberRepository.findByMemberIdAndEmailAndIsAdmin(dto.getMemberId(),
                                                                        dto.getEmail(),
                                                                        dto.getWay().equals("0")).orElse(null);
        if(member != null) {
            passwordResetRepository.save(PasswordReset.builder().uuid(uuid).member(member).expiryDate(time).build());
            sendPasswordReset(uuid, member.getEmail(), origin);
        }
        return new BaseResponse<>(BaseResponseMessage.FIND_PW_SUCCESS, "비밀번호 찾기 성공");
    }

    @Transactional
    public BaseResponse<String> passwordReset(MemberDto.ResetPasswordRequest dto, CustomUserDetails customMember) {
        if(customMember != null) {
            Member member = memberRepository.findById(customMember.getMember().getIdx()).orElseThrow();
            if(!passwordEncoder.matches(dto.getOldPassword(), member.getPassword()))
                return new BaseResponse<>(BaseResponseMessage.RESET_PASSWORD_UNMATCHED, "실패");
            memberRepository.save(member.updateMember(Member.builder()
                    .password(passwordEncoder.encode(dto.getNewPassword()))
                    .build()));
            return new BaseResponse<>(BaseResponseMessage.RESET_PASSWORD_SUCCESS, "비밀번호 변경 성공");
        } else {
            PasswordReset passwordReset = passwordResetRepository
                    .findByUuidAndExpiryDateAfter(dto.getUuid(), LocalDateTime.now())
                    .orElse(null);
            if(passwordReset == null) {
                return new BaseResponse<>(BaseResponseMessage.RESET_PASSWORD_NULL, "인증 실패");
            }

            Member member = passwordReset.getMember();
            memberRepository.save(member.updateMember(Member.builder()
                    .password(passwordEncoder.encode(dto.getNewPassword()))
                    .build()));
            return new BaseResponse<>(BaseResponseMessage.RESET_PASSWORD_SUCCESS, "비밀번호 변경 성공");
        }
    }

    public BaseResponse<MemberDto.InfoDetailResponse> getStaffDetail(Long idx) {
        Member member = memberRepository.findById(idx).orElse(null);
        if(member == null)
            return new BaseResponse<>(BaseResponseMessage.MEMBER_SEARCH_NOT_FOUND, null);
        List<String> roles = new ArrayList<>();
        if(member.getIsAdmin()) roles.add("ROLE_ADMIN");
        if(member.getHasPartnerAuth()) roles.add("ROLE_PARTNER");
        if(member.getHasProdAuth()) roles.add("ROLE_PROD");
        String prefix = "ROLE_";
        List<HrAuthority> hrAuthorities = hrAuthorityRepository.findAllByMember(member);
        for (HrAuthority hrAuthority : hrAuthorities) {
            roles.add(prefix + hrAuthority.getDepartment().getIdx());
        }
        List<Department> departments = departmentRepository.findAllByCompany(member.getCompany());
        MemberDto.InfoDetailResponse infoDetailResponse = MemberDto.InfoDetailResponse.fromEntity(
                MemberDto.InfoResponse.fromEntity(member, roles),
                DepartmentDto.DepartmentListResponse.builder().departments(departments).build()
        );
        return new BaseResponse<>(BaseResponseMessage.MEMBER_DETAIL_SUCCESS, infoDetailResponse);
    }

    @Transactional
    public BaseResponse<String> deleteMember(Long idx) {
        Member member = memberRepository.findById(idx).orElse(null);
        if(member == null)
            return new BaseResponse<>(BaseResponseMessage.MEMBER_SEARCH_NOT_FOUND, "해당 회원이 없습니다.");
        List<Campaign> campaigns = campaignRepository.findAllByMember(member);
        List<FeedbackResponse> feedbackResponsesFrom = feedbackResponseRepository.findAllByFrom(member);
        List<FeedbackResponse> feedbackResponsesTo = feedbackResponseRepository.findAllByTo(member);
        List<Activity> activities = activityRepository.findAllByMember(member);
        List<Notification> notifications = notificationRepository.findAllByMember(member);
        campaignRepository.deleteAll(campaigns);
        feedbackResponseRepository.deleteAll(feedbackResponsesFrom);
        feedbackResponseRepository.deleteAll(feedbackResponsesTo);
        activityRepository.deleteAll(activities);
        notificationRepository.deleteAll(notifications);
        memberRepository.delete(member);
        return new BaseResponse<>(BaseResponseMessage.MEMBER_RESIGN_SUCCESS, "탈퇴 처리 성공");
    }

    public BaseResponse<MemberDto.InfoResponse> getMyInfo(CustomUserDetails customMember) {
        Member member = memberRepository.findById(customMember.getMember().getIdx()).orElse(null);
        if(member == null)
            return new BaseResponse<>(BaseResponseMessage.MEMBER_SEARCH_NOT_FOUND, null);
        List<String> roles = customMember.getAuthoritySet().stream().toList();
        return new BaseResponse<>(BaseResponseMessage.MYINFO_RETRIEVE_SUCCESS,
                MemberDto.InfoResponse.fromEntity(member, roles));
    }

    @Transactional
    public BaseResponse<MemberDto.CompanySignupResponse> companySignup(MemberDto.CompanySignupRequest dto, MultipartFile file) {
        String uploadFilePath = null;
        Optional<Member> result = memberRepository.findByMemberIdAndIsAdmin(dto.getMemberId(), true);
        if(result.isPresent()) {
            return new BaseResponse<>(BaseResponseMessage.SIGNUP_DUPLICATE_ID, null);
        }
        result = memberRepository.findByEmailAndIsAdmin(dto.getEmail(), true);
        if(result.isPresent()) {
            return new BaseResponse<>(BaseResponseMessage.SIGNUP_DUPLICATE_EMAIL, null);
        }
        if(file!=null && !file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();

            uploadFilePath = "/"+ UUID.randomUUID()+"_"+originalFilename;
            File uploadFile = new File(defaultUploadPath+"/"+uploadFilePath);
            Company company = companyRepository.save(dto.toCompany(uploadFilePath));
            Member member = dto.toMember(passwordEncoder.encode(dto.getPassword()), company);
            memberRepository.save(member);
            try {
                file.transferTo(uploadFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new BaseResponse<>(BaseResponseMessage.COMPANY_SIGNUP_SUCCESS, MemberDto.CompanySignupResponse.fromMember(member));
        } else {
            return new BaseResponse<>(BaseResponseMessage.COMPANY_SIGNUP_NOT_FOUND_FILE, null);
        }
    }

    public BaseResponse<MemberDto.PersonalSignupResponse> personalSignup(MemberDto.PersonalSignupRequest dto) {
        Optional<Member> result = memberRepository.findByMemberIdAndIsAdmin(dto.getMemberId(), false);
        if(result.isPresent()) {
            return new BaseResponse<>(BaseResponseMessage.SIGNUP_DUPLICATE_ID, null);
        }
        result = memberRepository.findByEmailAndIsAdmin(dto.getEmail(), false);
        if(result.isPresent()) {
            return new BaseResponse<>(BaseResponseMessage.SIGNUP_DUPLICATE_EMAIL, null);
        }
        Company company = companyRepository.findByCode(dto.getCompanyCode()).orElse(null);
        if(company==null) {
            return new BaseResponse<>(BaseResponseMessage.PERSONAL_SIGNUP_NOT_FOUND_COMPANY, null);
        }
        Member member = memberRepository
                .save(dto.toMember(passwordEncoder.encode(dto.getPassword()), company));
        return new BaseResponse<>(BaseResponseMessage.PERSONAL_SIGNUP_SUCCESS,
                MemberDto.PersonalSignupResponse.fromMember(member));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String memberId = username.substring(0, username.lastIndexOf("_"));
        String way = username.substring(username.lastIndexOf("_")+1);
        if(way.equals("0")) {
            Optional<Member> result = memberRepository.findByMemberIdAndIsAdminAndStatus(memberId, Boolean.TRUE, Member.Status.APPROVED);
            if (result.isPresent()) {
                Set<String> authoritySet = new HashSet<>();
                String prefix = "ROLE_";
                authoritySet.add(prefix + "ADMIN");
                if(result.get().getHasProdAuth()) authoritySet.add(prefix + "PROD");
                if(result.get().getHasPartnerAuth()) authoritySet.add(prefix + "PARTNER");
                List<HrAuthority> hrAuthorities = hrAuthorityRepository.findAllByMember(result.get());
                for (HrAuthority hrAuthority : hrAuthorities) {
                    authoritySet.add(prefix + hrAuthority.getDepartment().getIdx());
                }
                return new CustomUserDetails(result.get(), authoritySet);
            }
        } else if (way.equals("1")) {
            Optional<Member> result = memberRepository.findByMemberIdAndIsAdminAndStatus(memberId, Boolean.FALSE, Member.Status.APPROVED);
            if (result.isPresent()) {
                Set<String> authoritySet = new HashSet<>();
                String prefix = "ROLE_";
                if(result.get().getHasProdAuth()) authoritySet.add(prefix + "PROD");
                if(result.get().getHasPartnerAuth()) authoritySet.add(prefix + "PARTNER");
                List<HrAuthority> hrAuthorities = hrAuthorityRepository.findAllByMember(result.get());
                for (HrAuthority hrAuthority : hrAuthorities) {
                    authoritySet.add(prefix + hrAuthority.getDepartment().getIdx());
                }
                return new CustomUserDetails(result.get(), authoritySet);
            }
        }
        return null;
    }

    public boolean isSameCompany(Long oriIdx, Long otherIdx) {
        Member oriMember = memberRepository.findById(oriIdx).orElse(null);
        Member otherMember = memberRepository.findById(otherIdx).orElse(null);
        return oriMember != null && otherMember != null
                && oriMember.getCompany().getIdx().equals(otherMember.getCompany().getIdx());
    }
}
