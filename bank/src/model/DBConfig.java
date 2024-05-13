package model;

/**
 *
 * @author migue
 */
public class DBConfig {
    
    private String driver = null;
    private String url = null;
    private String user = null;
    private String password = null;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String Driver) {
        this.driver = Driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
