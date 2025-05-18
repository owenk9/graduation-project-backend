package com.example.backend.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

@Service
public class ExcelExportService {
    public <T> byte[] exportToExcel(
            List<T> data,
            String[] headers,
            String sheetName,
            List<Function<T, Object>> fieldExtractors) throws Exception {

        // Tạo workbook Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        // Tạo header
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Định dạng ngày
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Điền dữ liệu
        for (int i = 0; i < data.size(); i++) {
            T item = data.get(i);
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < fieldExtractors.size(); j++) {
                Object value = fieldExtractors.get(j).apply(item);
                Cell cell = row.createCell(j);
                if (value instanceof LocalDateTime) {
                    cell.setCellValue(((LocalDateTime) value).format(formatter));
                } else if (value instanceof Number) {
                    cell.setCellValue(((Number) value).doubleValue());
                } else {
                    cell.setCellValue(value != null ? value.toString() : "-");
                }
            }
        }

        // Điều chỉnh độ rộng cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Ghi workbook vào byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
}
