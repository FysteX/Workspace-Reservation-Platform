package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.backend.db.DB;
import com.example.backend.models.User;

public class UserRepo implements UserRepoInterface{

    @Override
    public User getUser(User user) {
        
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from users where username=? and password=?");
        ) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            

            ResultSet rs = pstmt.executeQuery();

            User foundUser = null;
            if (rs.next()) {
                foundUser = new User(
                    rs.getString("username"), 
                    rs.getString("password"), 
                    rs.getString("firstname"), 
                    rs.getString("lastname"),
                    rs.getString("number"),
                    rs.getString("email"),
                    rs.getString("type"),
                    rs.getString("firm name"),
                    rs.getString("firm adress"),
                    rs.getString("companyRegistrationNumber"),
                    rs.getString("taxIdentificationNumber"));
                
            }
            return foundUser;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int postUser(User user) {
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("insert into users (username, password, firstname, lastname, number, email, type, firmName, firmAdress, companyRegistrationNumber, taxIdentificationNumber) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        ) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFirstname());
            pstmt.setString(4, user.getLastname());
            pstmt.setString(5, user.getNumber());
            pstmt.setString(6, user.getEmail());
            pstmt.setString(7, user.getType());
            pstmt.setString(8, user.getFirmName());
            pstmt.setString(9, user.getFirmAdress());
            pstmt.setString(10, user.getCompanyRegistrationNumber());
            pstmt.setString(11, user.getTaxIdentificationNumber());

            pstmt.executeUpdate();
            return 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
}
