package com.example.onjeong.util;

import com.example.onjeong.error.ErrorCode;
import com.example.onjeong.profile.domain.Profile;
import com.example.onjeong.profile.exception.ProfileNotExistException;
import com.example.onjeong.profile.repository.ProfileRepository;
import com.example.onjeong.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileUtil {
    private final ProfileRepository profileRepository;

    public Profile getProfileByUser(final User user){
        return profileRepository.findByUser(user)
                .orElseThrow(()-> new ProfileNotExistException("profile not exist", ErrorCode.PROFILE_NOTEXIST));
    }
}
