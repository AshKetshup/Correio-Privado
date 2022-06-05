package com.cp.correioprivadosite;


import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.stream.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


@Controller @Slf4j
public class MainController{

    private String restful = "http://localhost:8081";

    @GetMapping(path="/index")
    public String index(Model model){

        //no honest idea what this needs from the server


        return null;
    }

    @GetMapping(path="/login")
    public String login(Model model) throws IOException  {

        final String loginURI = restful+"/login";

        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.postForObject(loginURI," ", String.class);

        //send POST to /login to receive back verification tokens
        URL restfulURL = new URL(restful);

        HttpURLConnection connection = (HttpURLConnection) restfulURL.openConnection();
        connection.setRequestMethod("POST");

        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", "val");
        parameters.put("password", "val");




        //store tokens "somewhere"



        return null;
    }
    @GetMapping(path="/news_create.html")
    public void news_create(Model model){
        //send POST to server to add news to /api/


    }

    @GetMapping(path="/news_viewer.html")
    public String news_viewer(Model model) throws IOException {
        //query GET to server for news

        final String newsURI = restful+"/api/news";

        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.getForObject(newsURI, String.class);

        

        //final JsonParser jaaaaason = Json.createParser(new StringReader(response));

        //log.info("News received is: {}", jaaaaason);

        //model.addAttribute("news", content);

        return null;
    }

    @GetMapping(path="/profile.html")
    public String profile(Model model){

        //query POST to server to get personal user info back


        return null;
    }

    @GetMapping(path="/register.html")
    public String register(Model model){

        //generate a POST to the server

        return null;
    }

    @GetMapping(path="/topics.html")
    public String topics(Model model){

        //query a GET to the server

        return null;
    }



}
