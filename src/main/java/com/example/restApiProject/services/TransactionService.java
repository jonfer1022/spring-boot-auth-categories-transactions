package com.example.restApiProject.services;

import java.util.List;

import com.example.restApiProject.domain.Transaction;
import com.example.restApiProject.exceptions.EtBadRequestException;
import com.example.restApiProject.exceptions.EtResourceNotFoundException;

public interface TransactionService {

  List<Transaction> fetchAllTransactions(Integer userId, Integer categoryId);

  Transaction fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId) throws EtResourceNotFoundException;

  Transaction addTransaction(Integer userId, Integer categoryId, Double amount, String note, long transactionDate) throws EtBadRequestException;

  void updateTransaction(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction) throws EtBadRequestException;

  void removeTransaction(Integer userId, Integer categoryId, Integer transactionId) throws EtResourceNotFoundException;
}
