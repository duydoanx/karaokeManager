package com.karaoke.manager.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.karaoke.manager.security.SecurityConstant;
import com.karaoke.manager.support.HttpSupport;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (request.getServletPath().equals("/api/auth") || request.getServletPath().equals("/api/auth/refresh") ) {
      filterChain.doFilter(request, response);
    } else {
      String rawToken = request.getHeader(AUTHORIZATION);
      if (rawToken != null && rawToken.startsWith("Bearer ")) {
        try {
          String token = rawToken.substring("Bearer ".length());
          Algorithm algorithm =
              Algorithm.HMAC256(SecurityConstant.ACCESS_TOKEN_SECRET_KEY.getBytes());
          JWTVerifier verifier = JWT.require(algorithm).build();
          DecodedJWT decodedJWT = verifier.verify(token);
          String username = decodedJWT.getSubject();
          String[] roles = decodedJWT.getClaim("role").asArray(String.class);
          List<SimpleGrantedAuthority> authorities = new ArrayList<>();
          stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
          UsernamePasswordAuthenticationToken authenticationToken =
              new UsernamePasswordAuthenticationToken(username, null, authorities);
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          filterChain.doFilter(request, response);
        } catch (Exception e) {
          response.setStatus(HttpStatus.FORBIDDEN.value());
          response.setHeader("error", e.getMessage());
          Map<String, String> body = new HashMap<>();
          body.put("error_message", e.getMessage());
          HttpSupport.writeJsonObjectValue(response, body);
        }

      } else {
        filterChain.doFilter(request, response);
      }
    }
  }
}
