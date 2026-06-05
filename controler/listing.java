package controler;

import java.util.ArrayList;
import java.util.Scanner;

import model.User;
import model.Client;
import model.manager;
import model.Employee;
import model.equipment;
import model.repair_request;

import database.SQLconnect;

public class listing{
  public void list_users(SQLconnect db, Scanner input, String type){
    list_users_classic(db, input, type);
  }

  private String create_filter(){
    String filter;
    
    System.out.println("Please write what you want in the filter");
    filter = input.nextLint();

    String final_filter = "%" + filter + "%";
    return final_filter;
  }

  private void list_users_classic(SQLconnect db, Scanner input, String type){
    if(type.equals("manager")){
      boolean running = true;
      int choice;

      String filter = "";
      int offset = 0;

      while(running){
        ArrayList<User> = db.list_users_by_name(filter, offset);

        System.out.println("1- next page, 2- filter, 3- exit");
        choice = input.nextInt();
        input.nextLine();

        switch(choice){
          case(1):
            offset = offset + 10;

            break;
          case(2):
            filter = createfilter();

            break;
          case(3):
            running = false;

            break;
          default:
            System.out.println("sorry that an invalid choice");
            break;
        }
      }
    }else{
      System.out.println("Sorry you arent the right user type for this");
    }
  } 
}
