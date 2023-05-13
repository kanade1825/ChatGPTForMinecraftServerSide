package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ApiUserAdditionService {

    @Autowired
    private ApiUserRepository apiUserRepository;

    public void addApiUser(String username, String apiKey) {
        ApiUser apiUser = new ApiUser();
        apiUser.setUsername(username);
        apiUser.setApiKey(apiKey);

        apiUserRepository.save(apiUser);
    }
}


