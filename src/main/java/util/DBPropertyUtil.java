/*
 * package util;
 * 
 * import java.io.FileInputStream; import java.io.IOException; import
 * java.sql.Connection; import java.sql.DriverManager; import
 * java.sql.SQLException; import java.util.Properties;
 * 
 * public class DBPropertyUtil { public static Connection
 * getConnectionFromProperties(String propertiesFileName) { Properties
 * properties = new Properties(); try (FileInputStream input = new
 * FileInputStream(propertiesFileName)) { properties.load(input); String url =
 * properties.getProperty("db.url"); String user =
 * properties.getProperty("db.user"); String password =
 * properties.getProperty("db.password"); return
 * DriverManager.getConnection(url, user, password); } catch (IOException |
 * SQLException e) { e.printStackTrace(); return null; } } }
 */

package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {
	public static String getConnectionFromProperties (String propertiesFileName) {
		String ConnString = null;
		Properties propsObject =new Properties();
		try(FileInputStream fis = new FileInputStream(propertiesFileName)){
			propsObject.load(fis);
			String url=propsObject.getProperty("db.url");
			String user=propsObject.getProperty("db.user");
			String password=propsObject.getProperty("db.password");
			ConnString=url+"?user="+user+"&password="+password;
		}
		catch(IOException fnf){
			fnf.printStackTrace();
			System.out.println("Unable to load propertis from file");
		}
		return ConnString;
	}
	
}