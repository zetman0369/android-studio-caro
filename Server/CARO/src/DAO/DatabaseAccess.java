/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Game;
import Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FPTSHOP
 */
public class DatabaseAccess {
    public void register(User user) {
        Connection con = null;
        try {
            con = Dao.getConnection();
            PreparedStatement ps = con.prepareStatement("insert into user(Username,Password,Email,Phone,Status) values (?,?,?,?,?)");
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setInt(5, user.getStatus());
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public boolean isExist(User user) {
        Connection con = null;
        try {
            con = Dao.getConnection();
            PreparedStatement ps = con.prepareStatement("select * from user where Username =?");
            ps.setString(1, user.getUsername());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public User logIn(User user) {
        Connection con = null;
        try {
            con = Dao.getConnection();
            PreparedStatement ps = con.prepareStatement("select * from user where Username =? and Password =?");
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt(1));
                u.setUsername(rs.getString(2));
                u.setPassword(rs.getString(3));
                u.setEmail(rs.getString(4));
                u.setPhone(rs.getString(5));
                u.setListFriend(rs.getString(8));
                u.setPoint(rs.getInt(6));
                u.setStatus(1);
                this.setStatus(u.getId(), 1);
                return u;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public boolean logOut(User user) {
        Connection con = null;
        try {
            con = Dao.getConnection();
            PreparedStatement ps = con.prepareStatement("select * from user where id =?");
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                this.setStatus(rs.getInt(1), 0);
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public void setStatus(int id, int status) {
        try {
            Connection con = Dao.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE user SET Status=? where id=?");
            ps.setInt(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    public int createGame(Game g) {
//        try {
//            Connection con = Dao.getConnection();
//            PreparedStatement ps=con.prepareStatement("INSERT INTO game (User1) VALUES (?)");
//            ps.setInt(1, g.getUser1());
//            ps.executeUpdate();
//            con.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        
//    }
    public Boolean joinGame(Game g) {
        Connection con = null;
        try {
            con = Dao.getConnection();
            PreparedStatement ps = con.prepareStatement("select * from game where id =? and User2 =?");
            ps.setInt(1, g.getId());
            ps.setInt(2, g.getUser2());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public static Boolean endGame(Game g) {
        Connection con = null;
        try {
            con = Dao.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE game SET Result=? where id=?");
            ps.setInt(2, g.getId());
            ps.setInt(1, g.getResult());
            int d = ps.executeUpdate();
            if (d != 0) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public boolean searchFriend(User user) {
        Connection con = null;
        try {
            con = Dao.getConnection();
            PreparedStatement ps = con.prepareStatement("select * from user where Username like ?");
            ps.setString(1, user.getUsername());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public Boolean addFriend(User u1, User u2) {
        Connection con = null;
        try {
            con = Dao.getConnection();
            PreparedStatement ps1 = con.prepareStatement("UPDATE user SET ListFriend=? where id=?");

            String idU2 = String.valueOf(u2.getId());
            idU2 += ",";
            String idU1 = String.valueOf(u1.getId());
            idU1 += ",";

            ps1.setString(1, idU2);
            ps1.setInt(2, u1.getId());

            int d = ps1.executeUpdate();

            // ----------------------------------------------------
            ps1.setString(1, idU1);
            ps1.setInt(2, u2.getId());

            int r = ps1.executeUpdate();
            if (d != 0 && r != 0) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public ArrayList<User> getListFriend(User user) {
        Connection con = null;
        ArrayList<User> users = new ArrayList<>();
        try {
            con = Dao.getConnection();
            PreparedStatement ps = con.prepareStatement("select ListFriend from user where id =?");
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String s = rs.getString("ListFriend");
                String[] list = s.split(",");
                for (String string : list) {
                    User u = new User();
                    u.setId(Integer.parseInt(string));
                    users.add(u);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return users;
    }

    public ArrayList<User> getCharts() {
        Connection con = null;
        ArrayList<User> users = new ArrayList<>();
        try {
            con = Dao.getConnection();
            PreparedStatement ps = con.prepareStatement("select * from user");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt(1));
                u.setUsername(rs.getString(2));
                u.setPassword(rs.getString(3));
                u.setEmail(rs.getString(4));
                u.setPhone(rs.getString(5));
                u.setListFriend(rs.getString(8));
                u.setPoint(rs.getInt(6));
                u.setStatus(rs.getInt(7));
                users.add(u);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return users;
    }

   

}
