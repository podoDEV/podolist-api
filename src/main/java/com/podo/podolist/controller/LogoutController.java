package com.podo.podolist.controller;

import com.podo.podolist.exception.UnAuthorizedException;
import com.podo.podolist.service.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin(
        origins = {"*"},
        allowCredentials = "true"
)
@RequiredArgsConstructor
public class LogoutController implements Authenticated {

    private final LogoutService logoutService;

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            throw new UnAuthorizedException();
        }
        String sessionId = httpSession.getId();
        try {
            httpSession.invalidate();
        } catch (IllegalStateException ex) {
            log.info("Session already invalidated. sessionId: {}", sessionId);
        }
        logoutService.logout();
    }
}
