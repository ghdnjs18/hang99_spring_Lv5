package com.sparta.springboottest.security;

import com.sparta.springboottest.entity.User;
import com.sparta.springboottest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameAndUserUseTrue(username).orElseThrow(() ->
                new UsernameNotFoundException("해당 유저를 찾을 수 없습니다. :" + username)
        );

        return new UserDetailsImpl(user);
    }
}
