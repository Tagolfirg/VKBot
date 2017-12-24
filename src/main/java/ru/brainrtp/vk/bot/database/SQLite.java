package ru.brainrtp.vk.bot.database;


import ru.brainrtp.vk.bot.Main;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;

public class SQLite {
    private static Connection conn;

    private static String getJarPath() throws IOException, URISyntaxException {
        File f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        String jarPath = f.getCanonicalPath();
        return jarPath.substring( 0, jarPath.lastIndexOf( File.separator ));
    }

    public SQLite() {
        try {
            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection("jdbc:sqlite://" + getJarPath() + "/users.db");
            Statement statmt = conn.createStatement();
            statmt.execute(
                    "CREATE TABL    E IF NOT EXISTS [captain]( \n" +
                            " [vk_id] INTEGER NOT NULL UNIQUE, \n" +
                            " [party] INTEGER NOT NULL, \n" +
                            " [allowed] BOOL DEFAULT 0);");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(int id, int group, boolean allowed) {
        try {
            PreparedStatement e = conn.prepareStatement(
                    "INSERT OR REPLACE INTO captain (vk_id,party,allowed) VALUES (?,?,?);");
            e.setInt(1, id);
            e.setInt(2, group);
            e.setBoolean(3, allowed);
            e.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> select(int vk_id) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM captain WHERE vk_id = ?;");
            preparedStatement.setInt(1, vk_id);
            ResultSet e = preparedStatement.executeQuery();
            ArrayList<String> item = new ArrayList<String>();
            if (e.next()) {
                item.add(e.getString("vk_id"));
                item.add(e.getString("party"));
                item.add(e.getString("allowed"));
                e.close();
                return item;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}