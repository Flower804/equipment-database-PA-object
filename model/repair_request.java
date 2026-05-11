package model;

import java.util.Date;

import model.equipment;

public class repair_request{
  equipment equipment;

  private String responsible_user;
  private int SKU_code;
  private int repair_code;
  private Date submission_date;
  private Date repair_date;
  private String assigned_Employee;
  private boolean accepted;

  public repair_request(String username, equipment equipment_to_request){
    responsible_user = username;
    accepted = false;
    
    equipment = equipment_to_request;
  }

  public void accept_request(String Employee_username){
    assigned_Employee = Employee_username;
    accepted = true;
  }
}
