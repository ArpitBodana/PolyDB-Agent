package com.absys.io.export_data_mcp.config;


import com.absys.io.export_data_mcp.tools.GenericExportTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpToolConfig {

    @Bean
    public ToolCallbackProvider postgresTools(GenericExportTool genericExportTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(genericExportTool)
                .build();
    }

    @Bean
    CommandLineRunner debug(ToolCallbackProvider provider) {
        return args -> System.out.println("TOOLS LOADED: " + provider.getToolCallbacks().length);
    }
}
