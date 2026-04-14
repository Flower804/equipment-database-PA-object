package database;

import java.sql.*;
import java.util.Scanner;
import java.time.LocalTime;

import model.User;
import database.secret;

/**
 *
 * @author Flower/Gabriel Moita
*/ 

//TODO: add documentation

public class SQLconnect {
  

  //================================accesses==============================
  public static void main(String[] args){
    //TODO; verify if this is really necessary here 
    secret secret = new secret();

    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;

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

  public void turn_user_online(String username){
    turn_online(username);
  }

  public void turn_user_offline(String username){
    turn_offline(username);
  }

  private String get_current_time(){
    LocalTime currentTime = LocalTime.now();
    String time = ""+currentTime;
    return time;
  }
 //===============================private methods==========================
 
  private Connection get_connection(){
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
    Connection conn = get_connection();
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
    Connection conn = get_connection();
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
    Connection conn = get_connection();
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
  
  //==================================change values==========================================

  //Maybe view if we can just change all of this to just state = !state or smth like that
  private boolean turn_offline(String username){
    Connection conn = get_connection();

    try{
      String query = " update users set state = 1 where username = ?;";
      
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

  private boolean turn_online(String username){
    Connection conn = get_connection();

    try{
      String query = " update users set state = 0 where username = ?;";
      
      PreparedStatement st = conn.prepareStatement(query);
      st.setString(1, username);

      int rs = st.executeUpdate();
      
      conn.commit();

      String notification = "Insert into notification (type, username, description, is_read) Values (?, ?, ?, 1);";

      String description = "User " + username + " has loggen on at time: " + get_current_time();

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

  private void change_username(String old_username, String new_username, String changer_username){
    Connection conn = get_connection();

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
    Connection conn = get_connection();
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
  
  //========================methods========================================
  public void user_register(int user_type, String name, String username, String password, String email, int NIF, int Phone_number, String address, String activity_sector_, int grade, String speciality){
    Insert_user_register(user_type, name, username, password, email, NIF, Phone_number, address, activity_sector_, grade, speciality);
  }
  
  public void request_register(String username){
    User_reg_request(username);
  }
  //========================checkers=======================================
  private boolean check_username(String username){
    Connection conn = get_connection();
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
    Connection conn = get_connection();
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

    Connection conn = get_connection();

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
    }catch(SQLException e){
      e.printStackTrace();
    }
  }

  private void User_reg_request(String username){
    Connection conn = get_connection();
    
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
