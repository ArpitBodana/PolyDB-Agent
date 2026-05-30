package com.absys.io.export_data_mcp.tools;

import com.absys.io.export_data_mcp.dto.TaskDto;
import com.absys.io.export_data_mcp.dto.UserDto;
import com.absys.io.export_data_mcp.service.GenericExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GenericExportTool {

    private final GenericExportService exportService;

    @Tool(
            name = "export_task_data",
            description = "Export task data into csv, excel or pdf"
    )
    public String exportTaskData(
            String format,
            String fileName,
            List<TaskDto> data
    ) {

        return exportService.export(
                format,
                fileName,
                data
        );
    }


    @Tool(
            name = "export_user_data",
            description = "Export user data into csv, excel or pdf"
    )
    public String exportUserData(
            String format,
            String fileName,
            List<UserDto> data
    ) {

        return exportService.export(
                format,
                fileName,
                data
        );
    }

}