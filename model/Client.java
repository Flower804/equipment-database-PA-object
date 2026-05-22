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

  private void add_equipment(SQLconnect db, Scanner input){
    equipment newEquipment = new equipment("", "", "", -1, 00000000, -1);

    newEquipment.create_equipment(db, input, get_username());
  }

  private void executar_pedido_de_reparacao(){
    ;
  }
}
