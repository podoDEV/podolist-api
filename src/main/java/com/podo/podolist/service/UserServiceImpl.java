package com.podo.podolist.service;

import com.podo.podolist.entity.UserEntity;
import com.podo.podolist.exception.ResourceNotFoundException;
import com.podo.podolist.model.User;
import com.podo.podolist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(User::from)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> getUser(int userId) {
        return userRepository.findById(userId)
                .map(User::from);
    }

    @Override
    public Optional<User> getUser(String provider, String providerId) {
        return userRepository.findByProviderAndProviderId(provider, providerId)
                .map(User::from);
    }

    @Override
    @Transactional
    public User createUser(User user) {
        UserEntity userEntity = user.toUserEntity();
        return User.from(userRepository.saveAndFlush(userEntity));
    }

    @Override
    @Transactional
    public User updateUser(int userId, User user) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(ResourceNotFoundException::new);
        Optional.ofNullable(user.getName())
                .ifPresent(userEntity::setName);
        Optional.ofNullable(user.getProvider())
                .ifPresent(userEntity::setProvider);
        Optional.ofNullable(user.getProviderId())
                .ifPresent(userEntity::setProviderId);
        return User.from(userRepository.saveAndFlush(userEntity));
    }

    @Override
    @Transactional
    public void deleteUser(int userId) {
        if (!userRepository.findById(userId).isPresent()) {
            return;
        }
        userRepository.deleteById(userId);
    }
}
