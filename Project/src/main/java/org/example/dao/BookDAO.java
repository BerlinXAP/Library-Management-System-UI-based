package org.example.dao;

import org.example.model.Book;
import org.example.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public List<Book> getAll() throws SQLException {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT id, title, author, copies, issued FROM books ORDER BY id";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("copies"),
                        rs.getInt("issued") == 1
                ));
            }
        }
        return list;
    }

    public void add(Book b) throws SQLException {
        String sql = "INSERT INTO books(id, title, author, copies, issued) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, b.getId());
            ps.setString(2, b.getTitle());
            ps.setString(3, b.getAuthor());
            ps.setInt(4, b.getCopies());
            ps.setInt(5, b.isIssued() ? 1 : 0);
            ps.executeUpdate();
        }
    }

    public void update(Book b) throws SQLException {
        String sql = "UPDATE books SET title=?, author=?, copies=?, issued=? WHERE id=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getTitle());
            ps.setString(2, b.getAuthor());
            ps.setInt(3, b.getCopies());
            ps.setInt(4, b.isIssued() ? 1 : 0);
            ps.setInt(5, b.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM books WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
