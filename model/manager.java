package model;

import database.SQLconnect;

import java.util.Scanner;

public class manager extends User{
  String username = null;

  public manager(String name, String username, String password, boolean state, String email, String type){
    super(name, username, password, state, email, type);

  }

  public void view_register_requests(SQLconnect db, Scanner input){
    db.get_register_requests(); 
  }

}
