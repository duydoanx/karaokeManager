package com.karaoke.manager.utils.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.karaoke.manager.entity.Staff;
import com.karaoke.manager.security.SecurityConstant;
import com.karaoke.manager.utils.HttpSupport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TokenUtils {

  public static VerifierObject verifyToken(String tokenStartWithBearer, String secret) {
    try {
      String token = tokenStartWithBearer.substring("Bearer ".length());
      Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
      JWTVerifier verifier = JWT.require(algorithm).build();
      DecodedJWT decodedJWT = verifier.verify(token);
      return new ValidVerifierObject(decodedJWT.getSubject());
    } catch (JWTVerificationException e) {
      return new InvalidVerifierObject(e.getMessage());
    }
  }

  public static String accessTokenGenerate(User user) {
    Date expTime = new Date(System.currentTimeMillis() + SecurityConstant.ACCESS_TOKEN_EXP_TIME);
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
    Date expTime = new Date(System.currentTimeMillis() + SecurityConstant.REFRESH_TOKEN_EXP_TIME);
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

  public static User validVerifierObjectToUser(
      ValidVerifierObject verifierReturnObject, GetStaff getStaff) {

    String username = verifierReturnObject.getUsername();
    Staff staff = getStaff.get(username);
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_" + staff.getRole().getCodeName()));
    staff
        .getRole()
        .getPermissions()
        .forEach(
            permission ->
                authorities.add(new SimpleGrantedAuthority(permission.getPermissionCode())));

    return new User(username, staff.getPassword(), authorities);
  }

  public static void invalidVerifierObjectResponse(
      InvalidVerifierObject invalidVerifierObject, HttpServletResponse response)
      throws IOException {
    response.setHeader("error", invalidVerifierObject.getErrorMessage());
    HttpSupport.writeErrorMessage(
        response, invalidVerifierObject.getErrorMessage(), HttpStatus.FORBIDDEN);
  }

  @Getter
  @Setter
  @AllArgsConstructor
  public abstract static class VerifierObject {
    private boolean isValid;
  }

  @Getter
  @Setter
  public static class InvalidVerifierObject extends VerifierObject {
    private String errorMessage;

    public InvalidVerifierObject(String errorMessage) {
      super(false);
      this.errorMessage = errorMessage;
    }
  }

  @Getter
  @Setter
  public static class ValidVerifierObject extends VerifierObject {
    private String username;

    public ValidVerifierObject(String username) {
      super(true);
      this.username = username;
    }
  }
}
