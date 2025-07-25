package com.finsight.backend.security.service;

import com.finsight.backend.dto.request.LoginForm;
import com.finsight.backend.enumerate.ErrorCode;
import com.finsight.backend.mapper.MemberMapper;
import com.finsight.backend.security.info.UserPrincipal;
import com.finsight.backend.vo.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerDetailService implements UserDetailsService {

    private final MemberMapper memberMapper;

    public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException{
        Member member = memberMapper.findMemberByUserId(userId);
        if(member == null){
            throw new UsernameNotFoundException(ErrorCode.NOT_FOUND_USER.getMessage());
        }
        return UserPrincipal.create(member);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
