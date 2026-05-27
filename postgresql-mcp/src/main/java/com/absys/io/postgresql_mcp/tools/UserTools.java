package com.absys.io.postgresql_mcp.tools;

import com.absys.io.postgresql_mcp.entity.User;
import com.absys.io.postgresql_mcp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserTools {

    private final UserService userService;

    @Tool(
            name = "create_user",
            description = "Create a new user with name, email, age, and city"
    )
    public User createUser(User user) {

        log.info("[create_user] Request received: name={}, email={}, city={}",
                user.getName(), user.getEmail(), user.getCity());

        User created = userService.createUser(user);

        log.info("[create_user] User created successfully with id={}", created.getId());

        return created;
    }

    @Tool(
            name = "get_all_users",
            description = "Fetch all users from database"
    )
    public List<User> getAllUsers() {

        log.info("[get_all_users] Fetching all users");

        List<User> users = userService.getAllUsers();

        log.info("[get_all_users] Total users found: {}", users.size());

        return users;
    }

    @Tool(
            name = "get_user_by_id",
            description = "Get user by ID"
    )
    public User getUserById(Long id) {

        log.info("[get_user_by_id] Request for id={}", id);

        User user = userService.getUserById(id)
                .orElseThrow(() -> {
                    log.error("[get_user_by_id] User not found id={}", id);
                    return new RuntimeException("User not found");
                });

        log.info("[get_user_by_id] User found: name={}", user.getName());

        return user;
    }

    @Tool(
            name = "get_user_by_email",
            description = "Get user by email address"
    )
    public User getUserByEmail(String email) {

        log.info("[get_user_by_email] email={}", email);

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> {
                    log.error("[get_user_by_email] User not found email={}", email);
                    return new RuntimeException("User not found");
                });

        log.info("[get_user_by_email] User found: id={}", user.getId());

        return user;
    }

    @Tool(
            name = "get_users_by_city",
            description = "Get users filtered by city"
    )
    public List<User> getUsersByCity(String city) {

        log.info("[get_users_by_city] city={}", city);

        List<User> users = userService.getUsersByCity(city);

        log.info("[get_users_by_city] Found {} users", users.size());

        return users;
    }

    @Tool(
            name = "update_user",
            description = "Update user by ID"
    )
    public User updateUser(Long id, User updatedUser) {

        log.info("[update_user] Updating user id={}", id);

        User user = userService.updateUser(id, updatedUser);

        log.info("[update_user] User updated successfully id={}", user.getId());

        return user;
    }

    @Tool(
            name = "delete_user",
            description = "Delete user by ID"
    )
    public String deleteUser(Long id) {

        log.info("[delete_user] Deleting user id={}", id);

        userService.deleteUser(id);

        log.info("[delete_user] User deleted successfully id={}", id);

        return "User deleted successfully";
    }
}