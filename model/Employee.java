package model;

import java.util.Scanner;

import model.repair_request;
import database.SQLconnect;

/**
 *The class that contains the methods for the "Employee" inheritance
 *
 *@author Gabriel Moita
 */
public class Employee extends User{
  int NIF;
  int phone_number;
  String address;
  //Employee unique
  String speciality;
  String init_date;

  public Employee(String name, String username, String password, boolean state, String email, String type){
    super(name, username, password, state, email, type);

  }
  
  /**calls the method for the user Employee to change their own info
   * 
   * @param db the SQLconnect object thats being used to do the communication bethween the database and the app
   * @param input the Scanner object being used for inputs from the user
   */ 
  public void change_my_info(SQLconnect db, Scanner input){
    change_info(db, input);
  }
  
  /**returns the current Employee's NIF number
   *
   *@return the Employee's NIF number 
   */ 
  public int get_NIF(){
    return NIF;
  }

  /**A method that gets the Employee's phone number
   *
   *@return the Employee's phone number
   */ 
  public int get_phone_number(){
    return phone_number;
  }
  
  /**A method that gets the Employee's address
   *
   *@return the Employee's address
   */ 
  public String get_address(){
    return address;
  }
  
  /**A method that gets the Employee's speciality
   *
   *@return the Employee's speciality
   */ 
  public String get_speciality(){
    return speciality;
  }

  /**A method that gets the date when the Employee started working
   *
   *@return the Employee's start date
   */ 
  public String get_init_date(){
    return init_date;
  }

  //refeering to repair requests
  private void view_current_requests(){
    ;
  }
  
  /**Accepts the request given to the Employee by the Manager
   *
   *@param repair_request the resquest made by the Client to be assigned to the Employee by the Manager
   *@param Employee_username the username of the Employee to be assigned to the repair_request
   */ 
  private void accept_request(repair_request repair_to_accept, String Employee_username){
    repair_to_accept.accept_request(Employee_username);
  }
  
  /**Changes the info of the current Employee
   *
   *@param db the SQLconnect object thats being used to do the comunication bethween the database and the app
   *@param input the Scanner object being used for inputs from the user
   */
  private void change_info(SQLconnect db, Scanner input){
    boolean running = true;
    int choice;

    while(running){
      System.out.println("This is you current info: \n1-name: " + get_name() + "\n2-username: " + get_username() + "\n3-password: " + get_password() + "\n4-email: " + get_email() + "\n5-NIF: " + String.valueOf(get_NIF()) + "\n6-phone number: " + String.valueOf(get_phone_number()) + "\n7-address: " + get_address() + "\n8-speciality: " + get_speciality());
      System.out.println("What would you like to change? 9-exit");
      choice = input.nextInt();
      input.nextLine();

      switch(choice){
        case(1):
          System.out.println("what would you like to change you name into? ");
          String new_name = input.nextLine();

          db.change_my_info("users", "name", new_name, get_username());
          break;
        case(2):
          //uhmmmm should we really do this?
          //1- ask for username
          //2- do verification to confirm not repeated
          //3- change

          break;
        case(3):
          System.out.println("What is the new password? ");
          String new_password = input.nextLine();

          db.change_my_info("users", "password", new_password, get_username());

          break;
        case(4):
          System.out.println("Please ");

          break;
        case(5):

          break;
        case(6):

          break;
        case(7):

          break;
        case(8):

          break;
        case(9):
        default:
          System.out.println("I'm sorry but that input is invalid");

          break;
      }
    }
  }
}
