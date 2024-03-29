package com.example.restApiProject.services;

import com.example.restApiProject.domain.Users;
import com.example.restApiProject.exceptions.EtAuthException;

public interface UsersService {
  Users validateUser(String email, String password) throws EtAuthException;
  Users registerUser(String firstName, String lastName, String email, String password) throws EtAuthException;
}
