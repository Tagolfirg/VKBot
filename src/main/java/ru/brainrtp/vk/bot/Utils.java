package ru.brainrtp.vk.bot;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.petersamokhin.bots.sdk.clients.User;

/**
 * Создано 22.12.17
 */

class Utils {
    private static String first_name = "";
    private static String last_name = "";

    static String getFirstName(User user, Integer id) {
        user.api().call("users.get", "{user_ids:"+id+"}", response -> {
            String jsonString = response.toString();
            JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();
            JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
//            String last_name = jsonObject.get("last_name").getAsString();
//            int id_user = jsonObject.get("id").getAsInt()д
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
//            int id_user = jsonObject.get("id").getAsInt()д
//            first_name = jsonObject.get("first_name").getAsString();
        });
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return last_name;
    }
}
