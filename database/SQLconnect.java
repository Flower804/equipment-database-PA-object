package database;

import java.sql.*;
import model.User;

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

  public boolean get_match(String username, String password){
    boolean result = do_match(username, password);
    return result;
  }
  
  public User load_user(String username){
    return get_user(username);
  }

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

} 
