package model;

import database.SQLconnect;

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
    int choice;
 
    //TODO: protect this - the user may input other numbers than 1 and 2
    System.out.println("Please insert your user type");
    System.out.println("1- cliente = 2- funcionario");
    choice = input.nextInt();
    input.nextLine();
    
    if(choice == 1){
      user.set_type("cliente");

    } else if(choice == 2){
      user.set_type("funcionario");

    } else {
      //TODO: make the loop continue here
    }
    
    System.out.println("Please insert your name");
    String name = user.nextLine();
    
    user.set_name(name);
    
    //TODO: do another loop here to verify if username is unique, basically do a database search
    System.out.println("Please insert your username");
    String username = input.nextLine();

    //end loop here

    System.out.println("Please insert your password");
    String password = input.nextLine();

    user.set_password(password);
    
    //TODO: create a loop here to verify if email is valid
    System.out.println("Please insert your email");
    String email = input.nextLine();
    
    //TODO: verify if lenght is == 9 and is unique
    System.out.println("Please insert your NIF");
    int NIF = input.nextInt();
    input.nextLine();
    //end loop
    
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
