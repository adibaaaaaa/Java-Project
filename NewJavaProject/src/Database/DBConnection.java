package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DBConnection {

    protected static final String HOST = "jdbc:mysql://localhost:3306/";
    protected static final String USER = "root";
    protected static final String PASSWORD = "Adi3568@irA";

    protected String databaseName;

    public DBConnection(String databaseName) {
        this.databaseName = databaseName;
    }

    public Connection connect() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(HOST + databaseName, USER, PASSWORD);
            System.out.println("Connected to " + databaseName + " successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
}
