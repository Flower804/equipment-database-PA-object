package model;

import database.SQLconnect;
import model.Client; //cliente class
import model.Employee; //funcionarios class

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

  public boolean set_email(String email){
    if(email_is_Valid(email)){
      this.email = email;
      return true;
    } else {
      return false;
    }
  }

  public void set_type(String type){
    this.type = type;
  }

  /*=====================user methods=============================*/
  //taken from https://www.geeksforgeeks.org/java/check-email-address-valid-not-java/
  private static boolean email_is_Valid(String email){
    Pattern email_pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    
    Matcher m = email_pattern.matcher(email);

    if((email != null) && (m.matches())){
      return true;
    } else {
      return false;
    }
  }
  
  private static boolean phone_number_is_Valid(int phone_number){
    String string_phone_number = Integer.toString(phone_number);

    Pattern phone_pattern_1 = Pattern.compile("9\\d\\d\\d\\d\\d\\d\\d\\d");
    Pattern phone_pattern_2 = Pattern.compile("2\\d\\d\\d\\d\\d\\d\\d\\d");
    Pattern phone_pattern_3 = Pattern.compile("3\\d\\d\\d\\d\\d\\d\\d\\d");
    
    //maybe put this in a consecutive chercker instead of doing everything at the same time
    Matcher pattern_1 = phone_pattern_1.matcher(string_phone_number);
    Matcher pattern_2 = phone_pattern_2.matcher(string_phone_number);
    Matcher pattern_3 = phone_pattern_3.matcher(string_phone_number);

    if(pattern_1.matches() || pattern_2.matches() || pattern_3.matches()){
      return true;
    } else {
      return false;
    }
  }

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

    String name_to_register = null;
    String username_to_register = null;
    String password_to_register = null;
    String email_to_register = null;
    int NIF_to_register = -1;
    int phone_number_to_register = -1;
    String user_type_to_register = null;
    String address_to_register = null;
  
    boolean running = true;
    while(running){
      System.out.println("Please insert your user type");
      System.out.println("1- cliente = 2- funcionario");
      choice = input.nextInt();
      input.nextLine();
    
      if(choice == 1){
        user_type_to_register = "cliente";
        running = false;

      } else if(choice == 2){
        user_type_to_register = "funcionario";
        running = false;

      } else {
        System.out.println("Your input was invalid, please select one of the options");
      }
    }

    System.out.println("Please insert your name");
    String name = input.nextLine();
    
    name_to_register = name;
    
    running = true;
    while(running){
      System.out.println("Please insert your username");
      String username = input.nextLine();

      if(!db.check_if_username_unique(username)){
        username_to_register = username;
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
        password_to_register = password;
        running = false;
      } else {
        System.out.println("The password cant be empty");
      }
    }

    running = true;
    while(running){
      System.out.println("Please insert your email");
         
      String email = input.nextLine();
      if(email_is_Valid(email)){
        email_to_register = email;
        running = false;
      } else {
        System.out.println("the email that you've isent isnt valid");
      }
    }

    //verify if lenght is == 9 and is unique
    running = true;
    int NIF = -1;
    while(running){
      System.out.println("Please insert your NIF");
      NIF = input.nextInt();
      input.nextLine();
      
      if((NIF > 99999999) && (NIF < 1000000000)){
        NIF_to_register = NIF;
        running = false;
      } else {
        System.out.println("The inserted NIF is invalid");
      }
    }
    
    running = true;
    while(running){
      System.out.println("Please insert your phone number");
      int Phone_number = input.nextInt();
      input.nextLine();

      if(phone_number_is_Valid(Phone_number)){
        phone_number_to_register = Phone_number;
        running = false;
      } else {
        System.out.println("the phone number you have inserted is invalid, please write a valid one");
      
      }
    }
    
    System.out.println("Please insert your address");
    String address = input.nextLine();

    address_to_register = address;
    
    //TODO: this whole last section feels rushed, need to rework it if I have time
    String activity_sector_to_register = null;
    int grade_to_register = 0;
    String speciality_to_register = null;
    switch(choice){
      case(1): //the sign in is from a client
        running = true;

        while(running){
          System.out.println("Por favor insira o seu setor de atividade");
          activity_sector_to_register = input.nextLine();

          if(activity_sector_to_register != null){
            running = false;
          }
        }
        
        //TODO: save to bd 
        break;
      case(2): //the sign in is from a funcionario
        running = true;

        while(running){
          System.out.println("Por favor insira a sua especialidade (1-5)");
          speciality_to_register = input.nextLine();

          if(speciality_to_register != null){
            
            running = false;
          }
        }
        break;
    }
    //TODO: maybe change this method to 2 or 3 methods so I can save for specific tables 
    db.user_register(choice, name_to_register, username_to_register, password_to_register, email_to_register, NIF_to_register, phone_number_to_register, address_to_register, activity_sector_to_register, grade_to_register, speciality_to_register);
    db.request_register(username_to_register);
    //TODO: add notification here
    //and maybe pass the username
  }
} 
