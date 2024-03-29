package com.example.restApiProject.repositories;

import java.util.List;

import com.example.restApiProject.domain.Category;
import com.example.restApiProject.exceptions.EtBadRequestException;
import com.example.restApiProject.exceptions.EtResourceNotFoundException;

public interface CategoryRepository {
  List<Category> fetchAllCategories(Integer userId) throws EtResourceNotFoundException;

  Category findById(Integer userId, Integer categoryId) throws EtResourceNotFoundException;

  Integer create(Integer userId, String title, String description) throws EtBadRequestException;

  void update(Integer userId, Integer categoryId, Category category) throws EtBadRequestException;

  void removeById(Integer userId, Integer categoryId) throws EtResourceNotFoundException;
}
