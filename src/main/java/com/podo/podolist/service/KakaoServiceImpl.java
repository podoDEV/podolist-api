package com.podo.podolist.service;

import com.podo.podolist.exception.LoginFailureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoServiceImpl implements KakaoService {
    private final RestTemplate restTemplate;

    public Map<String, Object> getUserInfo(String accessToken) {
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("kapi.kakao.com")
                .path("/v2/user/me")
                .queryParam("access_token", accessToken)
                .build(false).toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // -H "Authorization: Bearer xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);

        ResponseEntity<Map> responseEntity;
        try {
            responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, Map.class);
        } catch (HttpClientErrorException ex) {
            throw new RuntimeException("kakao user me api failed", ex);
        }

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new LoginFailureException("kakao user me api failed");
        }

        return responseEntity.getBody();
    }
}
