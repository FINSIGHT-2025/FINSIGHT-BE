package com.finsight.backend.security.service;

import com.finsight.backend.common.exception.ErrorCode;
import com.finsight.backend.repository.mapper.UserMapper;
import com.finsight.backend.security.info.UserPrincipal;
import com.finsight.backend.domain.vo.user.UserVO;
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
        UserVO user = UserMapper.findUserByUserId(userId);
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
