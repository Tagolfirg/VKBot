package ru.brainrtp.vk.bot;

import com.petersamokhin.bots.sdk.clients.User;
import com.petersamokhin.bots.sdk.objects.Message;
import ru.brainrtp.vk.bot.utils.Utils;

import java.util.HashMap;

/**
 * Создано 22.12.17
 */

class Listeners {

    int superadmin = 147906281;
    private static HashMap<Integer, Long> cooldowns = new HashMap<Integer, Long>();
    private static int cooldownTime = 5;
    static int latestChat = 0;


    static void onChat(User user){

        //TODO: Ниже написан способ работы с чатами. v0.14-alpha
        /*
        user.onMessage(message -> {
            if (message.isMessageFromChat()) {

                // Get chat id
                int chatIdLong = message.getChatIdLong(); // 2000000011
                int chatId = message.chatId();            // 11
                int sender = message.authorId();          // 62802565

                // Handle message, it's from chat
                new Message()
                        .from(user)
                        .to(chatIdLong)
                        .text("Hello, chat!")
                        .send();

            } else {

                // Handle message that not from chat
                new Message()
                        .from(user)
                        .to(message.authorId())
                        .text("Sorry, I will work only in chats.")
                        .send();
            }
        });

        user.onChatMessage(message -> {
            // Handle message, it's from chat
        });
        */

        user.onMessage(message -> {
            if (!Student.students.containsKey(message.authorId())){
                new Student(message.authorId());

                /*
                * TODO:
                * 1) Сделать нормальную таблицу (потом обсудим)
                * 2) Сделать запись в БД [students] только в том случае, если сатроста подтвердил пользователя
                * 3) Возможно ли избавиться от [captain] или оставить как временное хранилище т.к у нас есть
                *    в [students] колонка 'permission' и 'party' по которым мы можем вывести:
                *    'permission' - captain, 'party' - 202, что будет означать - Староста 202-й группы.
                */

                // Сделал для тестов БД....
                if (Main.sql.select(message.authorId()).get(0) != null) {
                    Main.sql.insert(message.authorId(), 202, "student");
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Main.log(message.getText(), Student.getStudent(message.authorId()).getFirstName(), message.authorId());
            int senderId = message.authorId();

            if (cooldowns.containsKey(senderId)) {
                long secondsLeft = ((cooldowns.get(senderId) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
                if (secondsLeft > 0)
                    return;
            }
            cooldowns.put(senderId, System.currentTimeMillis());
            latestChat = message.authorId();
            String first = message.getText().toLowerCase().split(" ")[0];
            switch (first){
                case "инфо": {
                    new Message()
                            .from(user)
                            .to(message.authorId())
                            .text("Ваше имя: " + Student.getStudent(message.authorId()).getFirstName()
                                    + "\nВаш статус: " + Utils.getGroup(Student.getStudent(message.authorId()).getPermission().get(0)))
                            .send();
                    break;
                }



                /*
                * TODO:
                * Доделать старосту:
                * 1) Проверка есть ли этот пользователь в БД, если да, то:
                *    - "Извините, но Вы уже подали заявку на пост старосты group-й группы" //Если allowed - false
                *    - "Вы уже имеется статус старосты group-й группы :-)" //Если allowed - true и id_vk == message.authorId()
                *    - "Староста group-й группы - [id123123|Станислав]" //Если allowed - true и id_vk != message.authorId()
                * 2) Дописать хуйню по отказу (когда администрация отказала), например:
                *    - "Извините, но нам кажется, что Вы не староста group-й группы 🤔"
                */
                
                case "староста": {
                    if (Main.sql.select(senderId) != null && Main.sql.select(senderId).get(2).equalsIgnoreCase("1")){
                        new Message().from(user).to(senderId).text("Вы уже староста группы " + Main.sql.select(senderId).get(1) + ".").send();
                        break;
                    }
                    if (Main.sql.select(senderId) != null) {
                        new Message().from(user).to(senderId).text("Вы уже подали заявку на старосту группы " + Main.sql.select(senderId).get(1) + "!\nВаша заявка будет рассматриваться в течении суток.").send();
                        break;
                    }
                    try {
                        int num = Integer.parseInt(message.getText().split(" ")[1]);
                        Main.sql.insert(message.authorId(), num, false);
                        new Message()
                                .from(user)
                                .to(message.authorId())
                                .text(Student.getStudent(message.authorId()).getFirstName()
                                        + ", Ваша заявка на пост старосты принята!"
                                        + "\nВас оповестят, когда вам выдадут статус старосты")
                                .send();

                    } catch (Exception e) {
                        new Message()
                                .from(user)
                                .to(message.authorId())
                                .text("Приведите сообщения к такому виду:\n"
                                        + "староста группа\n"
                                        + "Например: староста 202")
                                .send();
                    }
                    break;
                }
                default: {
                    new Message()
                            .from(user)
                            .to(message.authorId())
                            .text("Привет, [id" + message.authorId() + "|" + Student.getStudent(message.authorId()).getFirstName()+ "]. В данный момент я занят.")
                            .send();
                    break;
                }}});
        user.enableTyping(true);

    }

}
