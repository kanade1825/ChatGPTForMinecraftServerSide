package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
            testService.insertJsonObject(jsonObject);
        }
        return "SUCCESS";
    }

    @Async
    public void insertJsonAsync(JSONObject jsonObject) {
        testService.insertJsonObject(jsonObject);
    }

    @GetMapping("/json")
    public String sendJson() {
        // Fetch all records from the database.
        List<TestEntity> allEntities = testService.getAll();

        // Convert the list of entities to JSON string.
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "";

        try {
            jsonString = objectMapper.writeValueAsString(allEntities);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonString;
    }
}


