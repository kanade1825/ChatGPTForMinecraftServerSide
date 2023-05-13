package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;
import java.nio.file.*;
import java.io.*;

@SpringBootApplication
@EnableAsync
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

@RestController
class JsonController {

    @Autowired
    private TestService testService;

    @PostMapping("/json")
    public String receiveJson(@RequestBody JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            insertJsonAsync(jsonObject);
        }
        return "SUCCESS";
    }

    @Async
    public void insertJsonAsync(JSONObject jsonObject) {
        testService.insertJsonObject(jsonObject);
    }

    @GetMapping("/json")
    public String sendJson() {
        String fileName = "H:\\ChatGPTForMinecraftServerSide\\src\\main\\resources\\example.json";
        File file = new File(fileName);
        String jsonString = "";

        try {
            jsonString = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonString;
    }
}
