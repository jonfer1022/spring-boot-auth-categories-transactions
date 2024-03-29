package com.example.restApiProject.repositories;

import com.example.restApiProject.domain.Users;
import com.example.restApiProject.exceptions.EtAuthException;

public interface UsersRepository {
  Integer create(String firstName, String lastName, String email, String password) throws EtAuthException;

  Users findByEmailAndPassword(String email, String password) throws EtAuthException;

  Integer getCountByEmail(String email);

  Users findById(Integer userId);
}
