package com.karaoke.manager.security;

import org.springframework.stereotype.Component;

@Component
public class SecurityConstant {
  public static final String ACCESS_TOKEN_SECRET_KEY = "tercesssecca";

  public static final String REFRESH_TOKEN_SECRET_KEY = "terceshseerfer";

  public static final Long ACCESS_TOKEN_EXP_TIME = 24 * 60 * 60 * 1000L;

  public static final Long REFRESH_TOKEN_EXP_TIME = 60 * 24 * 60 * 60 * 1000L;
}
