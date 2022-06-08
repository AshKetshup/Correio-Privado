package com.cp.correioprivadosite;

import com.cp.correioprivadosite.dados.*;
import com.cp.correioprivadosite.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.RequestBody;
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
    private final static String restful = "http://localhost:8081";
    private final static String accessToken = "access_token";
    private final static String userNameCookieName = "username";
    private final static String userRoleCookieName = "role";
    public final static String ACCESS_TOKEN = "access_token";
    public final static String EMAIL_COOKIE = "username";
    public final static String ROLE_COOKIE = "role";

    public Response queryRESTful (
        String mediaTypeString,
        String bodyString,
        String uri,
        String method,
        String headerType,
        String headerBody
    ) {
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
    public Response queryRESTfulPOST (String mediaTypeString, String bodyString, String uri,
                                      String headerType, String headerBody){

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

    public Response queryRestfulGETwithToken(String uri, String userToken){

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
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

    public Response queryRestfulGETwithToken(String uri){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
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
    public Response queryUserInfo(String userEmail, String userToken){

        String url = restful+"/api/userByEmail?email="+userEmail;
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
    public String getCookieValue(Cookie[] Cookies, String searchKey) {

        if (Cookies == null) {
            log.info("No Cookies were found");
        } else {
            log.info("Found {} Cookies", Cookies.length);
            for (int i = 0; i < Cookies.length; i++) {
                if (Cookies[i].getName() == searchKey) {
                    log.info("Access token found: {}", Cookies[i].getValue());
                    return Cookies[i].getValue();
                }
            }
        }
        return "";
    }
    //Controller for the main page
    @GetMapping(path="/")
    public String index(Model model, HttpServletRequest webRequest, HttpServletResponse webResponse){
        //no honest idea what this needs from the server
        log.info("User is in index");
        Cookie[] userCookies = webRequest.getCookies();

        String accessToken = Utils.getCookie(webRequest, ACCESS_TOKEN);
        String userEmail   = Utils.getCookie(webRequest, EMAIL_COOKIE);

        // TODO: add name and role to top right corner thingy

        return "/index";
    }


    //Controller for the login page that sends the login info to the restAPI and then gets back the access token
    @GetMapping(path="/login")
    public String login(Model model,
                        HttpServletRequest webRequest,
                        HttpServletResponse webResponse) {
        Cookie[] userCookies = webRequest.getCookies();
        log.info("User is in login");
        if(userCookies == null){
            log.info("No Cookies were found");
            return "/login";
        }else {
            log.info("Found {} Cookies", userCookies.length);
            for(int i = 0; i < userCookies.length ; i++){
                if (userCookies[i].getName().equals(accessToken)){
                    log.info("Access token found: {}", userCookies[i].getValue());
                    String accessTokenValue = userCookies[i].getValue();
                    return "redirect:/";
                }
                log.info("Cookie {}:{}-> {}", i, userCookies[i].getName(), userCookies[i].getValue());
            }
            log.info("No access token was found");
            model.addAttribute("loginInfo", new LoginForm());
        }
        return "/login";
    }
    @GetMapping(path="/tryLogin")
    public String tryLogin(Model model,
        @RequestParam(required = false) String username,
        @RequestParam(required = false) String password,
        HttpServletRequest webRequest, HttpServletResponse webResponse) throws IOException, NullPointerException {

        final String loginURI = restful + "/login";
        log.info("username is {} and password is {}", username, password);

        Response response = queryRESTfulPOST("application/x-www-form-urlencoded",
                "username=" + username + "&password=" + password,
                loginURI,
                "Content-Type",
                "application/x-www-form-urlencoded");

        if (response.code() == 200){
            log.info("The server responded to the the login attempt with: {}", response);
            //Store the received token in a JSON object
            JSONObject token = new JSONObject(response.body().string());
            log.info("User token is: {}", token);

            //This username is actually the user's email.
            Response userInfo = queryUserInfo(username, token.getString("access_token"));
            log.info("User info is: {}", userInfo.body().string());

            JSONObject userJSON = new JSONObject(userInfo.body().string());
            log.info("user info  is: {}", userJSON);

            User user = new User(userJSON);

            Cookie userTokenCookie = new Cookie(accessToken, token.getString("access_token"));
            Cookie userName = new Cookie(userNameCookieName, user.getName()+" "+user.getSurname());
            Cookie userRole = new Cookie(userRoleCookieName, user.getRole().getName());

            userTokenCookie.setMaxAge(30/*minutes*/ * 60/*seconds*/);
            userName.setMaxAge(30/*minutes*/ * 60/*seconds*/);
            userRole.setMaxAge(30/*minutes*/ * 60/*seconds*/);
            webResponse.addCookie(userTokenCookie);
            webResponse.addCookie(userName);
            webResponse.addCookie(userRole);

            return "redirect:/";
        } else {
            return "redirect:/login";
        }
    }
    @GetMapping(path="/news")
    public String news(Model model, HttpServletRequest webRequest, HttpServletResponse webResponse,
            @RequestParam(required = false) Long topicID,
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy/MM/dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy/MM/dd") Date endDate) {
        log.info("User is in news");

        String userToken = getCookieValue(webRequest.getCookies(), accessToken);
        String userName  = getCookieValue(webRequest.getCookies(), userNameCookieName);
        String userRole  = getCookieValue(webRequest.getCookies(), userRoleCookieName);

        RestTemplate restTemplate = new RestTemplate();

        List<Topic> listTopics = new ArrayList<>();
        List<News> listNews = new ArrayList<>();

        final String topicsURI = restful + "/api/topics";
        final String newsURI;

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
        model.addAttribute("ListTopics", listTopics);
        model.addAttribute("newFilter", new Filter());

        return "/news";
    }
    //Controller that generates a news article and sends it to the restAPI for storage
    @GetMapping(path="/news_create")
    public String news_create(Model model, HttpServletRequest webRequest,
                              HttpServletResponse webResponse){
        //send POST to server to add news to /api/
        log.info("User is in news_create");
        String userToken = getCookieValue(webRequest.getCookies(), accessToken);
        String userName  = getCookieValue(webRequest.getCookies(), userNameCookieName);
        String userRole  = getCookieValue(webRequest.getCookies(), userRoleCookieName);


        model.addAttribute("newArticle", new News());

        return "/news_create";
    }
    //Controller that displays the chosen news from /news.html to the user
    @GetMapping(path="/news_viewer")
    public String news_viewer(Model model, HttpServletRequest webRequest,
                              HttpServletResponse webResponse) {
        //query GET to server for news
        log.info("User is in news_viewer");
        String userToken = getCookieValue(webRequest.getCookies(), accessToken);
        String userName  = getCookieValue(webRequest.getCookies(), userNameCookieName);
        String userRole  = getCookieValue(webRequest.getCookies(), userRoleCookieName);

        return "/news_viewer.html";
    }
    //Controller that gets the user info from the RestAPI and displays it to the user
    @GetMapping(path="/profile")
    public String profile(Model model, HttpServletRequest webRequest,
                          HttpServletResponse webResponse) throws JSONException{
        log.info("User is in profile");
        String userToken = getCookieValue(webRequest.getCookies(), accessToken);
        String userName  = getCookieValue(webRequest.getCookies(), userNameCookieName);
        String userRole  = getCookieValue(webRequest.getCookies(), userRoleCookieName);

        //query POST to server to get personal user info back
        final String profileURI = restful + "/users";

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", "val");
        parameters.put("password", "val");

        String response = restTemplate.postForObject(profileURI, "",String.class, parameters);

        log.info("User tokens: {}", response);

        JSONObject result = new JSONObject(response.substring(1,response.length()-1));

        return "redirect:/profile";
    }
    //Controller that sends a new user to be added to the User Database
    @GetMapping(path="/register.html")
    public String register(Model model, HttpServletRequest webRequest,
                           HttpServletResponse webResponse) {
        log.info("User is in register");
        //Does this work?


        model.addAttribute("registerInfo", new RegisterForm());
        return "redirect:/register";
    }
    @GetMapping("/registerInfo")
    public String registerInfo(Model model, HttpServletRequest webRequest, HttpServletResponse webResponse,
                               @RequestParam int role, @RequestParam String firstname,
                               @RequestParam String lastname, @RequestParam String email,
                               @RequestParam String password, @RequestParam String confirmedPassword
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



                    Cookie userTokenCookie = new Cookie(accessToken, token.getString("access_token"));
                    userTokenCookie.setMaxAge(10 * 60); //it's in seconds

                    return "redirect:/";
                }
            }
        }
        return "redirect:/register";
    }
    //Controller that displays all topics from the RestAPI
    @GetMapping(path="/topics")
    public String topics(Model model,
                         HttpServletRequest webRequest,
                         HttpServletResponse webResponse) throws IOException{
        log.info("User is in topics");
        String userToken = getCookieValue(webRequest.getCookies(), accessToken);
        String userName  = getCookieValue(webRequest.getCookies(), userNameCookieName);
        String userRole  = getCookieValue(webRequest.getCookies(), userRoleCookieName);

        //query a GET to the server
        final String topicsURI = restful + "/api/topics";

        List<Topic> listTopics = new ArrayList<>();

        Response topics = queryRestfulGETwithToken(topicsURI, userToken);


        assert topics.body() != null;
        JSONArray topicsJSON = new JSONArray(topics.body().string());

        for (var object : topicsJSON) {
            Topic topic = new Topic((JSONObject) object);
            listTopics.add(topic);
        }

        log.info("Recieves topics are: {}", listTopics);

        //model.addAttribute("Insert thymeleaf object name here", listTopics);

        return "/topics";
    }
}