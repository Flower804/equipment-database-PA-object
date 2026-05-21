package model;

import java.util.Date;
import java.util.Scanner;

import database.SQLconnect; 

public class equipment {
  private String responsible_user;
  private String brand;
  private String model;
  private int SKU_code;
  private int manifacture_date;
  private int lote;

  //will receave data from client
  //does that make sense? 
  public equipment(String username, String brand, String model, int SKU_code, int manifacture_date, int lote){
    responsible_user = username;
    this.brand = brand;
    this.model = model;
    this.SKU_code = SKU_code;
    this.manifacture_date = manifacture_date;
    this.lote = lote;
  }

  public void create_equipment(SQLconnect db, Scanner input, String user_username){
    System.out.println("What is the equipments brand");
    String equi_brand = input.nextLine();

    System.out.println("What is the equipments model");
    String equi_model = input.nextLine();

    //TODO: HOW THE HELL DO I GET THE USER TO INPUT A DATE TFFFFFFF
    int int_manifacture_date = 0;

    System.out.println("Insert the lote where the equipment can be found");
    int equi_lote = input.nextInt();
    input.nextLine();

    db.create_equipment(responsible_user, brand, model, int_manifacture_date);
  }
}
