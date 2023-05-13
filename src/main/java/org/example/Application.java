package org.example;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;
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
    public String receiveJson(@RequestBody List<Map<String, Object>> jsonArray) {
        for (Map<String, Object> jsonObjectMap : jsonArray) {
            JSONObject jsonObject = new JSONObject(jsonObjectMap);
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
