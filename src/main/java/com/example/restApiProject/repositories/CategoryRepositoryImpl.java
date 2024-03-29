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

import com.example.restApiProject.domain.Category;
import com.example.restApiProject.exceptions.EtBadRequestException;
import com.example.restApiProject.exceptions.EtResourceNotFoundException;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository{

  private static final String SQL_CREATE = "INSERT INTO categories(id, user_id, title, description) VALUES(NEXTVAL('categories_seq'), ?, ?, ?)";
  private static final String SQL_FIND_BY_ID = "SELECT categories.id, categories.user_id, categories.title, categories.description, COALESCE(SUM(transactions.amount), 0) AS total_expense FROM transactions RIGHT JOIN categories ON categories.id = transactions.category_id WHERE categories.id = ? AND categories.user_id = ? GROUP BY categories.id";
  private static final String SQL_FIND_ALL = "SELECT categories.id, categories.user_id, categories.title, categories.description, COALESCE(SUM(transactions.amount), 0) AS total_expense FROM transactions RIGHT JOIN categories ON categories.id = transactions.category_id WHERE categories.user_id = ? GROUP BY categories.id";
  private static final String SQL_UPDATE = "UPDATE categories SET title = ?, description = ? WHERE id = ? AND user_id = ?";
  private static final String SQL_DELETE = "DELETE FROM categories WHERE id = ? AND user_id = ?";
  private static final String SQL_DELETE_ALL_TRANSACTIONS = "DELETE FROM transactions WHERE category_id = ?";

  @Autowired
  JdbcTemplate jdbcTemplate;

  @SuppressWarnings("deprecation")
  @Override
  public List<Category> fetchAllCategories(Integer userId) {
    try {
      return jdbcTemplate.query(SQL_FIND_ALL, new Object[] { userId }, categoryRowMapper);
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      throw new EtResourceNotFoundException("Categories not found");
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public Category findById(Integer userId, Integer categoryId) {
    try {
      return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[] { categoryId, userId }, categoryRowMapper);
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      throw new EtResourceNotFoundException("Category not found");
    }
  }

  @Override
  public Integer create(Integer userId, String title, String description) {
    try {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, userId);
        ps.setString(2, title);
        ps.setString(3, description);
        return ps;
      }, keyHolder);
      return (Integer) keyHolder.getKeys().get("id");
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      throw new EtBadRequestException("Invalid details, failed to create category");
    }
  }

  @Override
  public void update(Integer userId, Integer categoryId, Category category) {
    try {
      jdbcTemplate.update(SQL_UPDATE, category.getTitle(), category.getDescription(), categoryId, userId);
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      throw new EtBadRequestException("Invalid details, failed to update category");
    }
  }

  @Override
  public void removeById(Integer userId, Integer categoryId) {
    try {
      this.removeAllTransactions(categoryId);
      jdbcTemplate.update(SQL_DELETE, categoryId, userId);
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      throw new EtResourceNotFoundException("Category not found");
    }
  }

  private void removeAllTransactions(Integer categoryId) {
    jdbcTemplate.update(SQL_DELETE_ALL_TRANSACTIONS, categoryId);
  }

  private RowMapper<Category> categoryRowMapper = ((rs,rowNum) -> {
    return new Category(rs.getInt("id"), rs.getInt("user_id"), rs.getString("title"), rs.getString("description"), rs.getDouble("total_expense"));
  });
}
