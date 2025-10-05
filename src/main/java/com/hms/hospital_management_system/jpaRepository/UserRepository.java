package com.hms.hospital_management_system.jpaRepository;

import com.hms.hospital_management_system.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

     // Spring Data JPA automatically derives the query from the method name
    Optional<User> findByUsername(String username);
}
