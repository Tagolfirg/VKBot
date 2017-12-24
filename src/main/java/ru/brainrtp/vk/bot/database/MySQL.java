package ru.brainrtp.vk.bot.database;

import java.sql.*;
import java.util.ArrayList;

/**
 * Создано 06.11.17
 */

public class MySQL {

    private Connection connection;
    private String host, database, username, password, port;
    private Statement statement;

    public MySQL(String host, String port, String user, String database, String password){
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = user;
        this.password = password;
        try {
            openConnection();
            statement = connection.createStatement();
            createDataBase();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database +
                    "?allowMultiQueries=true&useUnicode=true&" + "characterEncoding=UTF-8&",
                    this.username, this.password);
        }
    }
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                statement.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public ResultSet query(String request) {
        ResultSet result;
        try {
            openConnection();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        try {
            result = connection.createStatement().executeQuery(request);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    private void createDataBase() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS `users` (" +
                        "`id` INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                        "`username` VARCHAR(50) NOT NULL UNIQUE, " +
                        "`eastereggs` VARCHAR(255)" +
                        ");";
//        String sql = "CREATE TABLE IF NOT EXISTS myTable(Something varchar(64));";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.executeUpdate();
    }

    public void insert(String user, String ee){

        if (hasUser(user)){
            try {
                PreparedStatement stmt = connection.prepareStatement("UPDATE `users` SET `eastereggs`='"+ ee +"' WHERE `username`='" + user.toLowerCase() + "' ");
                stmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO users (username,eastereggs) VALUES (?,?);");
//            PreparedStatement stmt = connection.prepareStatement("INSERT INTO `users`(`username`, `eastereggs`) VALUES('?', '?') ON DUPLICATE KEY UPDATE `e`=`Huynya`+'1';");
                stmt.setString(1, user.toLowerCase());
                stmt.setString(2, ee);
                stmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public ArrayList<String> select(String user) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?;");
            stmt.setString(1, user);
            ResultSet e = stmt.executeQuery();
            ArrayList<String> item = new ArrayList<String>();
            if (e.next()) {
                item.add(e.getString("username"));
                item.add(e.getString("eastereggs"));
                return item;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean hasUser(String user) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?;");
            stmt.setString(1, user);
            ResultSet e = stmt.executeQuery();
            if (e.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
