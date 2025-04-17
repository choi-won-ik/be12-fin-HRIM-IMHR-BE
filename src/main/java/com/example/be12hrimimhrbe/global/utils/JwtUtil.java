package com.example.be12hrimimhrbe.global.utils;


import com.example.be12hrimimhrbe.domain.company.model.Company;
import com.example.be12hrimimhrbe.domain.department.model.Department;
import com.example.be12hrimimhrbe.domain.member.model.CustomUserDetails;
import com.example.be12hrimimhrbe.domain.member.model.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    private static String SECRET;
    @Value("${jwt.expired}")
    private int exp;
    private static int EXP;

    @PostConstruct
    public void init() {
        SECRET = secret;
        EXP = exp;
    }

    public static CustomUserDetails getMember(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return CustomUserDetails.builder()
                    .member(Member.builder()
                                .idx(claims.get("memberIdx", Long.class))
                                .name(claims.get("memberName", String.class))
                                .email(claims.get("memberEmail", String.class))
                                .memberId(claims.get("memberId", String.class))
                                .isAdmin(claims.get("isAdmin", Boolean.class))
                                .hasProdAuth(claims.get("hasProdAuth", Boolean.class))
                                .hasPartnerAuth(claims.get("hasPartnerAuth", Boolean.class))
                                .company(Company.builder().idx(claims.get("companyIdx", Long.class)).build())
                                .department(Department.builder().idx(claims.get("departmentIdx", Long.class)).build())
                                .status(Member.Status.valueOf(claims.get("memberStatus", String.class)))
                                .build())
                    .hrAuthoritySet(new HashSet<String>(claims.get("hrAuthoritySet", List.class)))
                    .build();

        } catch (ExpiredJwtException e) {
            System.out.println("토큰이 만료되었습니다!");
            return null;
        }
    }

    public static String generateToken(Long memberIdx, String memberEmail,
                                       String memberId, String memberName, Set<String> hrAuthoritySet,
                                       Boolean isAdmin, Boolean hasProdAuth, Boolean hasPartnerAuth,
                                       Company company, Department department, Member.Status status
                                       ) {
        Claims claims = Jwts.claims();
        claims.put("memberEmail", memberEmail);
        claims.put("memberIdx", memberIdx);
        claims.put("memberId", memberId);
        claims.put("memberName", memberName);
        claims.put("hrAuthoritySet", hrAuthoritySet);
        claims.put("isAdmin", isAdmin);
        claims.put("hasProdAuth", hasProdAuth);
        claims.put("hasPartnerAuth", hasPartnerAuth);
        claims.put("companyIdx", company.getIdx());
        claims.put("departmentIdx", department == null ? null : department.getIdx());
        claims.put("memberStatus", status == null ? null : status.name());
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXP))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
        return token;
    }

    public static String refreshToken(String oldToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET)
                    .build()
                    .parseClaimsJws(oldToken)
                    .getBody();
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + EXP))
                    .signWith(SignatureAlgorithm.HS256, SECRET)
                    .compact();
            return token;

        } catch (ExpiredJwtException e) {
            System.out.println("토큰이 만료되었습니다!");
            return null;
        }
    }

    public static boolean validate(String token) {
        try {
            if(token == null) { return false;}
            Jwts.parserBuilder()
                    .setSigningKey(SECRET)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            System.out.println("토큰이 만료되었습니다!");
            return false;
        }
        return true;
    }
}
