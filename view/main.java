package trabalhoPA;

//package imports
import database.SQLconnect;
import model.User;

//libarary imports
import java.util.Scanner;

public class main{
  static private Scanner input = new Scanner(System.in);
  static private SQLconnect db = new SQLconnect();
  
  //static private User 

  public static void main(String[] args) {
    while(true){
      System.out.println("Please choose an option");
      System.out.println("1: register \n2: login");
    
      int choice = input.nextInt();
      input.nextLine();
    
      switch(choice){
        case(1):
          register(input);
          on_exit();
          break;

        case(2):
          login(input);
          on_exit();
          break;
      
        default:
          System.out.println("invalid choice, please choose again");
      }
    }
  }
  
  private static void on_exit(){
    System.out.println("finished");
    System.exit(0);
  }
  
  private static void register(Scanner input){
    System.out.println("");
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
        running = false;
      } else {
        System.out.println("no match found, please try again");
      }
    }
  }
}


