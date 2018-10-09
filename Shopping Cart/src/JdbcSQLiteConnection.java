import java.sql.*;
import java.util.*;

/**
 * This program demonstrates making JDBC connection to a SQLite database.
 * @author www.codejava.net
 *
 */
public class JdbcSQLiteConnection {
	
  private static User admin = new User("admin", "admin", "lax", "wi", "54601", 
			"12345678912345678");
	
  private static String dbURL = "jdbc:sqlite:UsersDb.db";
  private static Connection conn;

  private String CREATE_TABLE = "CREATE TABLE Users ("
  		+						"Username TEXT PRIMARY KEY,"
  		+ 						"Password TEXT,"
  		+ 						"City TEXT,"
  		+ 						"State TEXT,"
  		+ 						"Zipcode TEXT,"
  		+ 						"Creditcard TEXT,"
  		+ 						"Status INTEGER);";
  

  private String DROP_TABLE = "DROP TABLE IF EXISTS Users";
  
  private String INSERT_INTO = "INSERT INTO Users (\r\n" + 
  		"                        Username,\r\n" + 
  		"                        Password,\r\n" + 
  		"                        City,\r\n" + 
  		"                        State,\r\n" + 
  		"                        Zipcode,\r\n" + 
  		"                        Creditcard,\r\n" + 
  		"                        Status\r\n" + 
  		"                    )";
  
  private String SEARCH_USERNAMES = "SELECT Username FROM Users";
  
  private String SEARCH_ALL_ATTRS = "SELECT * FROM Users";
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:UsersDb.db";
            conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
            	//create admin account info
            	
            	JdbcSQLiteConnection db = new JdbcSQLiteConnection();
            	//drop the old table
            	if (db.dropTable == true) {
            		System.out.println("Dropped table");
            		db.dropTable();
            	}
            	//create table
            	db.createTable();
            	System.out.println("created table");
            	//add user to database
            	db.addUserToDatabase(admin);
                System.out.println("Connected to the database");
                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());
                conn.close();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void addUserToDatabase(User newUser) {
    	//MUST HASH PASSWORD
   
      String userName = newUser.userName;
      String userPass = newUser.password;
      String city = newUser.city;
      String state = newUser.state;
      String zip = newUser.zipCode;
      String credit = newUser.creditCard;
      int userType = newUser.userType;
      try {
    	String temp = INSERT_INTO + " VALUES ('" + userName + "', '" + userPass + "', '" + city + "', '" + state +"', '" + zip + 
    			"', '" + credit + "', '" + userType + "');";
    	//remove conn line below -- call open connection immediately after creating DB
    	conn = DriverManager.getConnection(dbURL);
    	Statement statement = conn.createStatement();
      	statement.executeUpdate(temp);
      } catch (SQLException e) {
        e.printStackTrace();
      }
     
    }
    
    private boolean dropTable;
    //if dropTable == true
    //statement.executeUpdate(DROP_TABLE); should be uncommented 
    // OR 
    // the dropTable method should be called upon creation of the JDBC object.
    public JdbcSQLiteConnection () {
    	//dropTable == true on first run to clear the database/tables
    	//dropTable == false to keep existing info in database
    	dropTable = true;
    }
    
    public void dropTable() {
    	try {
			Statement statement = conn.createStatement();
			statement.executeUpdate(DROP_TABLE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void createTable() {
    	try {
			Statement statement = conn.createStatement();
			if (dropTable == true) {
				dropTable();
			}
			statement.executeUpdate(CREATE_TABLE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * call this after creating the database
     */
    public void openConnection() {
    	try {
			conn = DriverManager.getConnection(dbURL);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * call this when done using the database/close program
     */
    public void closeConnection() {
    	try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * 
     * @param username the name to search for in the database
     * @return
     */
    public boolean searchUserName(String username) {
    	ResultSet rs = null;
    	boolean userInDb = false;
    	try {
			Statement st = conn.createStatement();
			rs = st.executeQuery(SEARCH_USERNAMES);
			String temp = "";
			while(rs.next()) {
				temp = rs.getString(0);
				if (username.equalsIgnoreCase(temp)) {
					userInDb = true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return userInDb;
    }
    
    public User getUserInfo(String username) {
    	User user = null;
    	ResultSet  rs = null;
    	if (searchUserName(username) == true) { //found the user so return their info
    		
    	} 
    	//else user will be null and return null
    	return user;
    }
}


/*private String CREATE = "CREATE TABLE Users (\r\n" + 
	"    Username   TEXT    PRIMARY KEY,\r\n" + 
	"    Password   TEXT,\r\n" + 
	"    City       TEXT,\r\n" + 
	"    State      TEXT,\r\n" + 
	"    Zipcode    TEXT,\r\n" + 
	"    Creditcard TEXT,\r\n" + 
	"    Status     INTEGER\r\n" + 
	");";*/

//private String INSERT = "INSERT INTO Users(Username, Password, Status) VALUES(?,?,?)";
