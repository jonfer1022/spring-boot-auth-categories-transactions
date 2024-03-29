package com.example.restApiProject.resources;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.restApiProject.Constants;
import com.example.restApiProject.domain.Users;
import com.example.restApiProject.services.UsersService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@RestController
@RequestMapping("/api/users")
public class UsersResource {

  @Autowired
  UsersService usersService;

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, Object> userMap) {
    String email = (String) userMap.get("email");
    String password = (String) userMap.get("password");

    Users user = usersService.validateUser(email, password);
    // Map<String, String> entity = new HashMap<>();
    // if(user != null) {
    //   entity.put("message", "Login successful");
    // } else {
    //   entity.put("message", "Login failed");
    // }
    return new ResponseEntity<>(generateJwtToken(user), HttpStatus.OK);
  }
  

  @PostMapping("/register")
  public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, Object> userMap) {
      String firstName = (String) userMap.get("firstName");
      String lastName = (String) userMap.get("lastName");
      String email = (String) userMap.get("email");
      String password = (String) userMap.get("password");
      
      Users user = usersService.registerUser(firstName, lastName, email, password);
      return new ResponseEntity<>(generateJwtToken(user), HttpStatus.OK);
  }

  private Map<String, String> generateJwtToken(Users users) {
    long timeStamp = System.currentTimeMillis();
    String token = Jwts.builder()
        .claim("userId", users.getUserId())
        .claim("firstName", users.getFirstName())
        .claim("lastName", users.getLastName())
        .claim("email", users.getEmail())
        .issuedAt(new Date(timeStamp))
        .expiration(new Date(timeStamp + Constants.TOKEN_VALIDITY))
        .signWith(getKey())
        .compact();
    Map<String, String> map = new HashMap<>();
    map.put("token", token);
    return map;
  }

  private SecretKey getKey() {
    byte[] keyBytes=Decoders.BASE64.decode(Constants.API_SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
