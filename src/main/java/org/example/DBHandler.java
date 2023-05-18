package org.example;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
class TestService {

    @Autowired
    private TestRepository testRepository;

    public void insertJsonObject(JSONObject jsonObject) {
        String column1 = jsonObject.getString("column1");
        String column2 = jsonObject.getString("column2");

        // Check for existing data
        Optional<TestEntity> existingData = testRepository.findByColumn1AndColumn2(column1, column2);

        if (existingData.isEmpty()) {
            TestEntity testEntity = new TestEntity();
            testEntity.setColumn1(column1);
            testEntity.setColumn2(column2);
            testRepository.save(testEntity);
        }
    }

    // This method fetches all records from the database.
    public List<TestEntity> getAll() {
        return testRepository.findAll();
    }
}