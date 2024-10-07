/*
 * package util;
 * 
 * import java.sql.Connection; import java.sql.DriverManager; import
 * java.sql.SQLException;
 * 
 * public class DBConnUtil { private static final String URL =
 * "jdbc:mysql://localhost:3306/CarConnect"; // Update with your DB URL private
 * static final String USER = "root"; // Update with your DB username private
 * static final String PASSWORD = "Abcd1234@"; // Update with your DB password
 * 
 * // Method to get a database connection public static Connection
 * getConnection() throws SQLException { return DriverManager.getConnection(URL,
 * USER, PASSWORD); } }
 */


package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnUtil{
	public static Connection getConnection(String ConnString) {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			//System.out.println("Driver Loaded");
			con = DriverManager.getConnection(ConnString);
			//System.out.println("Database Connected");
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("Driver not Loaded");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			System.out.println("Database failed to Connect");
		}
		return con;
	}
}