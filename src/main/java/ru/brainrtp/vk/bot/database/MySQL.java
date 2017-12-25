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
        String sql = "CREATE TABLE IF NOT EXISTS `captain` (" +
                "`id` INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                "`vk_id` INTEGER NOT NULL UNIQUE, " +
                "`party` INTEGER NOT NULL" +
                "`allowed` BOOLEAN DEFAULT 0" +
                ");";
//        String sql = "CREATE TABLE IF NOT EXISTS myTable(Something varchar(64));";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.executeUpdate();
    }

    ///

    public void insert(int id, int group, boolean allowed) {

        if (hasUser(id)){
            try {
                PreparedStatement stmt = connection.prepareStatement("UPDATE `captain` SET `allowed`='"+ allowed +"' WHERE `vk_id`='" + id + "' ");
                stmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO captain (vk_id,party,allowed) VALUES (?,?,?);");
//            PreparedStatement stmt = connection.prepareStatement("INSERT INTO `users`(`username`, `eastereggs`) VALUES('?', '?') ON DUPLICATE KEY UPDATE `e`=`Huynya`+'1';");
                stmt.setInt(1, id);
                stmt.setInt(2, group);
                stmt.setBoolean(3, allowed);
                stmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public ArrayList<String> select(int vk_id) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM captain WHERE vk_id = ?;");
            stmt.setInt(1, vk_id);
            ResultSet e = stmt.executeQuery();
            ArrayList<String> item = new ArrayList<String>();
            if (e.next()) {
                item.add(e.getString("vk_id"));
                item.add(e.getString("party"));
                item.add(e.getString("allowed"));
                return item;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean hasUser(int vk_id) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM captain WHERE vk_id = ?;");
            stmt.setInt(1, vk_id);
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