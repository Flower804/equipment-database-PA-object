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

/**
 *A class that contains all the listings possible to be done by the users to search inside the database
 *@author Gabriel Moita
 */ 

public class listing{
  
  /**
   *Calls the function responsible for listing all the users on the table "users"
   *@param db the SQLconnect object thats being used to do the comunication bethween the database and the app
   *@param input the Scanner object being used for inputs from the user
   *@param type the type of the user that called the method 
   */ 
  public void list_users(SQLconnect db, Scanner input, String type){
    list_users_classic(db, input, type);
  }
  
  /**Creates a filter to filter trough usernames
   *
   * @return the filter form of the leters that the user is searching for
   */
  private String create_filter(Scanner input){
    String filter;
    
    System.out.println("Please write what you want in the filter");
    filter = input.nextLine();

    String final_filter = "%" + filter + "%";
    return final_filter;
  }
  
  /**The method accesable by managers that does the connection to the database to get a list of all the users existent, with possibilty of applied filter
   *
   *@param db the SQLconnect object thats being used to do the comunication bethween the database and the app
   *@param input the Scanner object being used for inputs from the user
   *@param type the type of the user that called the method 
   */
  private void list_users_classic(SQLconnect db, Scanner input, String type){
    if(type.equals("manager")){
      boolean running = true;
      int choice;

      String filter = "";
      int offset = 0;

      while(running){
        ArrayList<User> users_by_name = db.list_users_by_name(filter, offset);

        System.out.println("1- next page, 2- filter, 3- exit");
        choice = input.nextInt();
        input.nextLine();

        switch(choice){
          case(1):
            offset = offset + 10;

            break;
          case(2):
            filter = create_filter(input);

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
  
  /**
   *
   *
   *
   */ 
  private void list_users_by_class(SQLconnect db, Scanner input, String type){
    if(type.equals("manager")){
      boolean running = true;

      while(running){
        int choice;

        System.out.println("Please choose which by which listing you want to do?\n1-name \n2-username \n3-type");
        choice = input.nextInt();
        input.nextLine();

        switch(choice){
          case(1)://search by name
            search_man_by_name(db, input);

            running = false;
            break;
          case(2)://search by username
            search_man_by_username(db, input);

            running = false;
            break;
          case(3)://search by type
            search_man_by_type(db, input);

            running = false;
            break;
          default:
            System.out.println("Sorry that choice was invalid");
            break;
        }
      }
    }
  }

  /**
   *
   *
   *
   */ 
  private void search_man_by_name(SQLconnect db, Scanner input){
    boolean running = true;
    int choice;

    String filter = "";
    int offset = 0;

    while(running){
      ArrayList<User> users_by_name = db.list_users_by_choice(1, filter, offset);

      System.out.println("1- next page, 2- filter, 3- exit");
      choice = input.nextInt();
      input.nextLine();

      switch(choice){
        case(1):
          offset = offset + 10;

          break;
        case(2):
          filter = create_filter(input);

          break;
        case(3):
          running = false;

          break;
        default:
          System.out.println("sorry that an invalid choice");
          break;
      }
    }
  }

  /**
   *
   *
   *
   *
   */ 
  private void search_man_by_username(SQLconnect db, Scanner input){
    boolean running = true;
    int choice;

    String filter = "";
    int offset = 0;

    while(running){
      ArrayList<User> users_by_name = db.list_users_by_choice(2, filter, offset);

      System.out.println("1- next page, 2- filter, 3- exit");
      choice = input.nextInt();
      input.nextLine();

      switch(choice){
        case(1):
          offset = offset + 10;

          break;
        case(2):
          filter = create_filter(input);

          break;
        case(3):
          running = false;

          break;
        default:
          System.out.println("sorry that an invalid choice");
          break;
      }
    }
  }
  
  /**
   *
   *
   *
   *
   */ 
  private void search_man_by_type(SQLconnect db, Scanner input){
    boolean running = true;
    int choice;

    String filter = "";
    int offset = 0;

    while(running){
      ArrayList<User> users_by_name = db.list_users_by_choice(3, filter, offset);

      System.out.println("1- next page, 2- filter, 3- exit");
      choice = input.nextInt();
      input.nextLine();

      switch(choice){
        case(1):
          offset = offset + 10;

          break;
        case(2):
          filter = create_filter(input);

          break;
        case(3):
          running = false;

          break;
        default:
          System.out.println("sorry that an invalid choice");
          break;
      }
    }
  }
}
