package com.absys.io.export_data_mcp.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Service
public class CsvExportService {

    public String export(String fileName, List<?> data) {

        try {

            if (data == null || data.isEmpty()) {
                throw new RuntimeException("No data to export");
            }

            new File("exports").mkdirs();
            String fullName = "exports/" + fileName + ".csv";

            Object firstRow = data.get(0);
            List<Field> fields = Arrays.asList(firstRow.getClass().getDeclaredFields());

            List<String> headers = fields.stream()
                    .map(Field::getName)
                    .toList();

            try (FileWriter writer = new FileWriter(fullName);
                 CSVPrinter csv = new CSVPrinter(
                         writer,
                         CSVFormat.DEFAULT.withHeader(headers.toArray(new String[0]))
                 )) {

                for (Object row : data) {

                    List<Object> values = fields.stream()
                            .map(field -> {
                                try {
                                    field.setAccessible(true);
                                    return field.get(row);
                                } catch (Exception e) {
                                    return null;
                                }
                            })
                            .toList();

                    csv.printRecord(values);
                }
            }

            return fullName;

        } catch (Exception e) {
            throw new RuntimeException("CSV export failed", e);
        }
    }
}