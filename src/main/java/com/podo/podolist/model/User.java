package com.podo.podolist.model;

import com.podo.podolist.entity.UserEntity;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.Objects;

@Builder
@Getter
public class User {
    private Integer userId;
    private String name;
    private String provider;
    private String providerId;
    private String profileImageUrl;
    private String email;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    public static User from(UserEntity userEntity) {
        return User.builder()
                .userId(userEntity.getUserId())
                .name(userEntity.getName())
                .provider(userEntity.getProvider())
                .providerId(userEntity.getProviderId())
                .profileImageUrl(userEntity.getProfileImageUrl())
                .email(userEntity.getEmail())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }

    public UserEntity toUserEntity() {
        return UserEntity.builder()
                .name(name)
                .provider(provider)
                .providerId(providerId)
                .profileImageUrl(profileImageUrl)
                .email(email)
                .build();
    }

    @ApiModel(value = "UserRequest")
    @Getter
    @ToString
    public static class Request {
        private String name;
        private String provider;
        private String providerId;
        private String profileImageUrl;
        private String email;

        public User toUser() {
            return User.builder()
                    .name(name)
                    .provider(provider)
                    .providerId(providerId)
                    .profileImageUrl(profileImageUrl)
                    .email(email)
                    .build();
        }
    }

    @ApiModel(value = "UserResponse")
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Response {
        private Integer id;
        private String name;
        private String provider;
        private String providerId;
        private String profileImageUrl;
        private String email;
        private ZonedDateTime createdAt;
        private ZonedDateTime updatedAt;

        public static Response from(User user) {
            if (Objects.isNull(user)) {
                throw new IllegalArgumentException("user should not be null");
            }
            return Response.builder()
                    .id(user.getUserId())
                    .name(user.getName())
                    .provider(user.getProvider())
                    .providerId(user.getProviderId())
                    .profileImageUrl(user.getProfileImageUrl())
                    .email(user.getEmail())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .build();
        }
    }
}
