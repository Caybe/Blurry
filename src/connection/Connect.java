package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {

    private static String url = "jdbc:postgresql://localhost:5432/blurrydb";
    private static String user = "postgres";
    private static String passwd = "admin";
    private static Connection connect;

    public static Connection getInstance(){
        if(connect == null){
            try {
                connect = (Connection) DriverManager.getConnection(url, user, passwd);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connect;
    }

}

