package org.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface TestRepository extends JpaRepository<TestEntity, Integer> {
    Optional<TestEntity> findByColumn1AndColumn2(String column1, String column2);

    // This method will fetch all records from the database.
    List<TestEntity> findAll();
}
