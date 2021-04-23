package dbConn;

import java.sql.*;

public class MySQLConn {
    
    public MySQLConn(){

    }

    public void insertLicense(String license){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crypto", "root", "");
            // here sonoo is database name, root is username and password
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO licenses (license, 0) VALUES ("+ license +", 0)");
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void getLicenses(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crypto", "root", "");
            // here sonoo is database name, root is username and password
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM licenses");
            while (rs.next())
                System.out.println(rs.getInt("id") + "  " + rs.getString("license") + "  " + rs.getInt("active"));
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
        
}
