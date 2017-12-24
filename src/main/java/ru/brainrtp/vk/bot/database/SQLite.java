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
                    "CREATE TABLE IF NOT EXISTS `captain` (`vk_id` varchar(50) PRIMARY KEY,`party` int NOT NULL, `allowed` BOOL)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(String id, int group, int allowed) {
        try {
            PreparedStatement e = conn.prepareStatement("INSERT OR REPLACE INTO captain (vk_id,party,allowed) VALUES (?,?,?);");
            e.setString(1, id);
            e.setInt(2, group);
            e.setInt(2, allowed);
//            e.setString(2, allowed);
            e.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> select(int vk_id) {
        try {
            preparedStatement = conn.prepareStatement("SELECT * FROM captain WHERE vk_id = ?;");
            preparedStatement.setInt(1, vk_id);
            ResultSet e = preparedStatement.executeQuery();
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
}