package com.example.be12hrimimhrbe.domain.member;

import com.example.be12hrimimhrbe.domain.authority.HrAuthorityRepository;
import com.example.be12hrimimhrbe.domain.authority.model.HrAuthority;
import com.example.be12hrimimhrbe.domain.company.CompanyRepository;
import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import com.example.be12hrimimhrbe.domain.member.model.MemberDto;
import com.example.be12hrimimhrbe.global.response.BaseResponse;
import com.example.be12hrimimhrbe.global.response.BaseResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final HrAuthorityRepository hrAuthorityRepository;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String memberId = username.substring(0, username.lastIndexOf("_")-1);
        String way = username.substring(username.lastIndexOf("_")+1);
        if(way.equals("0")) {
            Optional<Member> result = memberRepository.findByMemberIdAndIsAdmin(memberId, Boolean.TRUE);
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
            Optional<Member> result = memberRepository.findByMemberIdAndIsAdmin(memberId, Boolean.FALSE);
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
