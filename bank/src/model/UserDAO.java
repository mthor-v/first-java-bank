package model;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import javax.swing.JOptionPane;
import view.CreateUserPanel;
import view.DeleteUserPanel;
import view.MainJFrame;

/**
 *
 * @author migue
 */
public class UserDAO {

    DBManager dbManager = new DBManager();

    public List getUsersList() throws Exception {
        List<User> usersData = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (ResultSet rs = dbManager.dbExecuteSelect(query)) {
            while (rs.next()) {
                User user = new User();
                user.setName(rs.getString("name"));
                user.setUid(rs.getString("uid"));
                usersData.add(user);
            }
        }
        return usersData;
    }

    public User getUserByUID(String uid) throws Exception {
        User user = null;
        String query = String.format("SELECT * FROM users WHERE uid = '%s'", uid);
        try (ResultSet rs = dbManager.dbExecuteSelect(query)) {
            if (rs.next()) {
                user = new User();
                user.setUid(rs.getString("uid"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setBalance(rs.getInt("balance"));
            }
        }
        return user;
    }

    public int createUser(User user) {
        String query = String.format("INSERT INTO users (name, uid, password, balance) VALUES ('%s','%s','%s',%d)",
                user.getName(), user.getUid(), user.getPassword(), user.getBalance());
        System.out.println(query);
        try {
            int rowsAffected = dbManager.dbExecuteUpdate(query);
            System.out.println(rowsAffected + " filas insertadas");
            return rowsAffected;
        } catch (Exception e) {
            System.out.println("CreateDAO: " + e.getMessage());
            JOptionPane.showMessageDialog(new CreateUserPanel(), e.getMessage());
        }
        return 0;
    }

    public int updatePassword(String uid, String oldPwd, String newPwd) {
        try {
            String query = String.format("UPDATE users SET password = '%s' WHERE uid = '%s'",
                    newPwd, uid);
            System.out.println(query);
            if (checkPassword(uid, oldPwd)){
                int rowsAffected = dbManager.dbExecuteUpdate(query);
                    System.out.println("Contraseña actualizada.");
                    return rowsAffected;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int deleteUser(String uid, String password) {
        try {
            String query = String.format("DELETE FROM users WHERE uid = '%s'", uid);
            System.out.println(query);
            User user = getUserByUID(uid);
            if (user != null) {
                if (user.getPassword().equals(password)) {
                    int rowsAffected = dbManager.dbExecuteUpdate(query);
                    System.out.println(rowsAffected + " registros eliminados.");
                    return rowsAffected;
                } else {
                    JOptionPane.showMessageDialog(new DeleteUserPanel(), "Contraseña Incorrecta");
                }
            } else {
                JOptionPane.showMessageDialog(new DeleteUserPanel(), String.format("Usuario con uid %s no existe.", uid));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(new DeleteUserPanel(), e.getMessage());
        }
        return 0;
    }

    public boolean checkPassword(String uid, String pwd) {
        try {
            User user = getUserByUID(uid);
            if (user != null) {
                if (user.getPassword().equals(pwd)) {
                    return true;
                }else {
                    JOptionPane.showMessageDialog(new MainJFrame(), "Contraseña Incorrecta");
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(new MainJFrame(), String.format("Usuario con uid %s no existe.", uid));
                throw new Exception("Usuario no existe.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
