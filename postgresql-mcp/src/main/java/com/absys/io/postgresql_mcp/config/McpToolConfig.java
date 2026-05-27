package com.absys.io.postgresql_mcp.config;

import com.absys.io.postgresql_mcp.tools.TaskTools;
import com.absys.io.postgresql_mcp.tools.UserTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpToolConfig {

    @Bean
    public ToolCallbackProvider postgresTools(UserTools userTools, TaskTools taskTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(userTools, taskTools)
                .build();
    }

    @Bean
    CommandLineRunner debug(ToolCallbackProvider provider) {
        return args -> System.out.println("TOOLS LOADED: " + provider.getToolCallbacks().length);
    }
}
