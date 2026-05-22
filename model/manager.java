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
        System.out.println("Do you want to accept any Users? 1-yes 2-no");

        int choice = input.nextInt();
        input.nextLine();

        switch(choice){
          case(1):
            accept_user(db, input);

            running = false;
            break;
          case(2):
            running = false;
            break;
          default:
            System.out.println("Sorry that was an invalid choice");
        }
      }
    }
  }

  private void accept_user(SQLconnect db, Scanner input){
    ;
  }
}
