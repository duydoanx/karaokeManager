package com.karaoke.manager.security;

import org.springframework.stereotype.Component;

@Component
public class SecurityConstant {
  public static final String ACCESS_TOKEN_SECRET_KEY = "tercesssecca";

  public static final String REFRESH_TOKEN_SECRET_KEY = "terceshseerfer";

  public static final Integer ACCESS_TOKEN_EXP_TIME = 30 * 60 * 1000;

  public static final Integer REFRESH_TOKEN_EXP_TIME = 24 * 60 * 60 * 1000;

}
