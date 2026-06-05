package model;

import java.util.Scanner;

import model.repair_request;
import database.SQLconnect;

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
  
  public void change_my_info(SQLconnect db, Scanner input){
    change_info(db, input);
  }
  
  public int get_NIF(){
    return NIF;
  }

  public int get_phone_number(){
    return phone_number;
  }

  public String get_address(){
    return address;
  }

  public String get_speciality(){
    return speciality;
  }

  public String get_init_date(){
    return init_date;
  }

  //refeering to repair requests
  private void view_current_requests(){
    ;
  }

  private void accept_request(repair_request repair_to_accept, String Employee_username){
    repair_to_accept.accept_request(Employee_username);
  }

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

          break;
        case(3):

          break;
        case(4):

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
