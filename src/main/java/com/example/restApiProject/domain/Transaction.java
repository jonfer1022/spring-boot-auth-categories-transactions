package com.example.restApiProject.domain;

public class Transaction {

  private Integer transactionId;
  private Integer userId;
  private Integer categoryId;
  private Double amount;
  private String note;
  private Long transactionDate;

  public Transaction(Integer transactionId, Integer userId, Integer categoryId, Double amount, String note, Long transactionDate) {
    this.transactionId = transactionId;
    this.userId = userId;
    this.categoryId = categoryId;
    this.amount = amount;
    this.note = note;
    this.transactionDate = transactionDate;
  }

  public Integer getTransactionId() {
    return transactionId;
  }

  public Integer getUserId() {
    return userId;
  }

  public Integer getCategoryId() {
    return categoryId;
  }

  public Double getAmount() {
    return amount;
  }

  public String getNote() {
    return note;
  }

  public Long getTransactionDate() {
    return transactionDate;
  }
}
