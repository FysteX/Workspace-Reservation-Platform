package com.example.backend.db.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.example.backend.db.DB;
import com.example.backend.models.User;

@Service
public class UserRepo implements UserRepoInterface{

    public String hashPassword(String password) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        return null;
    }
}       

    @Override
    public User getUser(User user) {

        if(user.getPassword().equals("")) {
            return getUserByUsername(user.getUsername());
        }
        
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from users where username=?");
        ) {
            pstmt.setString(1, user.getUsername());

            ResultSet rs = pstmt.executeQuery();

            User foundUser = null;
            if (rs.next()) {
                if(!user.getType().equals("admin") && rs.getString("type").equals("admin")) {
                    return null;
                }
                if(rs.getBoolean("pending")) {
                    return null;
                }
                if(!user.getType().equals("admin") && !hashPassword(user.getPassword()).equals(rs.getString("password"))) {
                    return null;
                }
                if(user.getType().equals("admin") && !user.getPassword().equals(rs.getString("password"))) {
                    return null;
                }
                foundUser = new User(
                    rs.getString("username"), 
                    user.getPassword(), 
                    rs.getString("firstname"), 
                    rs.getString("lastname"),
                    rs.getString("number"),
                    rs.getString("email"),
                    rs.getString("type"),
                    rs.getString("firmName"),
                    rs.getString("firmAdress"),
                    rs.getString("companyRegistrationNumber"),
                    rs.getString("taxIdentificationNumber"),
                    rs.getBoolean("pending"),
                    rs.getInt("idPicture"));
                
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
                    rs.getBoolean("pending"),
                    rs.getInt("idPicture"));
                
            }

            return foundUser;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int postUser(User user) {
        if(user.getType().equals("manager")) {
            int numOfManagersForCompany = getNumOfManagersForCompany(user.getCompanyRegistrationNumber());
            if(numOfManagersForCompany == 2) {
                return 0;
            } 
        }
        if(user.getIdPicture() != -1) {
            List<User> users = getAllUsers();
            int max = 0;
            for(User u: users) {
                int t = u.getIdPicture();
                if(t > max) {
                    max = t;
                }
            }
            user.setIdPicture(max + 1);       
        }
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("insert into users (username, password, firstname, lastname, number, email, type, firmName, firmAdress, companyRegistrationNumber, taxIdentificationNumber, pending, idPicture) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, true, ?)");
        ) {
            String hashedPassword = hashPassword(user.getPassword());

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, user.getFirstname());
            pstmt.setString(4, user.getLastname());
            pstmt.setString(5, user.getNumber());
            pstmt.setString(6, user.getEmail());
            pstmt.setString(7, user.getType());
            pstmt.setString(8, user.getFirmName());
            pstmt.setString(9, user.getFirmAdress());
            pstmt.setString(10, user.getCompanyRegistrationNumber());
            pstmt.setString(11, user.getTaxIdentificationNumber());
            pstmt.setInt(12, user.getIdPicture());

            pstmt.executeUpdate();
            return user.getIdPicture();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -2;
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
                    rs.getBoolean("pending"),
                    rs.getInt("idPicture")));
                
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
            PreparedStatement pstmt = conn.prepareStatement("update users set password = ?, firstname = ?, lastname = ?, number = ?, email = ?, firmname = ?, firmAdress = ?, companyRegistrationNumber = ?, taxidentificationNumber = ?, pending = ?, idPicture = ? where username = ?");
        ) {
            String hashedPassword;

            if(user.getPassword().length() > 12) {
                hashedPassword = user.getPassword();
            } else { 
                hashedPassword = hashPassword(user.getPassword());
            }
            
            if(user.getIdPicture() == -1) {
                List<User> users = getAllUsers();
                int max = 0;
                for(User u: users) {
                    int t = u.getIdPicture();
                    if(t > max) {
                        max = t;
                    }
                }
                user.setIdPicture(max + 1);
                System.out.println(user.getIdPicture());
            }

            pstmt.setString(1, hashedPassword);
            pstmt.setString(2, user.getFirstname());
            pstmt.setString(3, user.getLastname());
            pstmt.setString(4, user.getNumber());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getFirmName());
            pstmt.setString(7, user.getFirmAdress());
            pstmt.setString(8, user.getCompanyRegistrationNumber());
            pstmt.setString(9, user.getTaxIdentificationNumber());
            pstmt.setBoolean(10, user.isPending());
            pstmt.setInt(11, user.getIdPicture());
            pstmt.setString(12, user.getUsername());

            pstmt.executeUpdate();

            return user.getIdPicture();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public List<User> getAllUsers() {
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from users");
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
                    rs.getBoolean("pending"),
                    rs.getInt("idPicture")));
                
            }

            return users;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int deleteUser(User user) {
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("delete from users where username = ?");
        ) {

            pstmt.setString(1, user.getUsername());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public User getUserByEmail(User user) {

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from users where email = ?");
        ) {
            pstmt.setString(1, user.getEmail());

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
                    rs.getBoolean("pending"),
                    rs.getInt("idPicture"));
                
            }
            return foundUser;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserByUsername(User user) {
       try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from users where username = ?");
        ) {
            pstmt.setString(1, user.getUsername());

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
                    rs.getBoolean("pending"),
                    rs.getInt("idPicture"));
                
            }
            return foundUser;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int changePassword(User user) {
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("update users set password = ? where username = ?");
        ) {
            String hashedPassword = hashPassword(user.getPassword()); 

            pstmt.setString(1, hashedPassword);
            pstmt.setString(2, user.getUsername());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
