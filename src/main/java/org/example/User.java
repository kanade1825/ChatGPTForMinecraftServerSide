package org.example;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "test")
class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "column1")
    private String column1;

    @Column(name = "column2")
    private String column2;

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public String getColumn2() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }
}

@Repository
interface TestRepository extends JpaRepository<TestEntity, Integer> {
    Optional<TestEntity> findByColumn1AndColumn2(String column1, String column2);

    // This method will fetch all records from the database.
    List<TestEntity> findAll();
}

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    // Getters and Setters...
}


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
