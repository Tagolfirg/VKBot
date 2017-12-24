package ru.brainrtp.vk.bot.database;


import ru.brainrtp.vk.bot.Main;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;

public class SQLite {
    static Connection conn;
    static Statement statmt;
    static PreparedStatement preparedStatement = null;

    private static String getJarPath() throws IOException, URISyntaxException {
        File f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        String jarPath = f.getCanonicalPath().toString();
        String jarDir = jarPath.substring( 0, jarPath.lastIndexOf( File.separator ));
        return jarDir;
    }

    public SQLite() {
        try {
            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection("jdbc:sqlite://" + getJarPath() + "/users.db");
            statmt = conn.createStatement();
            statmt.execute(
                        "CREATE TABLE IF NOT EXISTS `users` (`username` varchar(50) PRIMARY KEY,`eastereggs` varchar(255) NOT NULL)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(String user, String ee) {
        try {
            PreparedStatement e = conn.prepareStatement("INSERT OR REPLACE INTO users (username,eastereggs) VALUES (?,?);");
            e.setString(1, user);
            e.setString(2, ee);
            e.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> select(String user) {
        try {
            preparedStatement = conn.prepareStatement("SELECT * FROM users WHERE username = ?;");
            preparedStatement.setString(1, user);
            ResultSet e = preparedStatement.executeQuery();
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
}