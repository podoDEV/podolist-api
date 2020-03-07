package com.podo.podolist.service;

import com.podo.podolist.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class LoginServiceImplTest {
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String WRONG_ACCESS_TOKEN = "wrongAccessToken";
    private static final int USER_ID = 1;
    private static final String USER_NAME = "userName";
    private static final String USER_PROVIDER = "kakao";
    private static final String USER_PROVIDER_ID = "kakaoUserId";
    private static final String USER_PROFILE_IMAGE_URL = "kakaoProfileImageUrl";

    @Mock
    private SignupService signupService;
    @Mock
    private KakaoService kakaoService;
    @Mock
    private UserService userService;
    @Mock
    private NameService nameService;
    private LoginServiceImpl loginServiceImpl;

    @Before
    public void setUp() {
        loginServiceImpl = new LoginServiceImpl(signupService, kakaoService, userService, nameService);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("nickname", USER_NAME);
        Map<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put("id", USER_PROVIDER_ID);
        userInfoMap.put("properties", properties);
        when(kakaoService.getUserInfo(ACCESS_TOKEN)).thenReturn(userInfoMap);

        User user = User.builder()
                .userId(USER_ID)
                .name(USER_NAME)
                .provider(USER_PROVIDER)
                .providerId(USER_PROVIDER_ID)
                .build();
        when(userService.getUser(USER_PROVIDER, USER_PROVIDER_ID)).thenReturn(Optional.of(user));
//        when(signupService.signup(USER_NAME, USER_PROVIDER, USER_PROVIDER_ID, USER_PROFILE_IMAGE_URL)).thenReturn(user);
    }

    @Test
    public void 로그인성공_이미_가입된_회원() {
        // given
        // when
        User user = loginServiceImpl.login(ACCESS_TOKEN);
        // then
        assertEquals(USER_NAME, user.getName());
        verify(userService).getUser(USER_PROVIDER, USER_PROVIDER_ID);
    }

    @Ignore
    @Test
    public void 로그인성공_처음_로그인하는_회원() {
        // given
        when(userService.getUser(USER_PROVIDER, USER_PROVIDER_ID))
                .thenReturn(Optional.empty());
        // when
        User user = loginServiceImpl.login(ACCESS_TOKEN);
        // then
        assertEquals(USER_NAME, user.getName());
        verify(signupService).signup(USER_NAME, USER_PROVIDER, USER_PROVIDER_ID, USER_PROFILE_IMAGE_URL);
    }

    @Test(expected = RuntimeException.class)
    public void 로그인실패_유효하지_않은_토큰() {
        // given
        when(kakaoService.getUserInfo(WRONG_ACCESS_TOKEN)).thenThrow(new RuntimeException("kakao api failed"));
        // when
        loginServiceImpl.login(WRONG_ACCESS_TOKEN);
        // then
    }
}
