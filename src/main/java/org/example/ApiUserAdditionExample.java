package org.example;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.UUID;

public class ApiUserAdditionExample {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            ApiUserAdditionService apiUserAdditionService = context.getBean(ApiUserAdditionService.class);

            String username = "test";
            String apiKey = UUID.randomUUID().toString();  // Generate a unique API key
            apiUserAdditionService.addApiUser(username, apiKey);  // No need to pass ID

            System.out.println("New API user added successfully!");
        }
    }
}
