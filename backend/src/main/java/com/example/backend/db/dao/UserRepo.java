package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.models.User;

public class UserRepo implements UserRepoInterface{

    @Override
    public User getUser(User user) {

        if(user.getPassword().equals("")) {
            return getUserByUsername(user.getUsername());
        }
        
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from users where username=? and password=?");
        ) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            

            ResultSet rs = pstmt.executeQuery();

            User foundUser = null;
            if (rs.next()) {
                if(!user.getType().equals("admin") && rs.getString("type").equals("admin")) {
                    return null;
                }
                if(rs.getBoolean("pending")) {
                    return null;
                }
                foundUser = new User(
                    rs.getString("username"), 
                    rs.getString("password"), 
                    rs.getString("firstname"), 
                    rs.getString("lastname"),
                    rs.getString("number"),
                    rs.getString("email"),
                    rs.getString("type"),
                    rs.getString("firmName"),
                    rs.getString("firmAdress"),
                    rs.getString("companyRegistrationNumber"),
                    rs.getString("taxIdentificationNumber"),
                    rs.getBoolean("pending"));
                
            }

            return foundUser;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByUsername(String username) {
         try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from users where username=?");
        ) {
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            User foundUser = null;
            if (rs.next()) {
                if(rs.getBoolean("pending")) {
                    return null;
                }
                foundUser = new User(
                    rs.getString("username"), 
                    rs.getString("password"), 
                    rs.getString("firstname"), 
                    rs.getString("lastname"),
                    rs.getString("number"),
                    rs.getString("email"),
                    rs.getString("type"),
                    rs.getString("firmName"),
                    rs.getString("firmAdress"),
                    rs.getString("companyRegistrationNumber"),
                    rs.getString("taxIdentificationNumber"),
                    rs.getBoolean("pending"));
                
            }

            return foundUser;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int postUser(User user) {
        int numOfManagersForCompany = getNumOfManagersForCompany(user.getCompanyRegistrationNumber());
        if(numOfManagersForCompany == 2) {
            return 0;
        }
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("insert into users (username, password, firstname, lastname, number, email, type, firmName, firmAdress, companyRegistrationNumber, taxIdentificationNumber, pending) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, true)");
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

    public int getNumOfManagersForCompany(String companyRegistrationNumber) {
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from users where companyRegistrationNumber = ?");
        ) {
            pstmt.setString(1, companyRegistrationNumber);

            int res = 0;

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                res++;
            }

            return res;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public List<User> getPendingUsers() {
         try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from users where pending = 1");
        ) {

            ResultSet rs = pstmt.executeQuery();

            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(new User(
                    rs.getString("username"), 
                    rs.getString("password"), 
                    rs.getString("firstname"), 
                    rs.getString("lastname"),
                    rs.getString("number"),
                    rs.getString("email"),
                    rs.getString("type"),
                    rs.getString("firmName"),
                    rs.getString("firmAdress"),
                    rs.getString("companyRegistrationNumber"),
                    rs.getString("taxIdentificationNumber"),
                    rs.getBoolean("pending")));
                
            }

            return users;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updateUser(User user) {
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("update users set password = ?, firstname = ?, lastname = ?, number = ?, email = ?, firmname = ?, firmAdress = ?, companyRegistrationNumber = ?, taxidentificationNumber = ?, pending = ? where username = ?");
        ) {

            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getFirstname());
            pstmt.setString(3, user.getLastname());
            pstmt.setString(4, user.getNumber());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getFirmName());
            pstmt.setString(7, user.getFirmAdress());
            pstmt.setString(8, user.getCompanyRegistrationNumber());
            pstmt.setString(9, user.getTaxIdentificationNumber());
            pstmt.setBoolean(10, user.isPending());
            pstmt.setString(11, user.getUsername());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
}
