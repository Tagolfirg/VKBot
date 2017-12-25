package ru.brainrtp.vk.bot;


import com.petersamokhin.bots.sdk.clients.User;
import com.petersamokhin.bots.sdk.objects.Message;
import ru.brainrtp.vk.bot.config.Configuration;
import ru.brainrtp.vk.bot.database.MySQL;
import ru.brainrtp.vk.bot.database.SQLite;
import jline.console.ConsoleReader;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Создано 15.12.17
 */

public class Main {

//    private static String acces_token;
    //
    static User user;
//    public static Main instance;
    private static Configuration configuration;
    private static MySQL mysql;
    public static SQLite sql;
    private static ConsoleReader consoleReader;
    private static Boolean enable = true;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        try {
            (consoleReader = new ConsoleReader()).setExpandEvents(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        configuration = CoreConfig.getConfig();

        if (configuration.getBoolean("MySQL.Enable")) {
            mysql = new MySQL
                    (
                            configuration.getString("MySQL.Host"),
                            configuration.getString("MySQL.Port"),
                            configuration.getString("MySQL.Username"),
                            configuration.getString("MySQL.Database"),
                            configuration.getString("MySQL.Password")
                    );
        } else {
            sql = new SQLite();
        }
        startConsoleReader();
//        System.out.println("Пошел на хуй ебаное хуйло");

//        GetToken.auth("vip123321@list.ru", "stast12345STAS");

//        try {
//            acces_token = GetToken.setConnection();
//        } catch (IOException | URISyntaxException e) {
//            e.printStackTrace();
//        }
        user = new User(463447379, "9b5e39b0252fbc4149709aeefdb5097736b2defc1d1d2f5eae496cf7e6be74ae846acfdb668fda4f2d027");
        // 9b5e39b0252fbc4149709aeefdb5097736b2defc1d1d2f5eae496cf7e6be74ae846acfdb668fda4f2d027 - Roman * 463447379
        System.out.println("\n");
        log("Бот v0.2 запущен за " + CC.RED + (System.currentTimeMillis() - start) + " мс." + CC.WHITE + " (vk.com/id" + user.getId() + ")\n" + CC.RESET);
        Listeners.onChat(user);
//        user.onMessage(message -> {
//            new Message()
//                    .from(user)
//                    .to(message.authorId())
//                    .text("Я как бы пидор")
//                    .send();
//        });
    }



    private static void log(String args){
        System.out.println(CC.WHITE + "[" + CC.CYAN + time() + CC.WHITE + "] ["  + CC.RED + "Инфо" + CC.WHITE + "] > " + CC.RESET + args);
    }

    private static void log(String args, String sender){
        System.out.println(CC.WHITE + "[" + CC.CYAN + time() + CC.WHITE + "] [" + CC.GREEN + "Чат" + CC.WHITE + "] " + sender + CC.GREEN + " > " + CC.RESET + args);
    }

    static void log(String args, String sender, int id){
    System.out.println(CC.WHITE + "[" + CC.CYAN + time() + CC.WHITE + "] [" + CC.GREEN + "Чат" + CC.WHITE + "] " + sender + "*" + CC.CYAN +id + CC.GREEN + " > " + CC.RESET + args);
    }

    private static String time(){
        DateTimeFormatter ttf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return ttf.format(now);
    }

    private static void startConsoleReader() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        final String line = consoleReader.readLine("");
                        final String[] command = line.split(" ");
                        final String lowerCase = command[0].toLowerCase();
                        switch (lowerCase) {
                            case "help": {
                                System.out.println(CC.RED + "stop " + CC.RESET + "- Остановить бота");
                                System.out.println(CC.RED + "gc " + CC.RESET + "- Получить данные о памяти");
                                System.out.println(CC.RED + "replay " + CC.RESET + "- Ответить на последнее сообщение");
                                System.out.println(CC.BLACK_BOLD_BRIGHT + "debug " + CC.RESET + "- включить/выключить debug");
                                System.out.println(CC.BLACK_BOLD_BRIGHT + "permission [user] [permission] " + CC.RESET + "- Установить ползователю [user] права [permisson]");
                                break;
                            }
                            case "stop": {
                                log(CC.RED + "Выключение..." + CC.RESET);
                                Runtime.getRuntime().exit(0);
                            }
                            case "gc": {
                                System.out.println(CC.RED + "Максимум памяти: " + CC.GREEN + Runtime.getRuntime().maxMemory() / 1024L / 1024L + " MB" + CC.RESET);
                                System.out.println(CC.RED + "Доступно памяти: " + CC.GREEN + Runtime.getRuntime().totalMemory() / 1024L / 1024L + " MB" + CC.RESET);
                                System.out.println(CC.RED + "Свободно памяти: " + CC.GREEN + Runtime.getRuntime().freeMemory() / 1024L / 1024L + " MB" + CC.RESET);
                                System.out.println(CC.RED + "Использовано памяти: " + CC.GREEN + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024L / 1024L + " MB" + CC.RESET);
                                break;
                            }
                            case "replay": {
                                if (Listeners.latestChat != 0) {
                                    new Message()
                                            .from(user)
                                            .to(Listeners.latestChat)
                                            .text(line.replaceFirst("replay", ""))
                                            .send();
                                    Main.log(line.replaceFirst("replay", ""), "Вы");
                                } else {
                                    Main.log(CC.RED + "Ошибка, последний диалог не найден!");
                                }
                                break;
                            }
                            case "captain":
                                if (command.length == 1) {
                                    System.out.println(CC.RED + "Недостаточно аргументов. Используй: 'captain <accept/deny> <idVk>'" + CC.RESET);
                                    break;
                                }
                                if (command[1].equalsIgnoreCase("deny")) {
                                    try {
                                        int idVk = Integer.parseInt(command[2]);
                                        if (sql.select(idVk).get(0) != null) {
                                            sql.delete(idVk);
                                            System.out.println(CC.RED + "Пользователь не подтвержден!" + CC.RESET);
                                            new Message().from(user).to(idVk).text("Увы, но Вы не являетесь старостой этой группы.\nЕсли это ошибка, напишите администратору.").send();
                                        }
                                    } catch (Exception ex) {
                                        System.out.println(CC.RED + "Неправльно указан idVk!" + CC.RESET);
                                    }
                                }
                                if (command[1].equalsIgnoreCase("accept")) {
                                    try {
                                        int idVk = Integer.parseInt(command[2]);
                                        if (sql.select(idVk).get(0) != null) {
                                            sql.update(idVk, true);
                                            Student.getStudent(idVk).setPermission("captain");
                                            System.out.println(CC.GREEN + "Пользователь подтвержден!" + CC.RESET);
                                            new Message().from(user).to(idVk).text("Поздравляем Вы теперь староста группы " + sql.select(idVk).get(1) + ".\nТеперь вам доступны команды:\nсозыв - созвать всех одногруппников\nновость - отправить всем новость.").send();
                                        }
                                    } catch (Exception ex) {
                                        System.out.println(CC.RED + "Неправльно указан idVk!" + CC.RESET);
                                    }
                                }
                                if (command[1].equalsIgnoreCase("delete")) {
                                    try {
                                        int idVk = Integer.parseInt(command[2]);
                                        if (sql.select(idVk).get(0) != null) {
                                            sql.delete(idVk);
                                            System.out.println(CC.RED + "Пользователь изгнан!" + CC.RESET);
                                            new Message().from(user).to(idVk).text("Увы, но администратор Вас узгнал из старосты.\nЕсли это ошибка, напишите администратору.").send();
                                        }
                                    } catch (Exception ex) {
                                        System.out.println(CC.RED + "Неправльно указан idVk!" + CC.RESET);
                                    }
                                }
                                break;
                            default: {
                                System.out.println(CC.RED + "Неизвестная команда. Напишите '" + CC.RESET + "help" + CC.RED + "'" + CC.RESET);
                                break;
                            }
                        }
                    } catch (Exception ignored) {}
                } while (enable);
            }
        }, "ConsoleThread").start();
    }

//    public static void main(String[] args) {
//
//        Group group = new Group(151083290, "fa1c39c03785c8d2d1dc5a7609c9d2db4b0a6174af20a988360ca0606bb8487208365a14d18e689766920");
//
//        // Yandex SpeechKit API key
//        // http://developer.tech.yandex.ru/keys/
//        String yandexKey = "25574c9b-b572-4963-b0bd-889d5192a221";
//
//        String url = "https://tts.voicetech.yandex.net/generate?format=mp3&lang=ru&speaker=zahar&key=" + yandexKey + "&text=";
//
//        // Voice all text messages
//        group.onSimpleTextMessage(message -> {
//            new Message()
//                    .from(group)
//                    .to(message.authorId())
//                    .sendVoiceMessage(url + URLEncoder.encode(message.getText()));
//        });
//
//        // Send error for other messages
//        group.onMessage(message -> {
//            new Message()
//                    .from(group)
//                    .to(message.authorId())
//                    .text("Sorry, please send me the message that contains only text. I will voice this message.")
//                    .send();
//        });
//    }

//    public static void main(String[] args){
//        Group group = new Group(151083290 , "fa1c39c03785c8d2d1dc5a7609c9d2db4b0a6174af20a988360ca0606bb8487208365a14d18e689766920");
//        System.out.println("+");
//        group.onMessage(message -> {
//            new Message()
//                    .from(group)
//                    .to(message.authorId())
//                    .text("Хуй.")
//                    .send();
//        });

//    }
}
