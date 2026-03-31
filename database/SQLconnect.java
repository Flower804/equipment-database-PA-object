package database;

import java.sql.*;

public class SQLconnect {

  public static void main(String[] args){
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;

  }

  private Connection get_connection(){
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
    Statement st = null;
    ResultSet rs = null;

    try{
      st = conn.createStatement();

      rs = st.executeQuery(" Select * from users where username = '" + username + "' and password = '" + password + "' ;");
    
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
  
  //Maybe view if we can just change all of this to just state = !state or smth like that
  private boolean turn_offline(String username){
    Connection conn = get_connection();
    Statement st = null;
    ResultSet rs = null;

    try{
      st = conn.createStatement();

      rs = st.executeQuery(" update users set state = 1 where username = '" + username + "';");
      return true;
    }catch(SQLException e){
      System.out.println("A SQLException has occured: " + e);
      return false;
    }
  }

  private boolean turn_online(String username){
    Connection conn = get_connection();
    Statement st = null;
    ResultSet rs = null;

    try{
      st = conn.createStatement();

      rs = st.executeQuery(" update users set state = 0 where username = '" + username + "';");
      return true;
    }catch(SQLException e){
      System.out.println("A SQLException has occured: " + e);
      return false;
    }
  }

  public boolean get_match(String username, String password){
    boolean result = do_match(username, password);
    return result;
  }

} 
