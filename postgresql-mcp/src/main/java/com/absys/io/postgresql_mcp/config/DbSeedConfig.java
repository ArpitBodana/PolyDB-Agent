package com.absys.io.postgresql_mcp.config;

import com.absys.io.postgresql_mcp.entity.Task;
import com.absys.io.postgresql_mcp.entity.User;
import com.absys.io.postgresql_mcp.repository.TaskRepository;
import com.absys.io.postgresql_mcp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DbSeedConfig {

    public final UserRepository userRepository;
    public final TaskRepository taskRepository;


    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {

            if (userRepository.findAll().isEmpty()) {
                log.info("Seeding users to db ...........");
                User arpit = User.builder()
                        .name("Arpit Bodana")
                        .age(26)
                        .city("Sonaktach")
                        .email("arpitbodana@gmail.com")
                        .build();

                User chiku = User.builder()
                        .email("chiku@gmail.com")
                        .city("Dewas")
                        .name("Chiku Bodana")
                        .age(27)
                        .build();
                userRepository.saveAll(List.of(chiku, arpit));
                log.info("Seeded users to db ...........");
            }

            if (taskRepository.findAll().isEmpty()) {
                log.info("Seeding tasks to db ...........");
                Task aTask = Task.builder()
                        .title("Complete the postgresql tools for  polydb agent project  ASAP.")
                        .priority("High")
                        .description("Implement all changes for postgresql tools which are required by agent.")
                        .assignedTo("Arpit Bodana")
                        .status("Started")
                        .build();

                Task cTask = Task.builder()
                        .title("Complete the mongodb tools for  polydb agent project  ASAP.")
                        .priority("High")
                        .description("Implement all changes for mongodb tools which are required by agent.")
                        .assignedTo("Chiku Bodana")
                        .status("Pending")
                        .build();
                taskRepository.saveAll(List.of(aTask, cTask));
                log.info("Seeded tasks to db ...........");
            }

        };
    }
}
