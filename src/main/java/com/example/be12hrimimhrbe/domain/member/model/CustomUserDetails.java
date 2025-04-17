package com.example.be12hrimimhrbe.domain.member.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private Member member;
    private Set<String> hrAuthoritySet;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        String prefix = "ROLE_";
        if(member.getIsAdmin()) authorities.add(new SimpleGrantedAuthority(prefix + "ADMIN"));
        if(member.getHasProdAuth()) authorities.add(new SimpleGrantedAuthority(prefix + "PROD"));
        if(member.getHasPartnerAuth()) authorities.add(new SimpleGrantedAuthority(prefix + "PARTNER"));
        hrAuthoritySet.forEach(authority -> authorities.add(new SimpleGrantedAuthority(prefix + authority)));
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return member.getStatus().equals(Member.Status.APPROVED);
    }
}
