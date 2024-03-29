package com.example.restApiProject.filters;

import javax.crypto.SecretKey;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import com.example.restApiProject.Constants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthFilter extends GenericFilterBean {
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, java.io.IOException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    String authHeader = httpRequest.getHeader("Authorization");
    if(authHeader != null) {
      String[] authHeaderArr = authHeader.split("Bearer ");
      if(authHeaderArr.length > 1 && authHeaderArr[1] != "null") {
        String authToken = authHeaderArr[1];
        try {
          Claims claims = Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(authToken).getPayload();
          httpRequest.setAttribute("userId", Integer.parseInt(claims.get("userId").toString()));
        } catch (Exception e) { 
          httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Invalid token/expired");
          return;
        }
      } else {
        httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token must be Bearer [token]");
        return;
      }
    } else {
      httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token must be provided");
      return;
    }

    chain.doFilter(request, response);
  }

  private SecretKey getKey() {
    byte[] keyBytes=Decoders.BASE64.decode(Constants.API_SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
