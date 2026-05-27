package com.absys.io.postgresql_mcp.service;

import com.absys.io.postgresql_mcp.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(User user);

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    Optional<User> getUserByEmail(String email);

    List<User> getUsersByCity(String city);

    User updateUser(Long id, User updatedUser);

    void deleteUser(Long id);
}