package com.podo.podolist.service;

import com.podo.podolist.exception.LoginFailureException;
import com.podo.podolist.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final SignupService signupService;
    private final KakaoService kakaoService;
    private final UserService userService;
    private final NameService nameService;

    @Override
    public User login(String accessToken) {
        if (StringUtils.isEmpty(accessToken)) {
            throw new LoginFailureException("accessToken should not be null");
        }
        // 카카오 로그인
        Map<String, Object> userInfoMap = kakaoService.getUserInfo(accessToken);
        String name = (String) ((Map<String, Object>) userInfoMap.get("properties")).get("nickname");
        String providerId = String.valueOf(userInfoMap.get("id"));
        String profileImageUrl = (String) ((Map<String, Object>) userInfoMap.get("properties")).get("profile_image");
        String email = null;

        User user = userService.getUser("kakao", providerId)
                .orElse(null);

        if (Objects.nonNull(user)) {
            return user;
        }

        User newUser = User.builder()
                .name(name)
                .provider("kakao")
                .providerId(providerId)
                .profileImageUrl(profileImageUrl)
                .email(email)
                .build();

        return userService.createUser(newUser);
    }

    @Override
    public User loginAnonymous(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            throw new LoginFailureException("uuid should not be null");
        }

        User user = userService.getUser("anonymous", uuid)
                .orElse(null);

        if (Objects.nonNull(user)) {
            return user;
        }

        User newUser = User.builder()
                .name(nameService.getRandomName())
                .provider("anonymous")
                .providerId(uuid)
                .profileImageUrl(null)
                .email(null)
                .build();

        return userService.createUser(newUser);
    }
}
