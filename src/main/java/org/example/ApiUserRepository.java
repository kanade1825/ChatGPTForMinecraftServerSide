package org.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface ApiUserRepository extends JpaRepository<ApiUser, Integer> {
    Optional<ApiUser> findByApiKey(String apiKey);
}

