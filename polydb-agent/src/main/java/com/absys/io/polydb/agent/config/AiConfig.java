package com.absys.io.polydb.agent.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.Arrays;

@Configuration
public class AiConfig {

    @Value("classpath:prompts/system.st")
    private Resource systemPrompt;

    @Bean
    public ChatClient ollamaChatClient(ChatClient.Builder builder, ToolCallbackProvider toolCallbackProvider) {
        return builder
                .defaultSystem(systemPrompt)
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @Bean
    public ApplicationRunner logMcpTools(ToolCallbackProvider toolCallbackProvider) {
        return args -> {

            System.out.println("\n===== MCP TOOLS LOADED =====");

            Arrays.stream(toolCallbackProvider.getToolCallbacks()).forEach(tool ->
                    System.out.println("✔ Tool: " + tool.getToolDefinition().name()));


        };
    }
}
