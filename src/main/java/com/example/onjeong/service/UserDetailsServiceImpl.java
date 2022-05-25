package com.example.onjeong.service;

import com.example.onjeong.controller.UserNotFoundException;
import com.example.onjeong.domain.MyUserDetails;
import com.example.onjeong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public MyUserDetails loadUserByUsername(String userNickname) {
        /*
        User user = userRepository.findByUserNickname(userNickname);
        if (user == null) throw new UsernameNotFoundException(userNickname);
        return new MyUserDetails(user, Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString())));
        */
        return userRepository.findByUserNickname(userNickname)
                .map(u -> new MyUserDetails(u, Collections.singleton(new SimpleGrantedAuthority(u.getRole().getValue()))))
                .orElseThrow(() -> new UserNotFoundException(userNickname));
    }
}
