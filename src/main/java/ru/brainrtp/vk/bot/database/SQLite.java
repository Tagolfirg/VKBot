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
                        "CREATE TABLE IF NOT EXISTS `vk_wait` (`vk_id` INT NOT NULL,`group` INT NOT NULL, `allowed` BOOLEAN NOT NULL)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(int id, int group, boolean allowed) {
        try {
            PreparedStatement e = conn.prepareStatement("INSERT OR REPLACE INTO vk_wait (vk_id,group,allowed) VALUES (?,?,?);");
            e.setInt(1, id);
            e.setInt(2, group);
            e.setBoolean(2, allowed);
            e.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> select(int vk_id) {
        try {
            preparedStatement = conn.prepareStatement("SELECT * FROM vk_wait WHERE vk_id = ?;");
            preparedStatement.setInt(1, vk_id);
            ResultSet e = preparedStatement.executeQuery();
            ArrayList<String> item = new ArrayList<String>();
            if (e.next()) {
                item.add(e.getString("group"));
                item.add(e.getString("allowed"));
                return item;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}