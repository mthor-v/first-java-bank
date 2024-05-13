package model;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

/**
 *
 * @author migue
 */
public class DBManager {

    //private static final String dbConfigFilePath = "utils/db_config.json";
    private static final Gson gson = new Gson();
    private String driver;
    private String url;
    private String user;
    private String password;
    private Connection conn = null;
    private Statement statement = null;

    public DBManager() {
        try {
            String currentWorkingDirectory = System.getProperty("user.dir");
            String dbConfigFilePath = currentWorkingDirectory + "\\src\\utils\\db_config.json";
            System.out.println(dbConfigFilePath);
            DBConfig config = gson.fromJson(new FileReader(dbConfigFilePath), DBConfig.class);
            driver = config.getDriver();
            url = config.getUrl();
            user = config.getUser();
            password = config.getPassword();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        
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
