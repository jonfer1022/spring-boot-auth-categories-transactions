package com.example.restApiProject.domain;

public class Category {

  private Integer categoryId;
  private Integer userId;
  private String title;
  private String description;
  private Double totalExpense;

  public Category(Integer categoryId, Integer userId, String title, String description, Double totalExpense) {
    this.categoryId = categoryId;
    this.userId = userId;
    this.title = title;
    this.description = description;
    this.totalExpense = totalExpense;
  }

  public Integer getCategoryId() {
    return categoryId;
  }

  public Integer getUserId() {
    return userId;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public Double getTotalExpense() {
    return totalExpense;
  }

  
}
