package Model;

public class User {
    protected int id;


    boolean isInstructor(){
        int type = id;
        while(type >= 100) type /=10;

        if(type == 10) return true;
        else return false;
    }
}