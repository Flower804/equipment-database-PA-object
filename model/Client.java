package model;

import model.equipment;
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
      System.out.println("Please insert the SKU code of the equipment you want to repair");
    }
  }
}
