package com.honlife.core.infra.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleClient {

    private final RestTemplate restTemplate;

    @Value("${GOOGLE_ID}")
    private String googleId;

    @Value("${GOOGLE_REDIRECT_URI}")
    private String redirectUri;


}
