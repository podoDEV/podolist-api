package com.podo.podolist.service;

import com.podo.podolist.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {
    private final UserService userService;

    @Override
    public User signup(String name, String provider, String providerId, String profileImageUrl) {
        return userService.createUser(User.builder()
                .name(name)
                .provider(provider)
                .providerId(providerId)
                .profileImageUrl(profileImageUrl)
                .email(null)
                .build());
    }
}
