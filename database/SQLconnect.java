package database;

import java.sql.*;
import java.util.Scanner;
import java.time.LocalTime;
import java.util.Random;

import model.User;
import database.secret;

/**
 *
 * @author Flower/Gabriel Moita
*/ 

//TODO: add documentation

public class SQLconnect {
  Connection conn = get_connection(); 

  //================================accesses==============================
  public static void main(String[] args){
    //TODO; verify if this is really necessary here 
    secret secret = new secret();

    //Connection conn = get_connection();
  }
  
  public boolean check_if_users_exist(){
    return check_users();
  }

  public boolean get_match(String username, String password){
    boolean result = do_match(username, password);
    return result;
  }

  public boolean check_if_user_accepted(String username){
    return check_accepted(username);
  }

  public boolean check_if_user_denied(String username){
    return check_denied(username);
  }
  
  public void create_equipment(String responsible_user, String brand, String model,String manifacture_date){
    insert_equipment(responsible_user, brand, model, manifacture_date);
  }

  public User load_user(String username){
    return get_user(username);
  }
  
  public boolean check_if_username_unique(String username){
    return check_username(username);
  }

  public boolean check_if_NIF_unique(int NIF){
    return check_NIF(NIF);
  }

  public void change_username_connect(String old_username, String new_username, String asker_username){
    change_username(old_username, new_username, asker_username);
  }

  public boolean turn_online(String username){
    return turn_user_online(username);
  }

  public boolean turn_offline(String username){
    return turn_user_offline(username);
  }

  public boolean accept_user(String username, String manager){
    return turn_accepted(username, manager);
  }
  
  public boolean deny_user(String username, String manager){
    return turn_unnacepted(username, manager);
  }

  private String get_current_time(){
    LocalTime currentTime = LocalTime.now();
    String time = ""+currentTime;
    return time;
  }

  public boolean get_register_requests(){
    return get_register();
  }
 //===============================private methods==========================
 
  private static Connection get_connection(){
    /**
     *the get_connection() method serves as a way to 
     *create a connection to the database, in this case db,
      where all the tables and user's data is stored
     */ 
    
    String database_password = secret.get_password();

    try{
      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/db", "root", database_password);
      
      conn.setAutoCommit(false);
      return conn;
    }catch(SQLException e){
      System.out.println("A sql exception has occured: " + e);
    }
    System.out.println("Connection to the database has failed");
    return null;
  }
  
  private boolean check_users(){
    ResultSet rs = null;

    try{
      String query = "Select COUNT(*) as total from users;";
      
      PreparedStatement st = conn.prepareStatement(query);
      rs = st.executeQuery();
      if(rs.next()){
        int count = rs.getInt("total");
        return count > 0;
      } else {
        return false;
      }
      
    } catch(SQLException e){
      e.printStackTrace();
      return false;
    } 
  }

  private boolean do_match(String username, String password){
    ResultSet rs = null;

    try{
      String query = " Select * from users where username = ? and password = ?;";
      
      PreparedStatement st = conn.prepareStatement(query);
      st.setString(1, username);
      st.setString(2, password);
      
      rs = st.executeQuery();

      if(rs.next() == true){
        return true; //it was found one row with this set 
      } else {
        return false;
      }
    }catch(SQLException e){
      System.out.println("A SQLException has occured: " + e);
      return false;
    }
  }

  private boolean check_accepted(String username){
    ResultSet rs = null;

    try{
      String query = "Select * from users where username = ? and accepted = 1;";

      PreparedStatement st = conn.prepareStatement(query);
      st.setString(1, username);

      rs = st.executeQuery();

      if(rs.next() == true){
        return true; //the user was accepted by manager 
      } else {
        return false;
      }
    }catch(SQLException e){
      e.printStackTrace();
      return false;
    }
  }

  private boolean check_denied(String username){
    ResultSet rs = null;

    try{
      String query = "Select * from denied where username = ?;";

      PreparedStatement st = conn.prepareStatement(query);
      st.setString(1, username);

      rs = st.executeQuery();

      if(rs.next() == true){
        String description_query = "Select description from denied where username = ?;";

        PreparedStatement st_desctiption = conn.prepareStatement(description_query);
        st.setString(1, username);

        rs = st.executeQuery();

        return true;
      } else {
        return false;
      }
    }catch(SQLException e){
      e.printStackTrace();
      return false;
    }
  }

  private boolean get_register(){
    ResultSet rs = null;

    try{
      String check_query = "Select COUNT(*) as total from users where accepted = 0";
      
      PreparedStatement st = conn.prepareStatement(check_query);
      rs = st.executeQuery();
      if(rs.next()){
        int count = rs.getInt("total");
        if(count > 0){
          String query = "Select * from users where accepted = 0;";

          st = conn.prepareStatement(query);

          rs = st.executeQuery();
          while(rs.next()){
            System.out.println("----------");
            System.out.println("name: " + rs.getString("name") + ", Username: " + rs.getString("username") + ", Email: " + rs.getString("email") + ", type: " + rs.getString("type"));
            System.out.println("----------");
          }
          
          return true;
        } else {
          System.out.println("there are no register requests at this time");
          
          return false;
        }
      }
    }catch(SQLException e){
      e.printStackTrace();

      return false;
    }
    return false;
  }
  
  //==================================change values==========================================
 
  private boolean turn_accepted(String username, String manager){
    try{
      String query = " update users set accepted = 1 where username = ?";

      PreparedStatement st = conn.prepareStatement(query);
      st.setString(1, username);

      int rs = st.executeUpdate();

      conn.commit();

      String notification = "Insert into notification (type, username, description, is_read) Values (?, ?, ?, ?);";

      String description = "User " + username + " has been accepted by " + manager + " at time: " + get_current_time();

      PreparedStatement st_notif = conn.prepareStatement(notification);
      st_notif.setString(1, "accept state change");
      st_notif.setString(2, username);
      st_notif.setString(3, description);

      conn.commit();
      return true;
    }catch(SQLException e){
      System.out.println("A SQLException has occured: " + e);
      return false;
    }
  }

  private boolean turn_unnacepted(String username, String manager){
    //TODO: fix this
    try{
      String query = " update users set accepted = 0 where username = ?";

      PreparedStatement st = conn.prepareStatement(query);
      st.setString(1, username);

      int rs = st.executeUpdate();

      conn.commit();

      String notification = "Insert into notification (type, username, description, is_read) Values (?, ?, ?, ?);";

      String description = "User " + username + " has been denied access by " + manager + " at time: " + get_current_time();

      PreparedStatement st_notif = conn.prepareStatement(notification);
      st_notif.setString(1, "accept state change");
      st_notif.setString(2, username);
      st_notif.setString(3, description);

      conn.commit();
      return true;
    }catch(SQLException e){
      System.out.println("A SQLException has occured: " + e);
      return false;
    }
  }

  private boolean turn_user_online(String username){
    try{
      String query = " update users set state = 1 where username = ?;";
      
      PreparedStatement st = conn.prepareStatement(query);
      st.setString(1, username);

      int rs = st.executeUpdate();
      
      conn.commit();

      String notification = "Insert into notification (type, username, description, is_read) Values (?, ?, ?, 1);";

      String description = "User " + username + " has logged on at time " + get_current_time();

      PreparedStatement st_notif = conn.prepareStatement(notification);
      st_notif.setString(1, "log on");
      st_notif.setString(2, username);
      st_notif.setString(3, description);

      rs = st_notif.executeUpdate();

      conn.commit();

      return true;
    }catch(SQLException e){
      System.out.println("A SQLException has occured: " + e);
      return false;
    }
  }

  private boolean turn_user_offline(String username){
    try{
      String query = " update users set state = 0 where username = ?;";
      
      PreparedStatement st = conn.prepareStatement(query);
      st.setString(1, username);

      int rs = st.executeUpdate();
      
      conn.commit();

      String notification = "insert into notification (type, username, description, is_read) Values (?, ?, ?, 1); ";
      
      String description = "User " + username + " has logged off at time: " + get_current_time(); 

      PreparedStatement st_notif = conn.prepareStatement(notification);
      st_notif.setString(1, "log off");
      st_notif.setString(2, username);
      st_notif.setString(3, description);
      
      rs = st_notif.executeUpdate();

      conn.commit();

      return true;
    }catch(SQLException e){
      System.out.println("A SQLException has occured: " + e);
      return false;
    }
  }

  private void change_username(String old_username, String new_username, String changer_username){
    try{
      String query = "Update users set username = ? where username = ?;";

      PreparedStatement st = conn.prepareStatement(query);
      st.setString(1, new_username);
      st.setString(2, old_username);

      int rs = st.executeUpdate();

      conn.commit();

      String notification = "insert into notification (type, username, description, is_read) Values (?, ?, ?, 1);";

      String description = "User " + new_username + " changed the username of " + old_username + " to " + new_username + " at time: " + get_current_time();

      PreparedStatement st_notif = conn.prepareStatement(notification);
      st_notif.setString(1, "change name");
      st_notif.setString(2, changer_username);
      st_notif.setString(3, description);

      rs = st_notif.executeUpdate();

      conn.commit();
    }catch(SQLException e){
      e.printStackTrace();
    }
  }
  
  //=============================GETTERS====================================
  private User get_user(String username){
    ResultSet rs = null;

    User result = null;
    try{

      String query = " Select * from users where username = ?;";
      
      PreparedStatement st = conn.prepareStatement(query);
      st.setString(1, username);

      rs = st.executeQuery();

      if(rs.next()){
        String SQL_name = rs.getString("name");
        String SQL_username = rs.getString("username");
        String SQL_password = rs.getString("password");
        boolean SQL_state = rs.getBoolean("state");
        String SQL_email = rs.getString("email");
        String SQL_type = rs.getString("type");
      
        //TOD: view if returning a User object isent just easier
        //result = SQL_name + ";" + SQL_username + ";" + SQL_password + ";" + SQL_state + ";" + SQL_email + ";" + SQL_type;
        User curr_user = new User(SQL_name, SQL_username, SQL_password, SQL_state, SQL_email, SQL_type);
        result = curr_user;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }
  
  private boolean get_amount_SKU(int SKU_to_compare){
    ResultSet rs = null;

    try{
      String query = "Select * from Equipment where SKU_code = ?";

      PreparedStatement st = conn.prepareStatement(query);
      st.setInt(1, SKU_to_compare);

      rs = st.executeQuery();

      if(rs.next() == true){
        return true;
      } else {
        return false;
      }
    } catch(SQLException e){
      System.out.println("Sorry a SQLException has occured");
      return false;
    }
  }

  //========================methods========================================
  public void user_register(int user_type, String name, String username, String password, String email, int NIF, int Phone_number, String address, String activity_sector_, int grade, String speciality){
    Insert_user_register(user_type, name, username, password, email, NIF, Phone_number, address, activity_sector_, grade, speciality);
  }
  
  public void request_register(String username){
    User_reg_request(username);
  }
  //========================checkers=======================================
  private boolean check_username(String username){
    ResultSet rs = null;

    try{
      String query = "Select * from users where username = ?;";

      PreparedStatement st = conn.prepareStatement(query);
      st.setString(1, username);

      rs = st.executeQuery();

      if(rs.next() == true){
        return true; //the username already exists
      } else {
        return false; //the username doesnt exist
      }
    } catch(SQLException e){
      e.printStackTrace();
      return true;
    }
  }

  private boolean check_NIF(int NIF){
    ResultSet rs = null;
    
    //ehhhhhhhhh I feel like this is bad code need to take a look at this later
    //maybe use the table names as prepared statement and do this on a loop instead of straight up writen up
    try{
      //check in clients table 
      String query = "Select * from clientes where NIF = ?;";

      PreparedStatement st = conn.prepareStatement(query);
      st.setInt(1, NIF);

      rs = st.executeQuery();
      
      if(rs.next() == true){
        return true; //the NIF was found in clients table
      }
      //check in funcionarios table
      String Second_query = "Select * from funcionarios where NIF = ?;";

      st = conn.prepareStatement(Second_query);
      st.setInt(1, NIF);

      rs = st.executeQuery();

      if(rs.next() == true){
        return true; //the NIF was found in funcionarios table 
      } else {
        return false; //the NIF is unique
      }
    } catch(SQLException e){
      e.printStackTrace();
      return true; //better to stop this than to just return this as accepted
    }
  }
  
  /**
   *creates a unique SKU code (a code bethween 100000 and 1) 
   *
   * @return an SKU code 
   */

  private int create_SKU(){
    int SKU = -1;
    
    Random r = new Random();

    do{
      SKU = r.nextInt(100000 - 1); //generate a random number bethween(max - min)
    }while(get_amount_SKU(SKU));

    return SKU;
  }

  private Date string_into_Date(String date){
    String day = date.substring(0, 2);
    String month = date.substring(2, 4);
    String year = date.substring(4, 8);

    String date_format = year + "-" + month + "-" + day;

    Date manifacture_date = Date.valueOf(date_format);
    return manifacture_date;
  }

  //-----------------------INSERTER----------------
  
  /**
   * Saves in the database a equipment created by an user
   *
   * @param responsible_user the user that owns the equipment
   * @param brand the brand of the equipment being saved
   * @param model the model of the equipment being saved
   * @param manifacture_date the date of manifacture of the equipment being saved
   */ 
  private void insert_equipment(String responsible_user, String brand, String model, String string_manifacture_date){
    int SKU_code = create_SKU();
    
    Date manifacture_date = string_into_Date(string_manifacture_date);

    try{
      String query = "insert into Equipment (responsible_user, brand, model, SKU_code, manifacture_date) Values (?, ?, ?, ?, ?)";
    
      PreparedStatement st = conn.prepareStatement(query);
      st.setString(1, responsible_user);
      st.setString(2, brand);
      st.setString(3, model);
      st.setInt(4, SKU_code);
      st.setDate(5, manifacture_date);
    
      st.executeUpdate();
      
      conn.commit();
    } catch(SQLException e){
      e.printStackTrace();
      //conn.rollback();
    }
  }

  //insert in tables methods
  //pass 
  //1 -> client 
  //2 -> employee
  //3 -> admin
  private void Insert_user_register(int user_type, String name, String username, String password, String email, int NIF, int Phone_number, String address, String activity_sector_, int grade, String speciality){
    String type = null;
    switch(user_type){
      case(1):
        type = "client";
        break;
      case(2):
        type = "funcionario";  //TODO: fix this to english so there is language consistenci on the code remember professor Gil Vicente
        break;
      case(3):
        type = "manager";
        break;
    }

    try{
      int accepted = 0;
      if(user_type == 3){
        accepted = 1;
      } else {
        accepted = 0;
      }
      
      String query = "insert into users (name, username, password, state, email, type, accepted) Values (?, ?, ?, 0, ?, ?, ?)";
      
      PreparedStatement st = conn.prepareStatement(query);
      st.setString(1, name);
      st.setString(2, username);
      st.setString(3, password);
      st.setString(4, email);
      st.setString(5, type);
      st.setInt(6, accepted);

      int rs = st.executeUpdate();

      if(user_type == 1){
        String query_client = "insert into clientes (username, NIF, phone_number, address, activity_sector, escalao) Values (?, ?, ?, ?, ?, ?);";
        
        PreparedStatement st_1 = conn.prepareStatement(query_client);
        st_1.setString(1, username);
        st_1.setInt(2, NIF);
        st_1.setInt(3, Phone_number);
        st_1.setString(4, address);
        st_1.setString(5, activity_sector_);
        st_1.setInt(6, grade);

        rs = st_1.executeUpdate();

        conn.commit();
      }else if(user_type == 2){
        String query_funcionarios = "insert into funcionarios (username, NIF, phone_number, address, speciality, init_date) Values (?, ?, ?, ?, ?, curdate());";       

        PreparedStatement st_2 = conn.prepareStatement(query_funcionarios);
        st_2.setString(1, username);
        st_2.setInt(2, NIF);
        st_2.setInt(3, Phone_number);
        st_2.setString(4, address);
        st_2.setString(5, speciality);

        rs = st_2.executeUpdate();
        
        conn.commit();
      }else if(user_type == 3){
        String query_manager = "insert into managers (username) Values (?);";

        PreparedStatement st_3 = conn.prepareStatement(query_manager);
        st_3.setString(1, username);
      
        rs = st_3.executeUpdate();

        conn.commit();
      }

      conn.commit();
    }catch(SQLException e){
      e.printStackTrace();
    }
  }

  private void User_reg_request(String username){
    try{
      String query = "Insert into user_reg_request (username) Values (?)";

      PreparedStatement st = conn.prepareStatement(query);
      st.setString(1, username);
      
      int rs = st.executeUpdate();

      conn.commit();

      String notification = "insert into notification (type, username, description, is_read) Values (?, ?, ?, 1);";

      String description = "User " + username + " has requested to register at: " + get_current_time();

      PreparedStatement st_notif = conn.prepareStatement(notification);
      st_notif.setString(1, "registration reqiest");
      st_notif.setString(2, username);
      st_notif.setString(3, description);

      rs = st_notif.executeUpdate();

      conn.commit();
    }catch(SQLException e){
      e.printStackTrace();
    }
  }
} 
