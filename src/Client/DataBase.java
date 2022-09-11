package Client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Yonatan Addis
 */
public class DataBase {
static Connection con;
    public static Connection getConnection() throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.jdbc.Driver");
        con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost/chat_application","root","");
        return con;
    }
    public static void write_to_db(String sql) throws Exception
    {
        Connection con = DataBase.getConnection();
        Statement stmt = con.createStatement();
        stmt.execute(sql);
    }
    public static ResultSet read_from_db(String sql) throws Exception
    {
        Connection con = DataBase.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        return rs;
    }
    public static int update_db(String sql) throws Exception
    {
        Connection con = DataBase.getConnection();
        Statement stmt = con.createStatement();
        return stmt.executeUpdate(sql);
    }
}