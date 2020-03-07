package com.podo.podolist.controller;

import com.podo.podolist.exception.ResourceNotFoundException;
import com.podo.podolist.model.Login;
import com.podo.podolist.model.User;
import com.podo.podolist.model.dto.AnonymousLoginRequest;
import com.podo.podolist.service.LoginService;
import com.podo.podolist.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@CrossOrigin(
        origins = {"*"},
        allowedHeaders = {"*"},
        exposedHeaders = {"Set-Cookie", "SCOUTER"},
        allowCredentials = "true"
)
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final UserService userService;

    @PostMapping(value = {
            "/v1/login/kakao",
            "/login/kakao"
    })
    public Login.Response loginWithKakao(@RequestBody Login.Request loginRequest,
                                         HttpServletRequest request) {
        HttpSession httpSession = request.getSession(true);
        log.info("sessionId: " + httpSession.getId());
        Integer loginUserId = Optional.ofNullable((Integer) httpSession.getAttribute("userId"))
                .orElse(null);

        // 로그인되어있는경우
        if (Objects.nonNull(loginUserId)) {
            String sessionId = httpSession.getId();
            User user = userService.getUser(loginUserId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            log.info("sessionId: " + httpSession.getId());
            log.info("userId: " + httpSession.getAttribute("userId"));
            log.info("attributes: " + httpSession.getAttributeNames());

            return Login.Response.of(sessionId, user);
        }

        // 로그인되어있지않은경우
        User user = loginService.login(loginRequest.getAccessToken());
        httpSession.setAttribute("userId", user.getUserId());

        log.info("sessionId: " + httpSession.getId());
        log.info("userId: " + httpSession.getAttribute("userId"));
        log.info("attributes: " + httpSession.getAttributeNames());

        return Login.Response.of(httpSession.getId(), user);
    }

    @PostMapping(value = {
            "/v1/login/anonymous",
            "/login/anonymous"
    })
    public Login.Response loginAnonymously(@RequestBody AnonymousLoginRequest anonymousLoginRequest,
                                           HttpServletRequest request) {

        HttpSession httpSession = request.getSession(true);
        Integer loginUserId = Optional.ofNullable((String) httpSession.getAttribute("userId"))
                .map(Integer::parseInt)
                .orElse(null);

        // 로그인되어있는경우
        if (Objects.nonNull(loginUserId)) {
            String sessionId = httpSession.getId();
            User user = userService.getUser(loginUserId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            return Login.Response.of(sessionId, user);
        }

        // 로그인되어있지않은경우
        User user = loginService.loginAnonymous(anonymousLoginRequest.getUuid());
        httpSession.setAttribute("userId", user.getUserId());

        return Login.Response.of(httpSession.getId(), user);
    }
}
