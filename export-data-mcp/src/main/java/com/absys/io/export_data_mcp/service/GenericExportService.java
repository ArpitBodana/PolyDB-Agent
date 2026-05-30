package com.absys.io.export_data_mcp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenericExportService {

    private final CsvExportService csvService;
    private final ExcelExportService excelService;
    private final PdfExportService pdfService;

    public String export(String format, String fileName, List<?> data) {

        return switch (format.toLowerCase()) {

            case "csv" -> csvService.export(fileName, data);

            case "excel", "xlsx" -> excelService.export(fileName, data);

            case "pdf" -> pdfService.export(fileName, data);

            default -> throw new IllegalArgumentException(
                    "Unsupported format: " + format
            );
        };
    }
}