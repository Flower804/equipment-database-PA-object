package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import model.equipment;
import database.SQLconnect;

public class repair_request{
  equipment equipment;

  private String responsible_user;
  private int SKU_code;
  private long repair_code;
  private String submission_date;
  private Date repair_date;
  private String assigned_Employee;
  private int state;

  //TODO: think how to apply tempo decorrido here
  private long repair_cost;

  public repair_request(String responsible_user, int SKU_code){
    this.responsible_user = responsible_user;
    this.SKU_code = SKU_code;
  }
  
  public int get_SKU_code(){
    return SKU_code;
  }
  
  public String get_responsible_user(){
    return responsible_user;
  }

  public void create_request(SQLconnect db, Scanner input){
    float repair_code = generate_repair_code(db);

    state = 0;
    if(db.save_request(repair_code, get_SKU_code(), get_responsible_user())){
      System.out.println("Repair request created succesfully");
    }
  }

  public void accept_request(String Employee_username){
    assigned_Employee = Employee_username;
    state = 1;

    
  }

  private float generate_repair_code(SQLconnect db){
    SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddHHmmss");

    int repairs = db.get_number_of_repairs() + 1;
    String data = ft.format(new Date());
    
    String string_repair = String.valueOf(repairs);
    
    String string_repair_code = "" + string_repair + data; 
    
    return Float.parseFloat(string_repair_code);
  }
}
