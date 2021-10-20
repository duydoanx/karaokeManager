package com.karaoke.manager.support;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.karaoke.manager.security.SecurityConstant;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TokenHelper {
  public static String accessTokenGenerate(User user) {
    Date expTime = new Date(System.currentTimeMillis() + 10 * 60 * 1000);
    Map<String, List<String>> claims = new HashMap<>();
    claims.put(
        "role",
        user.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList()));
    return tokenGenerate(
        user.getUsername(), expTime, SecurityConstant.ACCESS_TOKEN_SECRET_KEY, claims);
  }

  public static String refreshTokenGenerate(User user) {
    Date expTime = new Date(System.currentTimeMillis() + 60 * 60 * 1000);
    return tokenGenerate(user.getUsername(), expTime, SecurityConstant.REFRESH_TOKEN_SECRET_KEY);
  }

  private static String tokenGenerate(String username, Date expire, String secretValue) {
    return tokenGenerate(username, expire, secretValue, null);
  }

  private static <T> String tokenGenerate(
      String username, Date expire, String secretValue, Map<String, T> claims) {
    Algorithm algorithm = Algorithm.HMAC256(secretValue.getBytes());
    JWTCreator.Builder builder = JWT.create();
    builder.withSubject(username).withExpiresAt(expire);

    if (claims != null) {
      claims.forEach(
          (s, t) -> {
            if (t instanceof String) {
              builder.withClaim(s, (String) t);
            } else if (t instanceof Boolean) {
              builder.withClaim(s, (Boolean) t);
            } else if (t instanceof Integer) {
              builder.withClaim(s, (Integer) t);
            } else if (t instanceof Long) {
              builder.withClaim(s, (Long) t);
            } else if (t instanceof Double) {
              builder.withClaim(s, (Double) t);
            } else if (t instanceof Date) {
              builder.withClaim(s, (Date) t);
            } else if (t instanceof Map) {
              builder.withClaim(s, (Map<String, ?>) t);
            } else if (t instanceof List) {
              builder.withClaim(s, (List<?>) t);
            }
          });
    }

    return builder.sign(algorithm);
  }
}
