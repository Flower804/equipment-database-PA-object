package model;

public class repair_request{
  private String responsible_user;
  private boolean accepted;
  private String assigned_Employee;

  public repair_request(String username){
    responsible_user = username;
    accepted = 0;

  }
  
  public void accept_request(String Employee_username){
    assigned_Employee = Employee_username;
    accepted = 1;

  }
}
