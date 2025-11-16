package Model;

import util.PasswordHasher;

public abstract class User {
    protected String username;
    protected int id;
    protected String role;
    protected String email;
    protected String hashed_password;

    public User(int id, String username, String email, String hashed_password, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.hashed_password = hashed_password;
        this.role = role;
    }

    public User(String username, String email, String hashed_password, String role) {
        this.username = username;
        this.email = email;
        this.hashed_password = hashed_password;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashed_password() {
        return hashed_password;
    }

    public void setHashed_password(String hashed_password) {
        this.hashed_password = hashed_password;
    }


    public boolean authenticate(String password)
    {
        return PasswordHasher.hashPassword(password).equals(hashed_password);
    }
}
