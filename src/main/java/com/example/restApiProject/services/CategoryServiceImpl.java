package com.example.restApiProject.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.restApiProject.domain.Category;
import com.example.restApiProject.repositories.CategoryRepository;

@Service
@Transactional 
public class CategoryServiceImpl implements CategoryService {

  @Autowired
  CategoryRepository categoryRepository;
  
  @Override
  public List<Category> fetchAllCategories(Integer userId) {
    return categoryRepository.fetchAllCategories(userId);
  }

  @Override
  public Category fetchCategoryById(Integer userId, Integer categoryId) {
    return categoryRepository.findById(userId, categoryId);
  }

  @Override
  public Category addCategory(Integer userId, String title, String description) {
    int categoryId = categoryRepository.create(userId, title, description);
    return categoryRepository.findById(userId, categoryId);
  }

  @Override
  public void updateCategory(Integer userId, Integer categoryId, Category category) {
    categoryRepository.update(userId, categoryId, category);
  }

  @Override
  public void removeCategoryWithAllTransactions(Integer userId, Integer categoryId) {
    categoryRepository.removeById(userId, categoryId);
  }
}
