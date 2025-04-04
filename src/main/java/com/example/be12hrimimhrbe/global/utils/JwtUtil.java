package com.example.be12hrimimhrbe.global.utils;


import com.example.be12hrimimhrbe.domain.member.model.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

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

    public static Member getMember(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Member.builder()
                    .idx(claims.get("memberIdx", Long.class))
                    .name(claims.get("memberName", String.class))
                    .email(claims.get("memberEmail", String.class))
                    .memberId(claims.get("memberId", String.class))
                    .build();

        } catch (ExpiredJwtException e) {
            System.out.println("토큰이 만료되었습니다!");
            return null;
        }
    }

    public static String generateToken(Long memberIdx, String memberEmail,
                                       String memberId, String memberName) {
        Claims claims = Jwts.claims();
        claims.put("memberEmail", memberEmail);
        claims.put("memberIdx", memberIdx);
        claims.put("memberId", memberId);
        claims.put("memberName", memberName);
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
