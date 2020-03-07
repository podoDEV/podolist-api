package com.podo.podolist.entity;

import com.podo.podolist.util.DateTimeUtils;
import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "User")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column
    private String name;
    @Column
    private String provider;
    @Column(name = "provider_id", unique = true)
    private String providerId;
    @Column(name = "profile_image_url")
    private String profileImageUrl;
    @Column(name = "email")
    private String email;
    @Column(name = "created_at")
    private ZonedDateTime createdAt;
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<ItemEntity> itemEntities;

    @PrePersist
    protected void onCreate() {
        updatedAt = createdAt = ZonedDateTime.now(DateTimeUtils.getZonedIdOfSeoul());
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now(DateTimeUtils.getZonedIdOfSeoul());
    }
}
