package ru.brainrtp.vk.bot;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Создано 22.12.17
 */

public class Student {

    private String firstName, lastName;
    private int id_student;
    static HashMap<Integer, Student> students = new HashMap<>();
    private List<String> permission = new ArrayList<>();


    Student(int id) {
        this.id_student = id;
        Main.user.api().call("users.get", "{user_ids:"+id+"}", response -> {
            String jsonString = response.toString();
            JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();
            JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
            this.lastName = jsonObject.get("last_name").getAsString();
//            id_student = jsonObject.get("id").getAsInt();
            this.firstName = jsonObject.get("first_name").getAsString();
            students.put(this.id_student, this);
            if (id == 147906281){
                permission.add("admin");
            } else {
                permission.add("student");
            }
        });
    }

    public String getFirstName() {
        return this.firstName;
    }

    public List<String> getPermission() {
        return this.permission;
    }

    public void setPermission(String permission) { List<String> perm = new ArrayList<String>(); perm.add(permission); this.permission = perm; }

    public String getLastName() {
        return this.lastName;
    }

    public int getIdStudent() {
        return this.id_student;
    }
    public static Student getStudent(int id) {return students.get(id);}

}
