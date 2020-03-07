package com.podo.podolist.repository;

import com.podo.podolist.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <UserEntity, Integer> {
    Optional<UserEntity> findById(int userId);
    Optional<UserEntity> findByProviderAndProviderId(String provider, String providerId);
    Page<UserEntity> findAll(Pageable pageable);
}
