package com.podo.podolist.service;

import com.podo.podolist.model.User;

public interface LoginService {
    User login(String accessToken);
    User loginAnonymous(String uuid);
}
