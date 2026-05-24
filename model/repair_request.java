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
  private int repair_code;
  private String submission_date;
  private Date repair_date;
  private String assigned_Employee;
  private boolean accepted;

  //TODO: think how to apply tempo decorrido here
  private long repair_cost;

  public repair_request(String responsible_user, int SKU_code){
    this.responsible_user = responsible_user;
    this.SKU_code = SKU_code;
  }

  public void create_request(SQLconnect db, Scanner input){
    SimpleDateFormat ft = new SimpleDateFormat("dd-mm-yyyy");
    submission_date = ft.format(new Date());

     

    accepted = false;
  }

  public void accept_request(String Employee_username){
    assigned_Employee = Employee_username;
    accepted = true;

    
  }

  private int generate_repair_code(SQLconnect db){
    //Uma reparação é caracterizada por um número de reparação que é composto por um número sequencial
    //(a cada pedido o número incrementa), seguido da data no formato AAAAMMDDHHMMSS.
    //Por exemplo, se já ocorreram 95 pedidos até ao momento, e às 15h33m10s do dia 19 de Fevereiro de 2026
    //surge um novo pedido, o mesmo terá o número 9620260219153310.
    
    int repairs;

  }
}
