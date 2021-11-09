package com.karaoke.manager.filter;

import com.auth0.jwt.algorithms.Algorithm;
import com.karaoke.manager.entity.support.ResponseApi;
import com.karaoke.manager.security.SecurityConstant;
import com.karaoke.manager.utils.HttpSupport;
import com.karaoke.manager.utils.token.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  public String accessTokenSecretKey;

  public String refreshTokenSecretKey;

  public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
    accessTokenSecretKey = SecurityConstant.ACCESS_TOKEN_SECRET_KEY;
    refreshTokenSecretKey = SecurityConstant.REFRESH_TOKEN_SECRET_KEY;
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(username, password);
    return authenticationManager.authenticate(token);
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult)
      throws IOException, ServletException {
    Algorithm accessAlgorithm = Algorithm.HMAC256(accessTokenSecretKey.getBytes());
    Algorithm refreshAlgorithm = Algorithm.HMAC256(refreshTokenSecretKey.getBytes());

    User user = (User) authResult.getPrincipal();

    String accessToken = TokenUtils.accessTokenGenerate(user);
    String refreshToken = TokenUtils.refreshTokenGenerate(user);

    Map<String, String> body = new HashMap<>();
    body.put("access_token", accessToken);
    body.put("refresh_token", refreshToken);
    HttpSupport.writeJsonMapObjectValue(response, new ResponseApi<>(HttpStatus.OK.value(), body));
  }

  @Override
  protected void unsuccessfulAuthentication(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {
    log.error("Login Failed");
    String message = "Authenticate failed!";
    HttpSupport.writeErrorMessage(response, message, HttpStatus.UNAUTHORIZED);
  }
}
