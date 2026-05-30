package com.absys.io.export_data_mcp.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Service
public class ExcelExportService {

    public String export(String fileName, List<?> data) {

        try {

            if (data == null || data.isEmpty()) {
                throw new RuntimeException("No data to export");
            }

            new File("exports").mkdirs();
            String fullName = "exports/" + fileName + ".xlsx";

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Data");

            Object firstRow = data.get(0);
            List<Field> fields = Arrays.asList(firstRow.getClass().getDeclaredFields());

            // ======================
            // HEADER ROW
            // ======================
            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < fields.size(); i++) {
                headerRow.createCell(i).setCellValue(fields.get(i).getName());
            }

            // ======================
            // DATA ROWS
            // ======================
            for (int i = 0; i < data.size(); i++) {

                Object obj = data.get(i);
                Row row = sheet.createRow(i + 1);

                for (int j = 0; j < fields.size(); j++) {

                    Field field = fields.get(j);
                    field.setAccessible(true);

                    Object value = field.get(obj);

                    row.createCell(j).setCellValue(
                            value != null ? value.toString() : ""
                    );
                }
            }

            try (FileOutputStream out = new FileOutputStream(fullName)) {
                workbook.write(out);
            }

            workbook.close();

            return fullName;

        } catch (Exception e) {
            throw new RuntimeException("Excel export failed", e);
        }
    }
}