package database;

import java.sql.*;
import java.util.Scanner;
import java.time.LocalTime;
import java.util.Random;
import java.util.ArrayList;

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
    
  public boolean check_SKU_code(int SKU_code){
    return get_amount_SKU(SKU_code);
  }

  public boolean check_if_users_exist(){
    return check_users();
  }

  public boolean get_match(String username, String password){
    boolean result = do_match(username, password);
    return result;
  }

  public boolean check_existing_equipments(String username){
    return check_equipments(username);
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
  
  public void change_my_info(String where, String username, String what, String content){
    change_info(where, username, what, content);
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
  
  public boolean deny_user(String username, String manager, Scanner input){
    return turn_unnacepted(username, manager, input);
  }

  private String get_current_time(){
    LocalTime currentTime = LocalTime.now();
    String time = ""+currentTime;
    return time;
  }

  public boolean get_register_requests(){
    return get_register();
  }

  public int get_number_of_repairs(){
    return number_repairs();
  }

  public boolean save_request(float repair_code, int SKU_code, String responsible_user){
    return save_repair_request(repair_code, SKU_code, responsible_user);
  }

  public ArrayList<User> list_users_by_name(String filter, int offset){
    return list_all_users(filter, offset);
  }

 //===============================private methods==========================
  
  //TODO: dont forget to change this before handing in the assignment, so that it 
  //doesnt have to use the secret

  /**Establishes the connection with the database
   *
   */ 
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
  
  /**Checks if any users exists in the database
   *
   * @return true if there exists users or false if dont
   */
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
  
  /**checks if the given username and password coincide with any existing entry on the database
   * 
   * @param username the username of the user trying to be matched with the given password 
   * @param password the password of the user trying to be matched with the given username
   * @return true if theres a match or false if not
   */ 
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
  
  /**Checks if a username from a user has been accepted by a manager
   *
   * @param username the username of the user that is being checked if it has been accepted
   * @return true if the user has been accepted, and false if not
   */
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
  
  /**Checks if an user has been denied by checking their username on the denied table
   * printing the reason for the deniel if found
   *
   * @param username the username of the user that is being checked if it has been denied
   * @return true if the username is found on the table, or false if the contrary
   */
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
        
        System.out.println("I'm sorry but your registration request has been denied");
        System.out.println("reason: " + rs.getString("description"));

        return true;
      } else {
        return false;
      }
    }catch(SQLException e){
      e.printStackTrace();
      return false;
    }
  }
  
  /**Gets the current number of existing(active and incative) repairs on the repairs database
   *
   * @return the current number of existing repairs in the database
   */
  private int number_repairs(){
    ResultSet rs = null;
    
    try{
      String query = "Select count(*) as total from equipment_repair;";
      
      PreparedStatement st = conn.prepareStatement(query);

      rs = st.executeQuery();
      
      if(rs.next()){
        return rs.getInt("total");
      }
    }catch(SQLException e){
      System.out.println("Sorry an SQLException has occured ");
      e.printStackTrace();
      return 0;
    }

    return 0;
  }
  
  /**Shows all the users in the users table, joint with their information(name, username, email and type)
   *
   *@return true if there exists users in the register, false if dont
   */ 
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

  /**Gets the reason to deny a user and adds the username and the reason to the denied database
   *
   *@param username the username of the user to be rejected
   *@param input the Scanner object being used for inputs from the user
   *
   * @return true if the user is denied succesfully false if not
   */
  private boolean denying_process(String username, Scanner input){
    String reason = "";
    System.out.println("Please insert the reason for rejecting user " + username);

    reason = input.nextLine();

    try{
      String query = "Insert into denied (username, description) Values (?, ?);";

      PreparedStatement st = conn.prepareStatement(query);
      st.setString(1, username);
      st.setString(2, reason);
      
      st.executeUpdate();

      conn.commit();
      return true;
    }catch(SQLException e){
      System.out.println("A SQLException has occures: " + e);
      return false;
    }
  }
 
  //==================================change values==========================================
  
  /**Accepts a user into the database, storing also which manager accepted the respective user
   *
   *@param username the username of the user being accepted
   *@param input the Scanner object being used for inputs from the user
   *
   *@return returns true if the user is accepted succesfully, and false if otherwyse
   */ 
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
      
      st_notif.executeUpdate();

      conn.commit();
      return true;
    }catch(SQLException e){
      System.out.println("A SQLException has occured: " + e);
      return false;
    }
  }
  
  /**denies a user, stopping them from being able to log in
   *
   *@param username the username of the user to be denied
   *@param manager the username of the manager that is denying the respective user
   *@param input the Scanner object being used for inputs from the user
   *
   *@return returns true if denied succesfully, false if else
   */
  private boolean turn_unnacepted(String username, String manager, Scanner input){
    if(denying_process(username, input)){
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
        
        st_notif.executeUpdate();

        conn.commit();
        return true;
      }catch(SQLException e){
        System.out.println("A SQLException has occured: " + e);
        return false;
      }
    } else {
      return false;
    }
  }
  
  /**turns a user online
   *
   *@param username the username of the user to turn online
   *
   *@return true if turned online succesfully and false if else
   */
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
  
  /**turns a user offline
   *
   *@param username the username of the user to be turned offline
   *
   *@return true if the user has been turned offline succesfully, returns false if else
   */ 
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

  /**Changes the username of a User, taking into account the username of the changer
   *
   *@param old_username the current/old username of the user which will have their username changed
   *@param new_username the username which wants to be the new username
   *@param changer_username the username of the user doing the "change username" request
   */
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
  /**Returns a User object via a username
   *
   *@param username the username from the user that wants to be returned
   *@return an User object
   */
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
  
  /**Checks if a SKU code exists in the database
   *
   *@param SKU_to_compare the SKU code being searched
   *@return boolean true if the SKU code already exists in the database, false if contrary 
   */
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
  
  /**saves a repair request made by an user on a table on the database
   *
   *@param repair_code the repair code of the request
   *@param SKU_code the SKU code of the tool being asked to repair
   *@param responsible_user the user responsible for the repair request
   *@return true if the repair request was made succesfully, false if not
   */ 
  private boolean save_repair_request(float repair_code, int SKU_code, String responsible_user){
    try{
      String query = "Insert into equipment_repair(repair_code, SKU_code, request_submission_date, responsible_user, state) Values (?, ?, curdate(), ?, 0);";
    
      PreparedStatement st = conn.prepareStatement(query);
      st.setFloat(1, repair_code);
      st.setInt(2, SKU_code);
      st.setString(3, responsible_user);

      st.executeUpdate();

      conn.commit();
      return true;
    }catch(SQLException e){
      System.out.println("I'm sorry but a SQLException has occures ");
      e.printStackTrace();
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
  /**Checks if a username exists in the database
   *
   *@param username the username to check in the database
   *@return true if the username exists, false if not
   */ 
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

  /**Checks if the NIF exists in the database
   *
   *@param NIF the nif to be searched
   *@return true if the NIF exists, false if not
   */ 
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
  
  /**Turns a given String into a Date
   *
   *@param date the string version of the date that is to be turned into a Date type
   *@return a Date type variable
   */ 
  private Date string_into_Date(String date){
    String day = date.substring(0, 2);
    String month = date.substring(2, 4);
    String year = date.substring(4, 8);

    String date_format = year + "-" + month + "-" + day;

    Date manifacture_date = Date.valueOf(date_format);
    return manifacture_date;
  }
  
  /**Prints the existing equipments belonging to a user via their username
   *
   *@param username the username of the owner of the equipments to be searched
   *@return true if the search was succesfull or false if the searched was empty or error
   */ 
  private boolean check_equipments(String username){ 
    ResultSet rs = null; 
    boolean found = false;

    try{
      String query = "Select brand, model, SKU_code from Equipment where responsible_user = ?;";

      PreparedStatement st = conn.prepareStatement(query);
      st.setString(1, username);
      
      rs = st.executeQuery();

      if(rs.next() == true){
        found = true;
        System.out.println("brand: " + rs.getString("brand") + " : model: " + rs.getString("model") + "SKU code: " + rs.getInt("SKU_code"));
      }

    }catch(SQLException e){
      System.out.println("Sorry a SQLException has occured ");
      e.printStackTrace();
    }

    return found;
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
  
  private void change_info(String where, String username, String what, String content){
    try{
      String query = "update ? set ? = ? where username - ?;";

      PreparedStatement st = conn.prepareStatement(query);
      st.setString(1, where);
      st.setString(2, what);
      st.setString(3, content);
      st.setString(4, username);

      conn.commit();

      System.out.println("Your change was made succesfully");
    }catch(SQLException e){
      System.out.println("Im sorry but a SQLException has occured");
      e.printStackTrace();
    }
  }

  //listings
  private ArrayList<User> list_all_users(String filter, int offset){
    ResultSet rs = null;
    ArrayList<User> users = new ArrayList<User>();

    try{
      String query = "Select * from users where name like ? order by name ASC limit 10 offset ?;";
    
      PreparedStatement st = conn.prepareStatement(query);
      st.setString(1, filter);
      st.setInt(2, offset);
      
      rs = st.executeQuery();

      while(rs.next()){
        User u = new User(rs.getString("name"), rs.getString("username"), rs.getString("password"), rs.getBoolean("state"), rs.getString("email"), rs.getString("type"));
      
        users.add(u);
      }
    }catch(SQLException e){
      System.out.println("Sorry an SQLException has occured: ");
      e.printStackTrace();
    }

    return users;
  } 
} 
