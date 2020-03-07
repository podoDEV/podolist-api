package com.podo.podolist.service;

import com.podo.podolist.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getUsers(Pageable pageable);
    Optional<User> getUser(int userId);
    Optional<User> getUser(String provider, String providerId);
    User createUser(User user);
    User updateUser(int userId, User user);
    void deleteUser(int userId);
}
