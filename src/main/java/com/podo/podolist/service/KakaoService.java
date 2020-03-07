package com.podo.podolist.service;

import java.util.Map;

public interface KakaoService {
    Map<String, Object> getUserInfo(String accessToken);
}
