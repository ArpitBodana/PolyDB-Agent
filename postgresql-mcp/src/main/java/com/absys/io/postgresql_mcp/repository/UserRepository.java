package com.absys.io.postgresql_mcp.repository;

import com.absys.io.postgresql_mcp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByCity(String city);

    List<User> findByAgeGreaterThan(Integer age);

    List<User> findByAgeLessThan(Integer age);
}
