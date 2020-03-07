package com.podo.podolist.service;

import com.podo.podolist.model.User;

public interface SignupService {
    User signup(String name, String provider, String providerId, String profileImageUrl);
}
