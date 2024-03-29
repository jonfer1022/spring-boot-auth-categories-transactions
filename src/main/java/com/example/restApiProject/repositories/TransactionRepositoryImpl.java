package com.example.restApiProject.repositories;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.restApiProject.domain.Transaction;
import com.example.restApiProject.exceptions.EtBadRequestException;
import com.example.restApiProject.exceptions.EtResourceNotFoundException;


@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

  private static final String SQL_CREATE = "INSERT INTO transactions(id, user_id, category_id, amount, note, transaction_date) VALUES(NEXTVAL('transactions_seq'), ?, ?, ?, ?, ?)";
  private static final String SQL_FIND_BY_ID = "SELECT * FROM transactions WHERE id = ? AND user_id = ? AND category_id = ?";
  private static final String SQL_FIND_ALL = "SELECT * FROM transactions WHERE user_id = ? AND category_id = ?";
  private static final String SQL_UPDATE = "UPDATE transactions SET amount = ?, note = ?, transaction_date = ? WHERE id = ? AND user_id = ? AND category_id = ?";
  private static final String SQL_DELETE = "DELETE FROM transactions WHERE id = ? AND user_id = ? AND category_id = ?";

  @Autowired
  JdbcTemplate jdbcTemplate;

  @SuppressWarnings("deprecation")
  @Override
  public List<Transaction> fetchAllTransactions(Integer userId, Integer categoryId) {
    return jdbcTemplate.query(SQL_FIND_ALL, new Object[] { userId, categoryId }, transactionRowMapper);
  }

  @SuppressWarnings("deprecation")
  @Override
  public Transaction fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId)
      throws EtResourceNotFoundException {
    try {
      return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[] { transactionId, userId, categoryId }, transactionRowMapper);
    } catch (Exception e) {
      System.out.println("Error fetchTransactionById: " + e.getMessage());
      throw new EtResourceNotFoundException("Transaction not found");
    }
  }

  @Override
  public Integer create(Integer userId, Integer categoryId, Double amount, String note, Long transactionDate)
      throws EtBadRequestException {
    try {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, userId);
        ps.setInt(2, categoryId);
        ps.setDouble(3, amount);
        ps.setString(4, note);
        ps.setLong(5, transactionDate);
        return ps;
      }, keyHolder);
      return (Integer) keyHolder.getKeys().get("id");
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      throw new EtResourceNotFoundException("Transaction could not be created");
    }
  }

  @Override
  public void updateTransaction(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction)
      throws EtBadRequestException {
    try {
      jdbcTemplate.update(SQL_UPDATE, transaction.getAmount(), transaction.getNote(), transaction.getTransactionDate(), transactionId, userId, categoryId);
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      throw new EtResourceNotFoundException("Transaction could not be updated");
    }
  }

  @Override
  public void removeTransaction(Integer userId, Integer categoryId, Integer transactionId)
      throws EtResourceNotFoundException {
    int count = jdbcTemplate.update(SQL_DELETE, new Object[] { transactionId, userId, categoryId });
    if (count == 0) throw new EtResourceNotFoundException("Transaction not found");
    else if (count < 0) throw new EtResourceNotFoundException("Transaction could not be deleted");
  }

  private RowMapper<Transaction> transactionRowMapper = ((rs,rowNum) -> {
    return new Transaction(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("category_id"), rs.getDouble("amount"), rs.getString("note"), rs.getLong("transaction_date"));
  });
}
