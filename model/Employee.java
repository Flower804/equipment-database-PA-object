package model;

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

  
}
