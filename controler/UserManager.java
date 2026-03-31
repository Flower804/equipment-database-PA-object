package controler;

public class UserManger{
  
  public void registerUser(){
    //validate data
    //create User object
    //set approved = false;
    //save to database
    //notify manager TODO: create table notifications
  }

  public void approveUser(String username){
    //find user in db 
    //set approved = true 
    //update db 
  }

  public void rejectUser(String username){
    //same lofic as approve user but mark as rejected instead of approved 
  }
}
