package model;

import model.repair_request;

public class Employee extends User{
  int NIF;
  int phone_number;
  String address;
  //Employee unique
  String speciality;
  String init_date;

  public Employee(String name, String username, String password, boolean state, String email, String type){
    super(name, username, password, state, email, type);

  }
  
  //refeering to repair requests
  private void view_current_requests(){

  }

  private void accept_request(repair_request repair_to_accept, String Employee_username){
    repair_to_accept.accept_request(Employee_username);
  }
}
