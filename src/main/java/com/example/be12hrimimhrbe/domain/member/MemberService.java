package com.example.be12hrimimhrbe.domain.member;

import com.example.be12hrimimhrbe.domain.company.CompanyRepository;
import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.member.model.MemberDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${project.upload.path}")
    private String defaultUploadPath;

    @Transactional
    public BaseResponse<MemberDto.CompanySignupResponse> companySignup(MemberDto.CompanySignupRequest dto, MultipartFile file) {
        String uploadFilePath = null;
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
        Company company = companyRepository.findByCode(dto.getCompanyCode()).orElse(null);
        if(company==null) {
            return new BaseResponse<>(BaseResponseMessage.PERSONAL_SIGNUP_NOT_FOUND_COMPANY, null);
        }
        Member member = memberRepository
                .save(dto.toMember(passwordEncoder.encode(dto.getPassword()), company));
        return new BaseResponse<>(BaseResponseMessage.PERSONAL_SIGNUP_SUCCESS,
                MemberDto.PersonalSignupResponse.fromMember(member));
    }
}
