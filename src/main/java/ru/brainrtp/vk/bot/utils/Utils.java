package ru.brainrtp.vk.bot.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.petersamokhin.bots.sdk.clients.User;

/**
 * Создано 22.12.17
 */

public class Utils {
    private static String first_name = "";
    private static String last_name = "";

    static String getFirstName(User user, Integer id) {
        user.api().call("users.get", "{user_ids:"+id+"}", response -> {
            String jsonString = response.toString();
            JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();
            JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
//            String last_name = jsonObject.get("last_name").getAsString();
            first_name = jsonObject.get("first_name").getAsString();
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return first_name;
    }
    static String getLastName(User user, Integer id) {
        user.api().call("users.get", "{user_ids:"+id+"}", response -> {
            String jsonString = response.toString();
            JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();
            JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
            last_name = jsonObject.get("last_name").getAsString();
//            first_name = jsonObject.get("first_name").getAsString();
        });
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return last_name;
    }
    
    public static String convert(int time){

        long seconds = (time / 1000) % 60;
        long minutes = (time / (1000 * 60)) % 60;
        long hours = (time / (1000 * 60 * 60)) % 24;
        long days = (time / (1000 * 60 * 60 * 24));

        return (days != 0 ? days + " " +formatTime((int) days, "день", "дня", "дней") : "")
                + (hours != 0 ? hours + " " + formatTime((int) hours, "час", "часа", "часов") : "")
                + (minutes !=0 ? minutes + " " + formatTime((int) minutes, "минута", "минуты", "минут"):
                seconds + " " + formatTime((int) seconds, "секунда", "секунды", "секунд"));

    }
    private static String formatTime(int num, String single, String lessfive, String others) {
        if (num % 100 > 10 && num % 100 < 15) return others;
        switch (num % 10) {
            case 1:
                return single;
            case 2:
            case 3:
            case 4:
                return lessfive;
            default:
                return others;
        }
    }

    public static String getGroup(String permission){
        if (permission.equals("admin")){
            return "Администратор";
        } else if (permission.equalsIgnoreCase("captain")) {
            return "Староста";
        } else {
            return "Студент";
        }
    }
    public static String getGroupColor(String permission){
        if (permission.equalsIgnoreCase("admin")){
            return CC.RED + "Администратор";
        } else if (permission.equalsIgnoreCase("captain")) {
            return CC.GREEN + "Староста";
        } else {
            return CC.CYAN + "Студент";
        }
    }
}
