package com.finsight.backend.security.service;

import com.finsight.backend.enumerate.ErrorCode;
import com.finsight.backend.mapper.UserMapper;
import com.finsight.backend.security.info.UserPrincipal;
import com.finsight.backend.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerDetailService implements UserDetailsService {

    private final UserMapper UserMapper;

    public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException{
        User user = UserMapper.findUserByUserId(userId);
        if(user == null){
            throw new UsernameNotFoundException(ErrorCode.NOT_FOUND_USER.getMessage());
        }
        return UserPrincipal.create(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
