package com.example.restApiProject.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.restApiProject.domain.Transaction;
import com.example.restApiProject.exceptions.EtBadRequestException;
import com.example.restApiProject.exceptions.EtResourceNotFoundException;
import com.example.restApiProject.repositories.TransactionRepository;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

  @Autowired
  private TransactionRepository  transactionRepository;

  @Override
  public List<Transaction> fetchAllTransactions(Integer userId, Integer categoryId) {
    return transactionRepository.fetchAllTransactions(userId, categoryId);
  }

  @Override
  public Transaction fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId)
      throws EtResourceNotFoundException {
    return transactionRepository.fetchTransactionById(userId, categoryId, transactionId);
  }

  @Override
  public Transaction addTransaction(Integer userId, Integer categoryId, Double amount, String note, long transactionDate) throws EtBadRequestException {
    int transactionId = transactionRepository.create(userId, categoryId, amount, note, transactionDate);
    return transactionRepository.fetchTransactionById(userId, categoryId, transactionId);
  }

  @Override
  public void updateTransaction(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction)
      throws EtBadRequestException {
    transactionRepository.updateTransaction(userId, categoryId, transactionId, transaction);
  }

  @Override
  public void removeTransaction(Integer userId, Integer categoryId, Integer transactionId)
      throws EtResourceNotFoundException {
    transactionRepository.removeTransaction(userId, categoryId, transactionId);
  }

}
