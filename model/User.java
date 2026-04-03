package model;

import database.SQLconnect;
import java.util.Scanner;

public class User{
  protected String name;
  protected String username;
  protected String password;
  protected boolean state;
  protected String email;
  protected String type;

  public User(String name, String username, String password, boolean state, String email, String type){
    this.name = name;
    this.username = username;
    this.password = password;
    this.state = state;
    this.email = email;
    this.type = type;
  }
   
  /*================getters============================*/

  public String get_name(){
    return name;
  }

  public String get_username(){
    return username;
  }

  public String get_password(){
    return password;
  }

  public boolean get_state(){
    //honestly dont know if we would use this getter much 
    return state;
  }

  public String get_email(){
    return email;
  }

  public String get_type(){
    return type;
  }
  

  /*====================setters========================*/
  public void set_name(String name){
    this.name = name;
  }

  public void set_username(String username){
    this.username = username;
  }

  public void set_password(String password){
    this.password = password;
  }

  public void set_state(boolean state){
    this.state = state;
  }

  public void set_email(String email){
    //TODO: do the email verification here maybe (with this I mean, create a method to do the veryfication)
  }

  public void set_type(String type){
    this.type = type;
  }

  /*=====================user methods=============================*/
  //accesses
  public void set_user(SQLconnect db, String given_username){
    get_user(db, given_username);
  }

  public void register_user(SQLconnect db, Scanner input){
    register(db, input);
  }

  //private stuff
  private void get_user(SQLconnect db, String given_username){
    User gotten_user = db.load_user(given_username);
    
    if(gotten_user != null){
      set_name(gotten_user.get_name());
      set_username(gotten_user.get_username());
      set_password(gotten_user.get_password());
      set_state(gotten_user.get_state());
      set_email(gotten_user.get_email());
      set_type(gotten_user.get_type());
    } else {
      System.out.println("An error has occured: There wasnt found an user");
    }
  }

  private void register(SQLconnect db, Scanner input){
    int choice = 0;
  
    boolean running = true;
    while(running){
      System.out.println("Please insert your user type");
      System.out.println("1- cliente = 2- funcionario");
      choice = input.nextInt();
      input.nextLine();
    
      if(choice == 1){
        set_type("cliente");
        running = false;

      } else if(choice == 2){
        set_type("funcionario");
        running = false;

      } else {
        System.out.println("Your input was invalid, please select one of the options");
      }
    }

    System.out.println("Please insert your name");
    String name = input.nextLine();
    
    set_name(name);
    
    running = true;
    while(running){
      System.out.println("Please insert your username");
      String username = input.nextLine();

      if(!db.check_if_username_unique(username)){
        set_username(username);
        running = false;
      } else {
        System.out.println("The username is already taken, please choose another");
      }
    }
    
    running = true;
    String password = null;
    while(running){
      System.out.println("Please insert your password");
      password = input.nextLine();
      
      if(password != null){
        set_password(password);
        running = false;
      } else {
        System.out.println("The password cant be empty");
      }
    }

    running = true;
    while(running){
      System.out.println("Please insert your email");
        
      //TODO: do email verification 
      String email = input.nextLine();
      running = false; 
    }

    //verify if lenght is == 9 and is unique
    running = true;
    while(running){
      System.out.println("Please insert your NIF");
      int NIF = input.nextInt();
      input.nextLine();
      
      if((NIF > 99999999) && (NIF < 1000000000)){
        //TODO: add NIF
        running = false;
      } else {
        System.out.println("The inserted NIF is invalid");
      }
    }
    
    
    //TODO: verify if lenght is == 9 and starts with 9, 2 or 3
    System.out.println("Please insert your phone number");
    int Phone_number = input.nextInt();
    input.nextLine();
    //end loop
    
    System.out.println("Please insert your address");
    String address = input.nextLine();

    switch(choice){
      case(1): //the sign in is from a client
        System.out.println("");

        break;
      case(2): //the sign in is from a funcionario
        System.out.println("");

        break;
    }

    //TODO: save stuff to ask registration table or whatever I come up with    
  }
} 
