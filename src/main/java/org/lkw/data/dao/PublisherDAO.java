package org.lkw.data.dao;

import org.lkw.data.util.DBConnection;
import org.lkw.model.Publisher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PublisherDAO {
    public List<Publisher> getAllPublishers() {
        List<Publisher> publishers = new ArrayList<>();
        String sql = "SELECT * FROM publishers";
        
        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Publisher publisher = new Publisher();
                publisher.setPublisherId(rs.getInt("publisher_id"));
                publisher.setName(rs.getString("publisher_name"));
                publishers.add(publisher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publishers;
    }
    
    public Publisher getPublisherById(int id) {
        String sql = "SELECT * FROM publishers WHERE publisher_id = ?";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Publisher publisher = new Publisher();
                publisher.setPublisherId(rs.getInt("publisher_id"));
                publisher.setName(rs.getString("publisher_name"));
                return publisher;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Publisher getPublisherByName(String name) {
        String sql = "SELECT * FROM publishers WHERE publisher_name = ?";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Publisher publisher = new Publisher();
                publisher.setPublisherId(rs.getInt("publisher_id"));
                publisher.setName(rs.getString("publisher_name"));
                return publisher;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
} 