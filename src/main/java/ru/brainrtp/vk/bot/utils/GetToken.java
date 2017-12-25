package ru.brainrtp.vk.bot.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

/**
 * Создано 18.12.17
 */

public class GetToken {
    private static String client_id = "6232882";
    private static String scope = "messages";
    private static String redirect_uri = "http://oauth.vk.com/blank.html";
    private static String display = "popup";
    private static String response_type = "token";
//    private static String access_token;
    private static String email = "email@mail.ru";//тут должен быть прописан email
    private static String pass = "password";//тут должен быть прописан пароль



    private static String access_token;

    public static String setConnection() throws IOException, URISyntaxException {
        HttpClient httpClient = new DefaultHttpClient();
// Делаем первый запрос
//        HttpPost post2 = new HttpPost("https://oauth.vk.com/token?grant_type=password&client_id=1914441&client_secret=987123&username=89619481904&password=stas12345STAS&v=5.69&2fa_supported=1");
//        HttpResponse response2;
//        response2 = httpClient.execute(post2);
//        System.out.println("response asdasdasdasd - " + response2);
//        post2.abort();
        HttpPost post = new HttpPost("http://oauth.vk.com/authorize?" +
                "client_id="+client_id+
                "&scope="+scope+
                "&redirect_uri="+redirect_uri+
                "&display="+display+
                "&response_type="+response_type+
                "&v=5.69&state=123456");
        HttpResponse response;
        response = httpClient.execute(post);
        System.out.println("response #1 (42) - " +response + "\n");
        post.abort();
//Получаем редирект
        String HeaderLocation = response.getFirstHeader("location").getValue();
        System.out.println("HeaderLocation - " + HeaderLocation);
        URI RedirectUri = new URI(HeaderLocation);
//Для запроса авторизации необходимо два параметра полученных в первом запросе
//ip_h и to_h
        String ip_h= RedirectUri.getQuery().split("&")[2].split("=")[1];
        String to_h=RedirectUri.getQuery().split("&")[4].split("=")[1];
// Делаем запрос авторизации
//        post = new HttpPost("            String authURL = \"https://oauth.vk.com/token?grant_type=password&client_id=2274003&\" + new StringBuilder().append(\"client_secret=\").append(this.getCLIENT_SECRET()).append(\"&username=\").append(CommonExtensionKt.urlEncode((String)username)).append(\"&password=\").append(CommonExtensionKt.urlEncode((String)password)).toString();\n")
        post = new HttpPost("https://login.vk.com/?act=login&soft=1"+
                "&q=1"+
                "&ip_h="+ip_h+
                "&from_host=oauth.vk.com"+
                "&to="+to_h+
                "&expire=0"+
                "&email="+email+
                "&pass="+pass);
        response = httpClient.execute(post);
        System.out.println("response #2 (61) - " +response + "\n");
        post.abort();
// Получили редирект на подтверждение требований приложения
        HeaderLocation = response.getFirstHeader("location").getValue();
        post = new HttpPost(HeaderLocation);
// Проходим по нему
        response = httpClient.execute(post);
        System.out.println("response #3 (68) - " +response + "\n");
        System.out.println("HeaderLocation - " + HeaderLocation);
        post.abort();
// Теперь последний редирект на получение токена
        HeaderLocation = response.getFirstHeader("Location").getValue();
// Проходим по нему
        post = new HttpPost(HeaderLocation);
        response = httpClient.execute(post);
        post.abort();
// Теперь в след редиректе необходимый токен
        HeaderLocation = response.getFirstHeader("location").getValue();
// Просто спарсим его сплитами
        return access_token = HeaderLocation.split("#")[1].split("&")[0].split("=")[1];
    }

    public String getNewMessage() throws ClientProtocolException, IOException, NoSuchAlgorithmException, URISyntaxException {//формируем строку запроса
        String url = "https://api.vk.com/method/"+
                "messages.get"+
                "?out=0"+
                "&access_token="+access_token
                ;
        String line = "";
        try {
            URL url2 = new URL(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
            line = reader.readLine();
            reader.close();

        } catch (IOException e) {
            // ...
        }
        return line;
    }
}
