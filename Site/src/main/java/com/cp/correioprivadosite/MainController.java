package com.cp.correioprivadosite;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller @Slf4j
public class MainController{
    private String restful = "http://localhost:8081";
    //Controller for the main page
    @GetMapping(path="/index.html")
    public String index(Model model){

        //no honest idea what this needs from the server


        return null;
    }
    //Controller for the login page that sends the login info to the restAPI and then gets back the access token
    @GetMapping(path="/login.html")
    public String login(Model model) throws IOException  {

        final String newsURI = restful+"/login";

        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", "val");
        parameters.put("password", "val");

        String response = restTemplate.postForObject(newsURI, "",String.class, parameters);

        log.info("User tokens: {}", response);

        try {
            JSONObject result = new JSONObject(response.substring(1,response.length()-1));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //store tokens "somewhere"

        return null;
    }
    //Controller for the main news page that receives all news from the restAPI
    @GetMapping(path="/news.html")
    public void news(Model model) {
        log.info("News Controller triggered");
        final String newsURI = restful + "/api/news";
        RestTemplate restTemplate = new RestTemplate();
        log.info("News Controller triggering /api/news from the RestAPI");

        String response = restTemplate.getForObject(newsURI, String.class);

        JSONObject news = new JSONObject(response.substring(1, response.length()-1));



        log.info("News received object is {}", news);

        model.addAttribute("ListArticles", news);
    }
    //Controller that generates a news article and sends it to the restAPI for storage
    @GetMapping(path="/news_create.html")
    public void news_create(Model model){
        //send POST to server to add news to /api/

    }
    //Controller that displays the chosen news from /news.html to the user
    @GetMapping(path="/news_viewer.html")
    public String news_viewer(Model model) throws IOException {
        //query GET to server for news

        final String newsURI = restful+"/api/news";

        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.getForObject(newsURI, String.class, "id to be defined");


        //final JsonParser jaaaaason = Json.createParser(new StringReader(response));

        //log.info("News received is: {}", jaaaaason);

        //model.addAttribute("news", content);

        return null;
    }
    //Controller that gets the user info from the RestAPI and displays it to the user
    @GetMapping(path="/profile.html")
    public String profile(Model model){

        //query POST to server to get personal user info back
        final String profileURI = restful + "/users";

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", "val");
        parameters.put("password", "val");

        String response = restTemplate.postForObject(profileURI, "",String.class, parameters);

        log.info("User tokens: {}", response);

        try {
            JSONObject result = new JSONObject(response.substring(1,response.length()-1));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    //Controller that sends a new user to be added to the User Database
    @GetMapping(path="/register.html")
    public String register(Model model){

        //generate a POST to the server
        final String registerURI = restful + "/users";

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", "val");
        parameters.put("password", "val");

        String response = restTemplate.postForObject(registerURI, "",String.class, parameters);

        log.info("User tokens: {}", response);

        try {
            JSONObject result = new JSONObject(response.substring(1,response.length()-1));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    //Controller that displays all topics from the RestAPI
    @GetMapping(path="/topics.html")
    public String topics(Model model){

        //query a GET to the server
        final String topicsURI = restful+"/api/topics";

        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.getForObject(topicsURI, String.class, "id to be defined");

        return null;
    }
}

