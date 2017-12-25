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

        // Мутил дичь, в итоге забил...
        // TODO: Сделать нормальный форматер времени.... (проблема с тем, что сейчас может быть 1 день 30 часов 61526 минут
        // TODO: Ну а еще замутить склоненеия

        int minutes = 0;
        int hour = 0;
        int day = 0;

        if (time >= 60) {
            minutes = time / 60;

            if (time >= 3600) {
                hour = time / 3600;

                if (time >= 86400){
                    day = time / 86400;
                }
            }
        }

        return (day != 0 ? day + " дней" : "")
                + (hour != 0 ? hour + " часов" : "")
                + (minutes !=0 ? minutes + " минут": time + " секнуд");
    }
}
