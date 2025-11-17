package util;

import javax.swing.*;

public class Validator {
    public static boolean checkEmail(String email)
    {
        return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }
    public static boolean checkPassword(String password)
    {
        return password.matches("^(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{6,}$");
    }
    public static boolean isFieldEmpty(String field)
    {
        return field.isEmpty();
    }
    public static boolean checkId(String id)
    {
        return id.matches("^(90|10)\\d{4}$");
    }
    public static boolean confirmPassword(String password, String confirmPass)
    {
        return password.equals(confirmPass);
    }
    public static boolean checkName(String name)
    {
        try {
            Integer.parseInt(name);
            return false;

        } catch (NumberFormatException e){
            return true;
        }
    }
}
