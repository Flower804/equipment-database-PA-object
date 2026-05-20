package model;

import java.util.Date;

import database.SQLconnect; 

public class equipment {
  private String responsible_user;
  private String brand;
  private String model;
  private int SKU_code;
  private Date manifacture_date;
  private int lote;

  //will receave data from client
  //does that make sense? 
  public equipment(String username, String brand, String model, int SKU_code, Date manifacture_date, int lote){
    responsible_user = username;
    this.brand = brand;
    this.model = model;
    this.SKU_code = SKU_code;
    this.manifacture_date = manifacture_date;
    this.lote = lote;
  }

  public void create_equipment(SQLconnect db, Scanner input){
    System.out.println("");
    
    db.insert_equipment(responsible_user, brand, model, SKU_code, manifacture_date, lote);
  }
}
