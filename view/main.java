package trabalhoPA;

//package imports
import database.SQLconnect;
import model.User;

//libarary imports
import java.util.Scanner;

public class main{
  static private Scanner input = new Scanner(System.in);
  static private SQLconnect db = new SQLconnect();
  
  static private User user = new User("no name", "no username", "no password", false, "no email", "no type"); 

  public static void main(String[] args) {
    while(true){
      if(!db.check_if_users_exist()){
        System.out.println("No users found, please create a manager");
        register_manager(input);
        break;
      } else {
        System.out.println("Please choose an option");
        System.out.println("1: register \n2: login");
    
        int choice = input.nextInt();
        input.nextLine();
    
        switch(choice){
          case(1):
            register(input);
            on_exit(user.get_username());
            break;

          case(2):
            login(input);
            on_exit(user.get_username());
            break;
      
          default:
            System.out.println("invalid choice, please choose again");
        }
      }
    }
  }
  
  private static void on_exit(String username){
    db.turn_user_offline(username);

    System.out.println("Adeus " + user.get_username());
    System.exit(0);
  }
  
  private static void register(Scanner input){
    user.register_user(db, input, false);
  }

  private static void register_manager(Scanner input){
    user.register_user(db, input, true);
  }

  private static void login(Scanner input){
    String username = null;
    String password = null;
  
    boolean running = true;
    boolean match = false;

    while(running){
      System.out.println("Please insert your username");
      username = input.nextLine();
    
      System.out.println("Please insert your password");
      password = input.nextLine();

      match = db.get_match(username, password);
      if(match){
        System.out.println("user found");
        if(db.check_if_user_accepted(username)){
          user.set_user(db ,username);
          db.turn_user_online(username);
          running = false;
          System.out.println("bem-vindo " + user.get_username());
          
          String user_type = user.get_type();
          switch(user_type){
            case("client"):
              //TODO: load client info 
            
              break;
            case("funcionario"):
              //TODO: load funcionario info
              
              break;
            case("manager"):
              Manager_loop(db, input);
              break;
          }
        } else {
          System.out.println("Sorry, you still havent been accepted by an user");
        }
      } else {
        System.out.println("no match found, please try again");
      }
    }
  }

  private static void Client_loop(SQLconnect db, Scanner input){
    boolean running = true;
    int choice;

    while(running){
      //TODO: change this two System outs to only one, and do the same on the other loops
      System.out.println("Please select what do you want to do");
      System.out.println("1-change my info \n 9-exit");
      choice = input.nextInt();
      input.nextLine();

      switch(choice){
        case(1):
          break;
        case(9):
          running = false;
          break;
      }
    }
  }

  private static void Employee_loop(SQLconnect db, Scanner input){
   boolean running = true;
    int choice;

    while(running){
      //TODO: change this two System outs to only one, and do the same on the other loops
      System.out.println("Please select what do you want to do");
      System.out.println("1-change my info \n 9-exit");
      choice = input.nextInt();
      input.nextLine();

      switch(choice){
        case(1):
          break;
        case(9):
          running = false;
          break;
      }
    } 
  }
  
  private static void Manager_loop(SQLconnect db, Scanner input){
    boolean running = true;
    int choice;
    
    //=======================debbugging==================
    System.out.println("On manager loop");
    //===================================================

    while(running){
      //TODO: change this two System outs to only one, and do the same on the other loops
      System.out.println("Please select what do you want to do");
      System.out.println("1-change my info \n 9-exit");
      choice = input.nextInt();
      input.nextLine();

      switch(choice){
        case(1):
          break;
        case(9):
          running = false;
          break;
      }
    }  
  }

}
