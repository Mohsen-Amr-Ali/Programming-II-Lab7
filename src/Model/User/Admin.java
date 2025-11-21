package Model.User;

public class Admin extends User {

    public Admin(int id, String username, String email, String hashedPassword, USER_ROLE role) {
        super(id, username, email, hashedPassword, role);
    }

    public Admin(String username, String email, String hashedPassword, USER_ROLE role) {
        super(username, email, hashedPassword, role);
    }

}