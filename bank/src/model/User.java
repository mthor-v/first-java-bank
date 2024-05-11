package model;

/**
 *
 * @author migue
 */
public class User {
    
    private String name = null;
    private String uid = null;
    private String password = null;
    private Integer balance = null;

    public User() {
    }

    public User(String name, String uid, String password) {
        this.name = name;
        this.uid = uid;
        this.password = password;
        balance = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User{" + "name=" + name + ", uid=" + uid + ", password=" + password + ", balance=" + balance + '}';
    }
    
}
