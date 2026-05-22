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

    System.out.println("Insert the lote where the equipment can be found");
    int equi_lote = input.nextInt();
    input.nextLine();
    
    System.out.println("Insert the date of manifacture of the equipment in the format of ddmmyyyy");
    boolean running = true;
    String string_manifacture_date = "";

    do{
        string_manifacture_date = input.nextLine();
    }while(!check_string_date(string_manifacture_date));

    db.create_equipment(responsible_user, brand, model, string_manifacture_date);
  }

  private boolean check_string_date(String manifacture_date){
    //I know this is stupid as hell but.... bear with me
    if(manifacture_date.length() == 8){
      return true;
    } else {
      System.out.println("Please insert a valid date in the format of ddmmyyyy");
      return false;
    }
  }
}
