package com.podo.podolist.model;

import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.util.Base64Utils;

@ToString
public class Login {
    @ApiModel(value = "LoginRequest")
    @Getter
    @ToString
    public static class Request {
        private String accessToken;
    }

    @ApiModel(value = "LoginResponse")
    @Builder
    @Getter
    @NoArgsConstructor @AllArgsConstructor
    @ToString
    public static class Response {
        private String sessionId;
        private User.Response user;

        public static Response of(String sessionId, User user) {
            return Response.builder()
                    .sessionId(Base64Utils.encodeToString(sessionId.getBytes()))
                    .user(User.Response.from(user))
                    .build();
        }
    }
}
