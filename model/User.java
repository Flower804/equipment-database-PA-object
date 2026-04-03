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
    name = this.name;
    username = this.username;
    password = this.password;
    state = this.state;
    email = this.email;
    type = this.type;
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
    name = this.name;
  }

  public void set_username(String username){
    username = this.username;
  }

  public void set_password(String password){
    password = this.password;
  }

  public void set_state(boolean state){
    state = this.state;
  }

  public void set_email(String email){
    //TODO: do the email verification here maybe (with this I mean, create a method to do the veryfication)
  }

  public void set_type(String type){
    type = this.type;
  }
  
  public void set_user(SQLconnect db, String given_username){
    get_user(db, given_username);
  }

  /*=====================user methods=============================*/

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
} 
