package model;

import database.SQLconnect;

import java.util.Scanner;

public class manager extends User{
  String username = null;

  public manager(String name, String username, String password, boolean state, String email, String type){
    super(name, username, password, state, email, type);

  }

  public void view_register_requests(SQLconnect db, Scanner input){
    if(db.get_register_requests()){
      boolean running = true;

      while(running){
        System.out.println("Do you want to accept/deny any Users? 1-accept 2-deny 3-no");

        int choice = input.nextInt();
        input.nextLine();

        switch(choice){
          case(1):
            accept_user(db, input);

            break;
          case(2):
            //other htingy

            break;
          case(3):
            running = false;

            break;
          default:
            System.out.println("Sorry that was an invalid choice");
        }
      }
    }
  }

  private void accept_user(SQLconnect db, Scanner input){
    db.get_register_requests();
    System.out.println("Please write the Username of the User you want to accept: ");
    
    String user_to_accept = input.nextLine();
    
    if(db.accept_user(user_to_accept, get_username())){
      System.out.println("User accepted succesfully");
    } else {
      System.out.println("I'm sorry the user wasnt found");
    }
  }

  private void denny_user(SQLconnect db, Scanner input){
    db.get_register_requests();
    System.out.println("Please write the Username of the User you want to denny: ");

    String user_to_denny = input.nextLine();

    if(db.deny_user(username, get_username())){
      System.out.println("User denied succesfully");
    } else {
      System.out.println("I'm sorry the user wasnt found");
    }
  }
}
