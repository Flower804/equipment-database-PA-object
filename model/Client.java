package model;

public class Client extends User{
  int NIF;
  int phone_number;
  String address;
  //Client unique
  String activity_sector;
  String escalao;

  public Client(String name, String username, String password, boolean state, String email, String type){
    super(name, username, password, state, email, type);

    
  }

    
}
