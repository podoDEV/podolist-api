package com.podo.podolist.controller;

import com.podo.podolist.exception.UnAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public interface Authenticated {
    Logger log = LoggerFactory.getLogger("Authenticated");

    @ModelAttribute("userId")
    default Integer getUserId(HttpServletRequest request) {

        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            log.info("session is null");
            throw new UnAuthorizedException();
        }
        log.info("sessionId: " + httpSession.getId());
        log.info("userId: " + httpSession.getAttribute("userId"));
        log.info("attributes: " + httpSession.getAttributeNames());

        return (Integer) Optional.ofNullable(request.getSession(false))
                .map(session -> session.getAttribute("userId"))
                .orElseThrow(UnAuthorizedException::new);
    }
}
