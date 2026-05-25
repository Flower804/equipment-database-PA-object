package model;

import model.equipment;
import model.repair_request;
import database.SQLconnect;

import java.util.Scanner;

public class Client extends User{
  private int NIF;
  private int phone_number;
  private String address;
  //Client unique
  private String activity_sector;
  private String escalao;

  public Client(String name, String username, String password, boolean state, String email, String type){
    super(name, username, password, state, email, type);

    
  }
  
  //accesserrsssssss

  public void add_new_equipment(SQLconnect db, Scanner input){
    add_equipment(db, input); 
  }
  
  public void repair_request(SQLconnect db, Scanner input){
    execute_repair_request(db, input);
  }

  private void add_equipment(SQLconnect db, Scanner input){
    equipment newEquipment = new equipment("", "", "", -1, 00000000, -1);

    newEquipment.create_equipment(db, input, get_username());
  }

  private void execute_repair_request(SQLconnect db, Scanner input){
    if(db.check_existing_equipments(get_username())){
      boolean running = true;
        
      while(running){
        System.out.println("Please insert the SKU code of the equipment you want to repair, or insert 9 to exit");
        int SKU_code_intended = input.nextInt();
        input.nextLine();

        if(SKU_code_intended == 9){
          running = false;
        }else if(db.check_SKU_code(SKU_code_intended)){
          running = false;
          
          repair_request request = new repair_request(get_username(), SKU_code_intended);
          request.create_request(db, input);
        }
      }
    }
  }
}
