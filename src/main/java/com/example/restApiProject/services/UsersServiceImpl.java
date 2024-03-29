package com.example.restApiProject.services;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.restApiProject.domain.Users;
import com.example.restApiProject.exceptions.EtAuthException;
import com.example.restApiProject.repositories.UsersRepository;

@Service
@Transactional
public class UsersServiceImpl implements UsersService{

  @Autowired
  UsersRepository usersRepository;

  @Override
  public Users validateUser(String email, String password) throws EtAuthException {
    // TODO Auto-generated method stub
    if(email != null) email = email.toLowerCase();
    if(password != null) password = password.toLowerCase();

    return usersRepository.findByEmailAndPassword(email, password);
  }

  @Override
  public Users registerUser(String firstName, String lastName, String email, String password) throws EtAuthException {
    // TODO Auto-generated method stub
    Pattern pattern = Pattern.compile("^(.+)@(.+)$");

    if(email != null) email = email.toLowerCase();

    if(!pattern.matcher(email).matches()) {
      throw new EtAuthException("Invalid email format");
    }

    if(firstName.length() < 2) {
      throw new EtAuthException("Invalid first name");
    }

    if(lastName.length() < 2) {
      throw new EtAuthException("Invalid last name");
    }

    if(password.length() < 5) {
      throw new EtAuthException("Invalid password");
    }

    if(email.length() < 5) {
      throw new EtAuthException("Invalid email");
    }

    Integer count = usersRepository.getCountByEmail(email);

    if(count > 0) {
      throw new EtAuthException("Email already in use");
    }

    Integer userId = usersRepository.create(firstName, lastName, email, password);

    return usersRepository.findById(userId);
  }
}
