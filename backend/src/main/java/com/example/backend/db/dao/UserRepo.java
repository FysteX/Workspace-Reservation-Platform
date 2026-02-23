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
                if(user.isAdmin() && rs.getString("type") == "admin") {
                    foundUser = new User(
                    rs.getString("username"), 
                    rs.getString("password"), 
                    rs.getString("firstname"), 
                    rs.getString("lastname"),
                    rs.getString("number"),
                    rs.getString("email"),
                    rs.getString("type"),
                    true);
                } else if(!user.isAdmin() && rs.getString("type") != "admin") {
                    foundUser = new User(
                    rs.getString("username"), 
                    rs.getString("password"), 
                    rs.getString("firstname"), 
                    rs.getString("lastname"),
                    rs.getString("number"),
                    rs.getString("email"),
                    rs.getString("type"),
                    false);
                }
                
            }
            return foundUser;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
