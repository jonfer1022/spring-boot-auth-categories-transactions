package com.example.restApiProject.resources;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restApiProject.domain.Transaction;
import com.example.restApiProject.services.TransactionService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/categories/{categoryId}/transactions")
public class TransactionResource {

  @Autowired
  private TransactionService transactionService;

  @PostMapping("")
  public ResponseEntity<Transaction> addTransaction(HttpServletRequest request, @PathVariable("categoryId") Integer categoryId ,@RequestBody Map<String, Object> transactionMap) {
    int userId = (Integer) request.getAttribute("userId");
    Double amount = Double.valueOf(transactionMap.get("amount").toString());
    String note = (String) transactionMap.get("note");
    long transactionDate = (Long) transactionMap.get("transactionDate");

    Transaction transaction = transactionService.addTransaction(userId, categoryId, amount, note, transactionDate);

    return new ResponseEntity<>(transaction, HttpStatus.CREATED);
  }
  
  @GetMapping("{transactionId}")
  public ResponseEntity<Transaction> getTransactionById(HttpServletRequest request, @PathVariable("categoryId") Integer categoryId, @PathVariable("transactionId") Integer transactionId) {
    int userId = (Integer) request.getAttribute("userId");
    Transaction transaction = transactionService.fetchTransactionById(userId, categoryId, transactionId);
    return new ResponseEntity<>(transaction, HttpStatus.OK);
  }

  @GetMapping("")
  public ResponseEntity<List<Transaction>> getAllTransactions(HttpServletRequest request, @PathVariable("categoryId") Integer categoryId) {
    int userId = (Integer) request.getAttribute("userId");
    List<Transaction> transactions = transactionService.fetchAllTransactions(userId, categoryId);
    return new ResponseEntity<>(transactions, HttpStatus.OK);
  }

  @PutMapping("{transactionId}")
  public ResponseEntity<Map<String, Boolean>> updateTransaction(HttpServletRequest request, @PathVariable("categoryId") Integer categoryId, @PathVariable("transactionId") Integer transactionId, @RequestBody Transaction transaction) {
    int userId = (Integer) request.getAttribute("userId");
    transactionService.updateTransaction(userId, categoryId, transactionId, transaction);
    Map<String, Boolean> response = Map.of("success", true);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping("{transactionId}")
  public ResponseEntity<Map<String, Boolean>> removeTransaction(HttpServletRequest request, @PathVariable("categoryId") Integer categoryId, @PathVariable("transactionId") Integer transactionId) {
    int userId = (Integer) request.getAttribute("userId");
    transactionService.removeTransaction(userId, categoryId, transactionId);
    Map<String, Boolean> response = Map.of("success", true);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
