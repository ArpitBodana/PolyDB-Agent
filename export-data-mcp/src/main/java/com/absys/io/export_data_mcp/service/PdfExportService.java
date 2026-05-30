package com.absys.io.export_data_mcp.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Service
public class PdfExportService {

    public String export(String fileName, List<?> data) {

        try {
            if (data == null || data.isEmpty()) {
                throw new RuntimeException("No data");
            }

            new File("exports").mkdirs();
            String fullName = "exports/" + fileName + ".pdf";

            Object first = data.get(0);
            List<Field> fields = Arrays.asList(first.getClass().getDeclaredFields());

            PdfWriter writer = new PdfWriter(fullName);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            Table table = new Table(fields.size());

            // headers
            for (Field field : fields) {
                table.addHeaderCell(field.getName());
            }

            // rows
            for (Object row : data) {
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(row);
                    table.addCell(value != null ? value.toString() : "");
                }
            }

            document.add(table);
            document.close();

            return fullName;

        } catch (Exception e) {
            throw new RuntimeException("PDF export failed", e);
        }
    }
}