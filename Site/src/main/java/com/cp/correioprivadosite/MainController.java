package com.cp.correioprivadosite;

import com.cp.correioprivadosite.dados.*;
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
    private final String restful = "http://vmi827195.contaboserver.net:8080";
    private final String accessToken = "access_token";
    //Controller for the main page
    @GetMapping(path="/")
    public String index(Model model, HttpServletRequest webRequest, HttpServletResponse webResponse){
        //no honest idea what this needs from the server
        log.info("User is in index");
        Cookie[] userCookies = webRequest.getCookies();

        if(userCookies == null){
            log.info("No Cookies were found");
        }else {
            log.info("Found {} Cookies", userCookies.length);
            for(int i = 0; i < userCookies.length ; i++){
                if (userCookies[i].getName() == accessToken){
                    log.info("Access token found: {}", userCookies[i].getValue());
                    String accessTokenValue = userCookies[i].getValue();
                    break;
                }
            }

        }

        

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
                if (userCookies[i].getName() == accessToken){
                    log.info("Access token found: {}", userCookies[i].getValue());
                    String accessTokenValue = userCookies[i].getValue();
                    return "redirect:/";
                }
                log.info("Cookie {}: {} -> {}", i, userCookies[i].getName(), userCookies[i].getValue());
            }
            log.info("No access token was found");
            model.addAttribute("loginInfo", new LoginForm());
        }
        return "/login";
    }
    @GetMapping(path="/tryLogin")
    public String tryLogin(
        Model model,
        @RequestParam(required = false) String username,
        @RequestParam(required = false) String password,
        HttpServletRequest webRequest,
        HttpServletResponse webResponse) throws IOException, NullPointerException
    {
        final String loginURI = restful + "/login";
        log.info("username is {} and password is {}", username, password);

        //Generate HTTP Request to the API to send the credentials
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "username=" + username + "&password=" + password);
        Request request = new Request.Builder()
                .url(loginURI)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        //Receive the response to the the attemped login
        Response response = client.newCall(request).execute();
        if (response.code() == 200){
            log.info("The server responded to the the login attempt with: {}", response);
            //Store the received token in a JSON object
            String jsonToken = response.body().string();
            JSONObject token = new JSONObject(jsonToken);
            //Store the token in a cookie
            Cookie userTokenCookie = new Cookie(accessToken, token.getString("access_token"));
            userTokenCookie.setMaxAge(10 * 60); //it's in seconds
            webResponse.addCookie(userTokenCookie);
            return "redirect:/";
        } else {
            return "redirect:/login";
        }
    }
    @GetMapping(path="/news")
    public String news(
            Model model,
            @RequestParam(required = false) Long topicID,
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate) {
        log.info("User is in news");
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
    public String news_create(Model model){
        //send POST to server to add news to /api/
        log.info("User is in news_create");

        model.addAttribute("newArticle", new News());

        return "/news_create";
    }

    //Controller that displays the chosen news from /news.html to the user
    @GetMapping(path="/news_viewer")
    public String news_viewer(Model model) {
        //query GET to server for news
        log.info("User is in news_viewer");

        return "/news_viewer.html";
    }

    //Controller that gets the user info from the RestAPI and displays it to the user
    @GetMapping(path="/profile")
    public String profile(Model model) throws JSONException{
        log.info("User is in profile");
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
    public String register(Model model) {
        log.info("User is in register");
        //Does this work?


        model.addAttribute("registerInfo", new RegisterForm());
        return "redirect:/register";
    }

    @GetMapping("/registerInfo")
    public String registerInfo(Model model,
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

                OkHttpClient client = new OkHttpClient().newBuilder().build();
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                    "name=" + firstname +
                    "&surname" + lastname +
                    "&email=" + email +
                    "&password=" + password +
                    "&role=" + roleName
                );

                Request request = new Request.Builder()
                    .url(registerURI)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();

                //Receive the response to the the attemped login
                Response response = client.newCall(request).execute();

                // if the registration is successful then login the user
                if (response.code() == 200) {
                    log.info("The server responded to the register attempt with: {}", response);
                    final String loginURI = restful + "/login";

                    OkHttpClient clientLogin = new OkHttpClient().newBuilder().build();
                    MediaType mediaTypeLogin = MediaType.parse("application/x-www-form-urlencoded");
                    RequestBody bodyLogin = RequestBody.create(mediaTypeLogin, "username=" + email + "&password=" + password);
                    Request requestLogin = new Request.Builder()
                            .url(loginURI)
                            .method("POST", bodyLogin)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .build();

                    //Receive the response to the the attemped login
                    Response responseLogin = clientLogin.newCall(requestLogin).execute();
                    log.info("The server responded to the login attempt with: {}", responseLogin);

                    //Store the received token in a JSON object
                    String jsonToken = responseLogin.body().string();
                    JSONObject token = new JSONObject(jsonToken);
                    //Store the token in a cookie

                    Cookie userTokenCookie = new Cookie("accesstoken", token.getString("access_token"));
                    userTokenCookie.setMaxAge(10 * 60); //it's in seconds

                    return "redirect:/";
                }
            }
        }
        return "redirect:/register";
    }

    //Controller that displays all topics from the RestAPI
    @GetMapping(path="/topics")
    public String topics(Model model){
        log.info("User is in topics");
        RestTemplate restTemplate = new RestTemplate();
        //query a GET to the server
        final String topicsURI = restful + "/api/topics";

        List<Topic> listTopics = new ArrayList<>();
        String n = restTemplate.getForObject(topicsURI, String.class);
        JSONArray topics = new JSONArray(n);

        for (int i = 0; i < topics.length(); i++) {
            Topic topic = new Topic((JSONObject) topics.get(i));
            listTopics.add(topic);
        }

        return "redirect:/topics";
    }
}

