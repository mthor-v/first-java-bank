package model;

import java.sql.*;

/**
 *
 * @author migue
 */
public class DBManager {

    private final String driver;
    private final String url;
    private final String user;
    private final String password;
    private Connection conn = null;
    private Statement statement = null;

    public DBManager() {
        driver = "org.postgresql.Driver";
        url = "jdbc:postgresql://localhost:5432/bank_db";
        user = "postgres";
        password = "123Torre$";
    }

    public void dbConnect() throws Exception {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            throw new Exception("Error in database connection");
        }
    }

    public boolean getState() throws SQLException {
        return !conn.isClosed();
    }

    public void dbDisconnect() throws Exception {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new Exception("Error in database disconnection");
        }
    }

    public ResultSet dbExecuteSelect(String query) throws Exception {
        try {
            dbConnect();
            statement = conn.createStatement();
            return statement.executeQuery(query);
        } catch (Exception e) {
            throw new Exception("Error in database select");
        } finally {
            if (getState()) {
                dbDisconnect();
            }
        }
    }

    public int dbExecuteUpdate(String query) throws Exception {
        try {
            dbConnect();
            statement = conn.createStatement();
            return statement.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if(e.getMessage().contains("duplicada")){
                throw new Exception("Ya existe un usuario con la misma identificación.");
            } else throw new Exception("Error in database update");
        } finally {
            if (getState()) {
                dbDisconnect();
            }
        }
    }
}
