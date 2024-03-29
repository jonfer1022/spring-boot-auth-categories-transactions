package com.example.restApiProject.repositories;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
// import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import com.example.restApiProject.domain.Users;
import com.example.restApiProject.exceptions.EtAuthException;

@Repository
public class UsersRepositoryImpl implements UsersRepository {

  private static final String SQL_CREATE = "INSERT INTO users(id, first_name, last_name, email, password) VALUES(NEXTVAL('users_seq'), ?, ?, ?, ?)";
  private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM users WHERE email = ?";
  // private static final String SQL_FIND_BY_EMAIL_AND_PASSWORD = "SELECT * FROM users WHERE email = ? AND password = ?";
  private static final String SQL_FIND_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
  private static final String SQL_FIND_BY_ID = "SELECT * FROM users WHERE id = ?";

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Override
  public Integer create(String firstName, String lastName, String email, String password) throws EtAuthException {
    try {
      // System.out.println("System.getproperty(java.classpath) " + System.getProperty("java.class.path"));
      String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
      // System.out.println("hashedPassword: " + hashedPassword);
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, firstName);
        ps.setString(2, lastName);
        ps.setString(3, email);
        ps.setString(4, hashedPassword);
        return ps;
      }, keyHolder);
      return (Integer) keyHolder.getKeys().get("id");
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      throw new EtAuthException("Invalid details, failed to create account");
    }
  }

  @Override
  public Users findByEmailAndPassword(String email, String password) throws EtAuthException {
    try {
      @SuppressWarnings("deprecation")
      Users users = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, new Object[] { email }, userRowMapper);
      if(!BCrypt.checkpw(password, users.getPassword())) {
        throw new EtAuthException("Invalid email/password");
      }
      return users;
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      throw new EtAuthException("Invalid details, login failed: " + e.getMessage());
    }
  }

  @Override
  public Integer getCountByEmail(String email) {
    // TODO Auto-generated method stub
    return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, Integer.class, email);
  }

  @SuppressWarnings("deprecation")
  @Override
  public Users findById(Integer id) {
    // TODO Auto-generated method stub
    return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[] { id }, userRowMapper);
  }

  private RowMapper<Users> userRowMapper = ((rs,rowNum) -> {
    return new Users(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), rs.getString("password"));
  });
}
