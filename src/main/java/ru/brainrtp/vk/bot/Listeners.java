package ru.brainrtp.vk.bot;

import com.petersamokhin.bots.sdk.clients.User;
import com.petersamokhin.bots.sdk.objects.Message;

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
                                    + "\nВаш статус: " + getGroup(Student.getStudent(message.authorId()).getPermission().get(0)))
                            .send();
                    break;
                }
                case "тест": {
//                    Main.sql.insert(message.authorId(), Integer.valueOf(message.getText().toLowerCase().split(" ")[1]), false);
                    Main.sql.insert(String.valueOf(message.authorId()), 201, 0);
//                    System.out.println("2");
                    new Message()
                            .from(user)
                            .to(message.authorId())
                            .text("Ваше имя: " + Student.getStudent(message.authorId()).getFirstName()
                                    + "\nВаш статус: " + getGroup(Student.getStudent(message.authorId()).getPermission().get(0)))
                            .send();
//                    System.out.println("3");
                    System.out.println(Main.sql.select(message.authorId()));
                    break;

                }
//                case "голос": {
//                    new Message()
//                            .from(user)
//                            .to(message.authorId())
//                            .text("Ваше имя: " + Student.getStudent(message.authorId()).getFirstName()
//                                    + "\nВаш статус: " + getGroup(Student.getStudent(message.authorId()).getPermission().get(0)))
//                            .send();
//                    break;
//                }
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

    private static String getGroup(String permission){
        if (permission.equals("admin")){
            return "Администратор";
        } else {
            return "Студент";
        }
    }
}
