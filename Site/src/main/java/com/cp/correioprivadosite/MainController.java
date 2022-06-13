package com.cp.correioprivadosite;

import com.cp.correioprivadosite.dados.*;
import com.cp.correioprivadosite.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.RequestBody;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Controller @Slf4j
public class MainController {
    private final static String restful = "http://vmi827195.contaboserver.net:8080";
    private final static String ROLE_PRODUCER = "Produtor";
    private final static String ROLE_CONSUMER = "Consumidor";
    public final static String ACCESS_TOKEN = "access_token";
    public final static String EMAIL_COOKIE = "username";
    public final static String ROLE_COOKIE = "role";

    public Response queryRESTfulPOSTwithToken(
        String mediaTypeString,
        String bodyString,
        String uri,
        String headerType,
        String headerBody,
        String userToken
    ){
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        MediaType mediaType = MediaType.parse(mediaTypeString);
        RequestBody body = RequestBody.create(mediaType, bodyString);
        Request request = new Request.Builder()
                .url(uri)
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + userToken)
                .addHeader(headerType, headerBody)
                .build();

        //Receive the response to the the attemped login
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Response queryRESTfulPOST(
        String mediaTypeString,
        String bodyString,
        String uri,
        String headerType,
        String headerBody
    ){
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        MediaType mediaType = MediaType.parse(mediaTypeString);
        RequestBody body = RequestBody.create(mediaType, bodyString);
        Request request = new Request.Builder()
                .url(uri)
                .method("POST", body)
                .addHeader(headerType, headerBody)
                .build();

        //Receive the response to the the attemped login
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public Response queryRestfulGETwithToken(
            String uri,
            String userToken){
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        MediaType mediaType = MediaType.parse("text/plain");
        Request request = new Request.Builder()
                .url(uri)
                .addHeader("Authorization", "Bearer "+userToken)
                .get()
                .build();

        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Response queryRestfulGET(
            String uri){
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        MediaType mediaType = MediaType.parse("text/plain");
        Request request = new Request.Builder()
                .url(uri)
                .get()
                .build();

        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Response queryUserInfo(
            String userEmail,
            String userToken){

        String url = restful + "/api/userByEmail?email="+userEmail;
        log.info("Querying {} for user info", url);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + userToken)
                .build();
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //Controller for the main page
    @GetMapping(path="/")
    public String index(
            Model model,
            HttpServletRequest webRequest,
            HttpServletResponse webResponse){
        log.info("User is in index");

        String accessToken = Utils.getCookie(webRequest, ACCESS_TOKEN);
        String userEmail   = Utils.getCookie(webRequest, EMAIL_COOKIE);


        boolean isLogged = !accessToken.isEmpty();
        model.addAttribute("isLogged", isLogged);


        // TODO: add name and role to top right corner thingy

        return "/index";
    }
    //Controller for the login page that sends the login info to the restAPI and then gets back the access token
    @GetMapping(path="/login")
    public String login(
            Model model,
            HttpServletRequest webRequest,
            HttpServletResponse webResponse) {
        Cookie[] userCookies = webRequest.getCookies();
        log.info("User is in login");

        // Current User is not logged in
        if(Utils.getCookie(webRequest, ACCESS_TOKEN).equals("")){
            log.info("No Cookies were found");
            model.addAttribute("loginInfo", new LoginForm());
        // Current user has an access token and so is logged in
            model.addAttribute("isLogged", true);
        } else {
            return "redirect:/";
        }



        return "/login";
    }
    @GetMapping("/tryLogin")
    public String tryLogin(
            Model model,
        @RequestParam(required = false) String username,
        @RequestParam(required = false) String password,
        HttpServletRequest webRequest, HttpServletResponse webResponse
    ) throws IOException, NullPointerException, ParseException {

        final String loginURI = restful + "/login";
        log.info("username is {} and password is {}", username, password);

        log.info("Querying {} with {}", loginURI,"username=" + username + "&password=" + password);

        Response response = queryRESTfulPOST("application/x-www-form-urlencoded",
                "username=" + username + "&password=" + password,
                loginURI,
                "Content-Type",
                "application/x-www-form-urlencoded");

        log.info("response = {}", response);
        if (response.code() == 200){
            log.info("The server responded to the the login attempt with: {}", response);
            //Store the received token in a JSON object
            JSONObject token = new JSONObject(response.body().string());
            log.info("User token is: {}", token);

            //This username is actually the user's email.
            Response userInfo = queryUserInfo(username, token.getString("access_token"));
            String answer = userInfo.body().string();
            log.info("User info is: {}", answer);
            JSONObject userJSON = new JSONObject(answer);

            Cookie userTokenCookie = new Cookie(ACCESS_TOKEN, token.getString("access_token"));
            Cookie userName = new Cookie(EMAIL_COOKIE, userJSON.get("email").toString());
            Cookie userRole = new Cookie(ROLE_COOKIE, userJSON.getJSONObject("role").get("name").toString());

            model.addAttribute("isLogged", !userTokenCookie.getValue().isEmpty());

            userTokenCookie.setMaxAge(30 * 60);
            userName.setMaxAge(30 * 60);
            userRole.setMaxAge(30 * 60);
            webResponse.addCookie(userTokenCookie);
            webResponse.addCookie(userName);
            webResponse.addCookie(userRole);

            return "redirect:/";
        } else {
            return "redirect:/login";
        }
    }
    @GetMapping(path="/news")
    public String news(
            Model model,
            HttpServletRequest webRequest,
            HttpServletResponse webResponse,
            @RequestParam(required = false) Long topicID,
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy/MM/dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy/MM/dd") Date endDate
    ) {
        log.info("User is in news");

        String userToken = Utils.getCookie(webRequest, ACCESS_TOKEN);
        String userName  = Utils.getCookie(webRequest, EMAIL_COOKIE);
        String userRole  = Utils.getCookie(webRequest, ROLE_COOKIE);

        RestTemplate restTemplate = new RestTemplate();

        List<Topic> listTopics = new ArrayList<>();
        List<News> listNews = new ArrayList<>();

        final String topicsURI = restful + "/api/topics";
        final String newsURI;

        boolean isLogged = !userToken.isEmpty();
        model.addAttribute("isLogged", isLogged);



        String uriString = restTemplate.getForObject(topicsURI, String.class);
        JSONArray topics = new JSONArray(uriString);

        for (var object : topics) {
            Topic topic = new Topic((JSONObject) object);
            listTopics.add(topic);
        }

        if (topicID != null && startDate != null && endDate != null) {
            newsURI = restful + "/api/newsBetweenDateByTopic";
            uriString = restTemplate.getForObject(newsURI, String.class, topicID, startDate.getTime(), endDate.getTime());
        } else {
            newsURI = restful + "/api/news";
            uriString = restTemplate.getForObject(newsURI, String.class);
        }

        JSONArray news = new JSONArray(uriString);

        for (var object : news) {
            News article = new News((JSONObject) object);
            listNews.add(article);
        }
        Collections.reverse(listNews);

        model.addAttribute("ListArticles", listNews);
        model.addAttribute("ListTopics"  , listTopics);
        model.addAttribute("newFilter"   , new Filter());

        return "/news";
    }

    //Controller that generates a news article and sends it to the restAPI for storage
    @GetMapping(path="/news_create")
    public String news_create(
        Model model,
        HttpServletRequest webRequest,
        HttpServletResponse webResponse
    ) throws IOException {
        //send POST to server to add news to /api/
        log.info("User is in news_create");
        String userToken = Utils.getCookie(webRequest, ACCESS_TOKEN);
        String userEmail  = Utils.getCookie(webRequest, EMAIL_COOKIE);
        String userRole  = Utils.getCookie(webRequest, ROLE_COOKIE);

        //Make sure that only a producer can stay on this page
        if(!userRole.equals(ROLE_PRODUCER))
            return "redirect:/";

        boolean isLogged = !userToken.isEmpty();
        model.addAttribute("isLogged", isLogged);

        //Get Date of when the news was written
        Date rightNow = new Date();

        //Get User that created the news
        Response userInfo = queryUserInfo(userEmail, userToken);
        log.info("User Response: {}", userInfo);
        String answer = userInfo.body().string();
        JSONObject userJSON = new JSONObject(answer);

        Response topicInfo = queryRestfulGET(restful + "/api/topics");
        String body = topicInfo.body().string();
        JSONArray topicsJSON = new JSONArray(body);
        log.info("Topic Array: {}", body);

        List<Topic> topicList = new ArrayList<>();
        for (var topic : topicsJSON) {
            topicList.add(new Topic((JSONObject) topic));
        }

        User author = new User(userJSON);

        model.addAttribute("newArticle", new CreateNews());
        model.addAttribute("listTopics", topicList);

        return "/news_create";
    }

    @GetMapping("/sendNews")
    public String sendNews(
        @RequestParam String title,
        @RequestParam String content,
        @RequestParam String topic_title,
        HttpServletRequest webRequest,
        HttpServletResponse webResponse
    ) {
        log.info("User is in news_create");
        String userToken = Utils.getCookie(webRequest, ACCESS_TOKEN);
        String userEmail  = Utils.getCookie(webRequest, EMAIL_COOKIE);
        String userRole  = Utils.getCookie(webRequest, ROLE_COOKIE);

        Response response = queryRESTfulPOSTwithToken(
            "application/x-www-form-urlencoded",
            String.format(
                "title=%s&content=%semail=%stopic=%s",
                title,
                content,
                userEmail,
                topic_title
            ),
            restful + "/api/news/save",
            "Content-Type",
            "application/x-www-form-urlencoded",
            userToken
        );

        log.info("Save news: {}", response);

        return "redirect:/profile";
    }



    //Controller that displays the chosen news from /news.html to the user
    @GetMapping(path="/news_viewer")
    public String news_viewer(
        Model model,
        HttpServletRequest webRequest,
        HttpServletResponse webResponse,
        @RequestParam String newsId
    ) throws IOException {
        String newsURI = restful + "/api/newsById" + "?id=" + newsId;
        log.info("Querying Restful at: {}", newsURI);

        //query GET to server for news
        log.info("User is in news_viewer");
        String userToken = Utils.getCookie(webRequest, ACCESS_TOKEN);
        String userName  = Utils.getCookie(webRequest, EMAIL_COOKIE);
        String userRole  = Utils.getCookie(webRequest, ROLE_COOKIE);

        model.addAttribute("isLogged", !userToken.isEmpty());

        Response newsResponse = queryRestfulGET(newsURI);
        log.info("Returned news: {}", newsResponse);

        String answer = newsResponse.body().string();
        JSONObject newsJSON = new JSONObject(answer);
        News news = new News(newsJSON);

        model.addAttribute("news", news);

        return "/news_viewer.html";
    }

    //Controller that gets the user info from the RestAPI and displays it to the user
    @GetMapping(path="/profile")
    public String profile(
        Model model,
        HttpServletRequest webRequest,
        HttpServletResponse webResponse
    ) throws JSONException, RuntimeException, IOException, java.text.ParseException {
        log.info("User is in profile");
        String userToken = Utils.getCookie(webRequest, ACCESS_TOKEN);
        String userName  = Utils.getCookie(webRequest, EMAIL_COOKIE);
        String userRole  = Utils.getCookie(webRequest, ROLE_COOKIE);

        Response userProfile = queryUserInfo(userName, userToken);
        JSONObject userJSON = new JSONObject(userProfile.body().string());
        User user = new User(userJSON);

        model.addAttribute("isLogged", !userToken.isEmpty());

        model.addAttribute("user_name", user.getName());
        model.addAttribute("user_mail", user.getEmail());
        model.addAttribute("user_role", user.getRole().getName());

        boolean isConsumer = user.getRole().getId() == 1;
        model.addAttribute("isConsumer", isConsumer);

        String uri = "";

        if (isConsumer) {
            uri = restful + "/api/topic_subscribedByUser?id=" + user.getId();
            List<Topic> listTopics = new ArrayList<>();

            Response topics = queryRestfulGET(uri);
            String body = topics.body().string();
            JSONArray topicsJSON = new JSONArray(body);

            log.info("Body {}", body);

            for (var object : topicsJSON) {
                TopicSubscribed topicSubscribed = new TopicSubscribed((JSONObject) object);
                listTopics.add(topicSubscribed.getTopic());
            }

            log.info("Recieves topics are: {}", listTopics);


            model.addAttribute("userSubTopics", listTopics);
        } else {
            uri = restful + "/api/newsByUser?id=" + user.getId();
            List<News> listNews = new ArrayList<>();

            Response news = queryRestfulGET(uri);
            JSONArray newsJSON = new JSONArray(news.body().string());

            for (var object : newsJSON) {
                News article = new News((JSONObject) object);
                listNews.add(article);
            }

            log.info("Recieves topics are: {}", listNews);
            model.addAttribute("userArticles", listNews);
        }

        return "/profile.html";
    }

    //Controller that sends a new user to be added to the User Database
    @GetMapping(path="/register")
    public String register(
            Model model,
            HttpServletRequest webRequest,
            HttpServletResponse webResponse) {
        log.info("User is in register");

        String userToken = Utils.getCookie(webRequest, ACCESS_TOKEN);
        String userName  = Utils.getCookie(webRequest, EMAIL_COOKIE);
        String userRole  = Utils.getCookie(webRequest, ROLE_COOKIE);

        boolean isLogged = !userToken.isEmpty();
        model.addAttribute("isLogged", isLogged);

        //Does this work?
        model.addAttribute("registerInfo", new RegisterForm());
        return "/register.html";
    }

    @GetMapping("/registerInfo")
    public String registerInfo(
        Model model,
        HttpServletRequest webRequest,
        HttpServletResponse webResponse,
        @RequestParam int role,
        @RequestParam String firstname,
        @RequestParam String lastname,
        @RequestParam String email,
        @RequestParam String password,
        @RequestParam String confirmedPassword
    ) throws IOException {
        final String registerURI = restful + "/api/user/save";
        log.info("User Details are: {};{};{};{};{};{}",role,firstname,lastname,email,password,confirmedPassword);

        String roleName;

        if (role == 1)
            roleName = "Produtor";
        else
            roleName = "Consumidor";

        if(firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty() || confirmedPassword.isEmpty()) {
            if (password.equals(confirmedPassword)) {

                String body = "name=" + firstname +
                        "&surname" + lastname +
                        "&email=" + email +
                        "&password=" + password +
                        "&role=" + roleName;

                Response registerResponse = queryRESTfulPOST("application/x-www-form-urlencoded",
                        body,
                        registerURI,
                        "Content-Type",
                        "application/x-www-form-urlencoded");


                // if the registration is successful then login the user
                if (registerResponse.code() == 200) {
                    log.info("The server responded to the register attempt with: {}", registerResponse);
                    final String loginURI = restful + "/login";

                    Response responseLogin = queryRESTfulPOST("application/x-www-form-urlencoded",
                            "username=" + email + "&password=" + password,
                            loginURI,
                            "Content-Type",
                            "application/x-www-form-urlencoded");

                    log.info("The server responded to the login attempt with: {}", responseLogin);

                    //Store the received token in a JSON object
                    assert responseLogin.body() != null;
                    String jsonToken = responseLogin.body().string();
                    JSONObject token = new JSONObject(jsonToken);
                    //Store the token in a cookie



                    Cookie userTokenCookie = new Cookie(ACCESS_TOKEN, token.getString("access_token"));
                    userTokenCookie.setMaxAge(10 * 60); //it's in seconds

                    return "redirect:/";
                }
            }
        }
        return "redirect:/register";
    }
    //Controller that displays all topics from the RestAPI
    @GetMapping(path="/topics")
    public String topics(
            Model model,
            HttpServletRequest webRequest,
            HttpServletResponse webResponse)
            throws IOException{
        log.info("User is in topics");
        String userToken = Utils.getCookie(webRequest, ACCESS_TOKEN);
        String userName  = Utils.getCookie(webRequest, EMAIL_COOKIE);
        String userRole  = Utils.getCookie(webRequest, ROLE_COOKIE);

        //query a GET to the server
        final String topicsURI = restful + "/api/allRecentNews";

        boolean isLogged = !userToken.isEmpty();
        model.addAttribute("isLogged", isLogged);

        List<News> listNews = new ArrayList<>();

        Response topics = queryRestfulGET(topicsURI);
        String body = topics.body().string();
        log.info("topics.body().string() " + body);
        JSONArray topicsJSON = new JSONArray(body);
        for (var object : topicsJSON) {
            News article = new News((JSONObject) object);
            listNews.add(article);
        }
        log.info("Recieves topics are: {}", listNews);

        model.addAttribute("lastArticle", listNews);

        return "/topics.html";
    }

    @GetMapping(path="/topic_unsub")
    public String topic_unsub(
        @RequestParam String userMail,
        @RequestParam String topicTitle
    ) {
        String uri = restful + "/api/topic_subscribed/unsubscribe" +
            "?email=" + userMail + "&title=" + topicTitle;

        MediaType mediaType = MediaType.parse("text/plain");
        Request request = new Request.Builder().url(uri).delete().build();

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return "redirect:/profile";
    }

    @GetMapping(path="/signout")
    public String signout(
            Model model,
            HttpServletRequest webRequest,
            HttpServletResponse webResponse){

        Cookie[] userCookies = webRequest.getCookies();

        for (Cookie c : userCookies){
            log.info("Deleting cookie {}", c);
            c.setMaxAge(0);
            webResponse.addCookie(c);
        }
        return "redirect:/";
    }
}