package com.karaoke.manager.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyHelper {
    @Value("${application.security.jwt.access_token.secret_key}")
    public String accessTokenSecretKey;

    @Value("${application.security.jwt.refresh_token.secret_key}")
    public String refreshTokenSecretKey;
}
