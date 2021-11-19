package com.karaoke.manager.filter;

import com.karaoke.manager.entity.Staff;
import com.karaoke.manager.security.SecurityConstant;
import com.karaoke.manager.service.StaffUserService;
import com.karaoke.manager.utils.HttpSupport;
import com.karaoke.manager.utils.token.TokenUtils;
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
import java.util.List;

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
    if (!request.getServletPath().equals("/auth/refresh")
        && !request.getServletPath().equals("/auth")) {

      String rawToken = request.getHeader(AUTHORIZATION);
      if (rawToken != null && rawToken.startsWith("Bearer ")) {

        TokenUtils.VerifierObject verifier =
            TokenUtils.verifyToken(rawToken, SecurityConstant.ACCESS_TOKEN_SECRET_KEY);

        if (verifier.isValid()) {
          Staff staff =
              staffUserService.getStaff(((TokenUtils.ValidVerifierObject) verifier).getUsername());

          if (staff == null) {
            HttpSupport.writeErrorMessage(
                response,
                "Can not find username. Please refresh the token.",
                HttpStatus.UNAUTHORIZED);
          }
          List<SimpleGrantedAuthority> authorities = new ArrayList<>();
          authorities.add(new SimpleGrantedAuthority("ROLE_" + staff.getRole().getCodeName()));
          staff
              .getRole()
              .getPermissions()
              .forEach(
                  permission ->
                      authorities.add(new SimpleGrantedAuthority(permission.getPermissionCode())));
          UsernamePasswordAuthenticationToken authenticationToken =
              new UsernamePasswordAuthenticationToken(staff, null, authorities);
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          filterChain.doFilter(request, response);
        } else {
          TokenUtils.invalidVerifierObjectResponse(
              (TokenUtils.InvalidVerifierObject) verifier, response);
        }
      } else {
        HttpSupport.writeErrorMessage(response, "Unable to find auth.", HttpStatus.UNAUTHORIZED);
      }
    } else {
      filterChain.doFilter(request, response);
    }
  }
}
