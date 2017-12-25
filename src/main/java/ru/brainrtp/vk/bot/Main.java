package ru.brainrtp.vk.bot;


import com.petersamokhin.bots.sdk.clients.User;
import com.petersamokhin.bots.sdk.objects.Message;
import jline.console.ConsoleReader;
import ru.brainrtp.vk.bot.config.Configuration;
import ru.brainrtp.vk.bot.config.CoreConfig;
import ru.brainrtp.vk.bot.database.MySQL;
import ru.brainrtp.vk.bot.database.SQLite;
import ru.brainrtp.vk.bot.utils.CC;
import ru.brainrtp.vk.bot.utils.Utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Создано 15.12.17
 */

public class Main {

    static User user;
    private static Configuration configuration;
    private static MySQL mysql;
    private static long start;
    static SQLite sql;
    private static ConsoleReader consoleReader;
    private static Boolean enable = true;

    public static void main(String[] args) {
        start = System.currentTimeMillis();

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
        user = new User(463447379, "9b5e39b0252fbc4149709aeefdb5097736b2defc1d1d2f5eae496cf7e6be74ae846acfdb668fda4f2d027");
        // 9b5e39b0252fbc4149709aeefdb5097736b2defc1d1d2f5eae496cf7e6be74ae846acfdb668fda4f2d027 - Roman * 463447379
        System.out.println("\n");
        log("Бот v0.2 запущен за " + CC.RED + (System.currentTimeMillis() - start) + " мс." + CC.WHITE + " (vk.com/id" + user.getId() + ")\n" + CC.RESET);
        Listeners.onChat(user);
    }


    private static void log(String args) {
        System.out.println(CC.WHITE + "[" + CC.CYAN + time() + CC.WHITE + "] [" + CC.RED + "Инфо" + CC.WHITE + "] > " + CC.RESET + args);
    }

    private static void log(String args, String sender) {
        System.out.println(CC.WHITE + "[" + CC.CYAN + time() + CC.WHITE + "] [" + CC.GREEN + "Чат" + CC.WHITE + "] " + sender + CC.GREEN + " > " + CC.RESET + args);
    }

    static void log(String args, String sender, int id) {
        System.out.println(CC.WHITE + "[" + CC.CYAN + time() + CC.WHITE + "] [" + CC.GREEN + "Чат" + CC.WHITE + "] " + sender + "*" + CC.CYAN + id + CC.GREEN + " > " + CC.RESET + args);
    }

    private static String time() {
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
                                System.out.println(CC.RED + "captain " + CC.RESET + "- Действия со старостами");
                                System.out.println(CC.BLACK_BOLD_BRIGHT + "debug " + CC.RESET + "- включить/выключить debug");
                                System.out.println(CC.BLACK_BOLD_BRIGHT + "permission [user] [permission] " + CC.RESET + "- Установить ползователю [user] права [permisson]");
                                break;
                            }
                            case "stop": {
                                log(CC.RED + "Бот проработал " + CC.RESET + Utils.convert((int) ((System.currentTimeMillis() - start)) / 1000));
                                log(CC.RED + "Выключение..." + CC.RESET);
                                Runtime.getRuntime().exit(0);
                            }
                            case "gc": {
                                System.out.println(CC.RED + "Время работы: " + CC.RESET + Utils.convert((int) ((System.currentTimeMillis() - start)) / 1000));
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
                                    System.out.println(CC.RED + "Недостаточно аргументов. Используйте: '" + CC.RESET + "captain <accept/deny> <idVk>" + CC.RED + "'" + CC.RESET);
                                    break;
                                }
                                if (command[1].equalsIgnoreCase("deny")) {
                                    try {
                                        int idVk = Integer.parseInt(command[2]);
                                        if (sql.select(idVk).get(0) != null) {
                                            sql.delete(idVk);
                                            System.out.println(CC.RED + "Пользователь не подтвержден!" + CC.RESET);
                                            new Message()
                                                    .from(user).
                                                    to(idVk)
                                                    .text("Увы, но Вы не являетесь старостой " + sql.select(idVk).get(1) + "-й группы." +
                                                            "\nЕсли это ошибка, напишите администратору.")
                                                    .send();
                                        }
                                    } catch (Exception ex) {
                                        System.out.println(CC.RED + "Неправльно указан id VK!" + CC.RESET);
                                    }
                                }
                                if (command[1].equalsIgnoreCase("accept")) {
                                    try {
                                        int idVk = Integer.parseInt(command[2]);
                                        if (sql.select(idVk).get(0) != null) {
                                            sql.update(idVk, true);
                                            Student.getStudent(idVk).setPermission("captain");
                                            System.out.println(CC.GREEN + "Пользователь подтвержден!" + CC.RESET);
                                            new Message()
                                                    .from(user)
                                                    .to(idVk)
                                                    .text("Поздравляем теперь Вы староста " + sql.select(idVk).get(1) + "-й группы!"+
                                                            ".\nТеперь вам доступны команды:" +
                                                            "\nсозыв - созвать всех одногруппников" +
                                                            "\nновость - отправить всем новость.")
                                                    .send();
                                        }
                                    } catch (Exception ex) {
                                        System.out.println(CC.RED + "Неправльно указан id VK!" + CC.RESET);
                                    }
                                }
                                if (command[1].equalsIgnoreCase("delete")) {
                                    try {
                                        int idVk = Integer.parseInt(command[2]);
                                        if (sql.select(idVk).get(0) != null) {
                                            sql.delete(idVk);
                                            System.out.println(CC.RED + "Староста убран!" + CC.RESET);
                                            new Message()
                                                    .from(user)
                                                    .to(idVk)
                                                    .text("Увы, но администратор убрал Вас из списка старост." +
                                                            "\nЕсли это ошибка, напишите администратору.")
                                                    .send();
                                        }
                                    } catch (Exception ex) {
                                        System.out.println(CC.RED + "Неправльно указан id VK!" + CC.RESET);
                                    }
                                }
                                break;
                            default: {
                                System.out.println(CC.RED + "Неизвестная команда. Напишите '" + CC.RESET + "help" + CC.RED + "'" + CC.RESET);
                                break;
                            }
                        }
                    } catch (Exception ignored) {
                    }
                } while (enable);
            }
        }, "ConsoleThread").start();
    }
}
