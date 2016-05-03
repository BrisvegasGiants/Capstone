package bvgiants.diary3;

/**
 * Created by kenst on 3/05/2016.
 * Simple class to create a user object.
 *
 */
public class User {

    public String email;
    public String alias;

    public int id;
    public String firstName;
    public String lastName;
    public int height;
    public int weight;
    public int age;
    public String gender;
    
    public User (String email, String alias){
        this.email = email;
        this.alias = alias;
    }

    //Polymorphic user object to help deal with creating user variables
    public User (int id,String firstName, String lastName, int height, int weight, int age,
                 String gender){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.gender = gender;
    }
    
    public String toString(){
        return email + " " + alias;
    }

    //Method to return a string capable of being stored in the UserTraitsTxtFile
    public String dbWriteUserTraits(){
        return id + " " + firstName + " " + lastName + " " + height + " " + weight + " " + age
                + " " + gender + "\n";
    }
}
