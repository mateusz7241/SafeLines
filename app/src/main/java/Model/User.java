package Model;

public class User {

    public String fullName, email,role;

    public User(){
    }

    public User(String fullName,String email, String role){
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public String toString(){
        return "Nazwa: " + fullName + "\n" + "Email: "+ email + "\n" +"Rola: " + role;
    }

}
