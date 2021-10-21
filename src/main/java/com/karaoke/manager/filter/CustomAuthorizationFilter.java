package com.karaoke.manager.filter;

import com.karaoke.manager.security.SecurityConstant;
import com.karaoke.manager.service.StaffUserService;
import com.karaoke.manager.utils.HttpSupport;
import com.karaoke.manager.utils.token.TokenUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

  private final StaffUserService staffUserService;

  public CustomAuthorizationFilter(StaffUserService staffUserService) {
    this.staffUserService = staffUserService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (request.getServletPath().equals("/api/auth")
        || request.getServletPath().equals("/api/auth/refresh")) {
      filterChain.doFilter(request, response);
    } else {
      String rawToken = request.getHeader(AUTHORIZATION);
      if (rawToken != null && rawToken.startsWith("Bearer ")) {

        TokenUtils.VerifierObject verifier =
            TokenUtils.verifyToken(rawToken, SecurityConstant.ACCESS_TOKEN_SECRET_KEY);

        if (verifier.isValid()) {
          User user = TokenUtils.validVerifierObjectToUser(
              (TokenUtils.ValidVerifierObject) verifier, staffUserService::getStaff);

          UsernamePasswordAuthenticationToken authenticationToken =
              new UsernamePasswordAuthenticationToken(
                  user.getUsername(), null, user.getAuthorities());
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          filterChain.doFilter(request, response);
        } else {
          TokenUtils.invalidVerifierObjectResponse((TokenUtils.InvalidVerifierObject) verifier, response);
        }
      } else {
        filterChain.doFilter(request, response);
      }
    }
  }
}
