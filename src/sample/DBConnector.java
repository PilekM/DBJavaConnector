package sample;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;
import java.util.concurrent.Callable;

public class DBConnector {

    private Connection connection;
    private Statement statement;
    private CallableStatement callStatement;
    private ResultSet resultSet;

    public DBConnector() {
        try {
            Properties prop = new Properties();

            prop.load(new FileInputStream("config.properties"));
            String user = prop.getProperty("dbuser6");
            String pass = prop.getProperty("dbpassword3");
            String host = prop.getProperty("database2");
            String basename = prop.getProperty("dbuser7");
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+basename, user, pass);
            this.statement = this.connection.createStatement();
        } catch (Exception var2) {
            System.out.println("Error: " + var2);
        }

    }

    public ResultSet callSaldo() throws SQLException {
        this.callStatement = connection.prepareCall("{call saldo()}");
        this.callStatement.execute();
        ResultSet resultSet = this.callStatement.getResultSet();
        return resultSet;
    }

    public ResultSet giveDatafromQuery(String query) throws SQLException {

            this.resultSet = this.statement.executeQuery(query);
        return resultSet;
    }

    public void updateTable(String update) throws SQLException {
        this.statement.executeUpdate(update);
    }
}
