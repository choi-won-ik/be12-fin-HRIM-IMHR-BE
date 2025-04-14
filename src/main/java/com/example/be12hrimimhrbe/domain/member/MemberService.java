package com.example.be12hrimimhrbe.domain.member;

import com.example.be12hrimimhrbe.domain.authority.HrAuthorityRepository;
import com.example.be12hrimimhrbe.domain.authority.model.HrAuthority;
import com.example.be12hrimimhrbe.domain.company.CompanyRepository;
import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.member.model.MemberDto;
import com.example.be12hrimimhrbe.domain.member.model.PasswordReset;
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
    private final PasswordResetRepository passwordResetRepository;
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

    public void sendPasswordReset(String uuid, String email, String host) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[비밀번호 재설정] IMHR 비밀번호 재설정 안내");
        message.setText(
                "아래 링크를 통해 비밀번호 재설정을 진행해주세요.\n "+host+"/change_pw/" + uuid
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
    public BaseResponse<String> findPassword(MemberDto.FindPWRequest dto, String host) {
        String uuid = UUID.randomUUID().toString();
        LocalDateTime time = LocalDateTime.now().plusHours(1L);
        Member member = memberRepository.findByMemberIdAndEmailAndIsAdmin(dto.getMemberId(),
                                                                        dto.getEmail(),
                                                                        dto.getWay().equals("0")).orElse(null);
        if(member != null) {
            passwordResetRepository.save(PasswordReset.builder().uuid(uuid).member(member).expiryDate(time).build());
            sendPasswordReset(uuid, member.getEmail(), host);
        }
        return new BaseResponse<>(BaseResponseMessage.FIND_PW_SUCCESS, "비밀번호 찾기 성공");
    }

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
                List<HrAuthority> hrAuthorities = hrAuthorityRepository.findAllByMember(result.get());
                for (HrAuthority hrAuthority : hrAuthorities) {
                    authoritySet.add(prefix + hrAuthority.getDepartment().getIdx());
                }
                return new CustomUserDetails(result.get(), authoritySet);
            }
        }
        return null;
    }
}
