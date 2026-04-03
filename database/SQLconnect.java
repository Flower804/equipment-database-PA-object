package database;

import java.sql.*;
import java.util.Scanner;

import model.User;

/**
 *
 * @author Flower/Gabriel Moita
*/ 

//TODO: add documentation

public class SQLconnect {
  

  //================================accesses==============================
  public static void main(String[] args){
    //TODO; verify if this is really necessary here 

    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;

  }
  
  public boolean get_match(String username, String password){
    boolean result = do_match(username, password);
    return result;
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
 //===============================private methods==========================
 
  private Connection get_connection(){
    /**
     *the get_connection() method serves as a way to 
     *create a connection to the database, in this case db,
      where all the tables and user's data is stored
     */ 

    try{
      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/db", "root", "GabrielMoita_129");
      return conn;
    }catch(SQLException e){
      System.out.println("A sql exception has occured: " + e);
    }
    System.out.println("Connection to the database has failed");
    return null;
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
  
  //==================================change values==========================================

  //Maybe view if we can just change all of this to just state = !state or smth like that
  private boolean turn_offline(String username){
    Connection conn = get_connection();
    ResultSet rs = null;

    try{
      String query = " update users set state = 1 where username = ?;";
      
      PreparedStatement st = conn.prepareStatement(query);
      st.setString(1, username);

      rs = st.executeQuery();

      return true;
    }catch(SQLException e){
      System.out.println("A SQLException has occured: " + e);
      return false;
    }
  }

  private boolean turn_online(String username){
    Connection conn = get_connection();
    ResultSet rs = null;

    try{
      String query = " update users set state = 0 where username = ?;";
      
      PreparedStatement st = conn.prepareStatement(query);
      st.setString(1, username);

      rs = st.executeQuery();

      return true;
    }catch(SQLException e){
      System.out.println("A SQLException has occured: " + e);
      return false;
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
} 
