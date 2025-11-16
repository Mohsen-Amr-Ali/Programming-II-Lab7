package Model;

import util.PasswordHasher;

public abstract class User {
    protected String username;
    protected int user_id;
    protected String role;
    protected String email;
    protected String hashed_password;

    public User(int user_id, String username, String email, String hashed_password, String role) {
        this.user_id = user_id;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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
