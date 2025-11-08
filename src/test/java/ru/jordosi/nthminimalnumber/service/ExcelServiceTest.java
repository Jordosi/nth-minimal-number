package ru.jordosi.nthminimalnumber.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ExcelServiceTest {

    private ExcelService excelService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        excelService = new ExcelService();
    }

    @Test
    void testFindNumbersFromExcel_ValidFile() throws IOException {
        // Given
        File testFile = createTestExcelFile("test.xlsx", new Integer[]{10, 20, 30, 40, 50});

        // When
        Integer[] result = excelService.findNumbersFromExcel(testFile.getAbsolutePath());

        // Then
        assertNotNull(result);
        assertEquals(5, result.length);
        assertArrayEquals(new Integer[]{10, 20, 30, 40, 50}, result);
    }

    @Test
    void testFindNumbersFromExcel_EmptyFile() throws IOException {
        // Given
        File testFile = createTestExcelFile("empty.xlsx", new Integer[]{});

        // When & Then
        IOException exception = assertThrows(IOException.class,
                () -> excelService.findNumbersFromExcel(testFile.getAbsolutePath()));
        assertTrue(exception.getMessage().contains("No numeric data found"));
    }

    @Test
    void testFindNumbersFromExcel_WithMixedData() throws IOException {
        // Given - file with numbers, strings, and empty cells
        File testFile = createTestExcelFileWithMixedData();

        // When
        Integer[] result = excelService.findNumbersFromExcel(testFile.getAbsolutePath());

        // Then - only numeric values must be extracted
        assertNotNull(result);
        assertEquals(3, result.length);
        assertArrayEquals(new Integer[]{100, 200, 300}, result);
    }

    @Test
    void testFindNumbersFromExcel_FileNotFound() {
        // Given
        String nonExistentPath = "/nonexistent/file.xlsx";

        // When & Then
        IOException exception = assertThrows(IOException.class,
                () -> excelService.findNumbersFromExcel(nonExistentPath));
        assertTrue( exception.getMessage().contains("Системе не удается найти указанный путь") ||
                    exception.getMessage().contains("Не удается найти указанный файл"));
    }

    @Test
    void testFindNumbersFromExcel_LargeFile() throws IOException {
        // Given - file with 1000 numbers
        Integer[] largeData = new Integer[1000];
        for (int i = 0; i < 1000; i++) {
            largeData[i] = i + 1;
        }
        File testFile = createTestExcelFile("large.xlsx", largeData);

        // When
        Integer[] result = excelService.findNumbersFromExcel(testFile.getAbsolutePath());

        // Then
        assertNotNull(result);
        assertEquals(1000, result.length);
        assertEquals(1, result[0]);
        assertEquals(1000, result[999]);
    }

    @Test
    void testFindNumbersFromExcel_DecimalNumbers() throws IOException {
        // Given - file with decimals (must be cast to integers)
        File testFile = createTestExcelFileWithDecimals();

        // When
        Integer[] result = excelService.findNumbersFromExcel(testFile.getAbsolutePath());

        // Then - decimals must be cast to int
        assertNotNull(result);
        assertEquals(3, result.length);
        assertArrayEquals(new Integer[]{10, 20, 30}, result); // 10.5 -> 10, 20.9 -> 20
    }

    private File createTestExcelFile(String filename, Integer[] numbers) throws IOException {
        File file = tempDir.resolve(filename).toFile();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("TestSheet");

            for (int i = 0; i < numbers.length; i++) {
                Row row = sheet.createRow(i);
                row.createCell(0).setCellValue(numbers[i]);
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }

        return file;
    }

    private File createTestExcelFileWithMixedData() throws IOException {
        File file = tempDir.resolve("mixed.xlsx").toFile();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("MixedSheet");

            // Numeric values
            sheet.createRow(0).createCell(0).setCellValue(100);
            sheet.createRow(1).createCell(0).setCellValue(200);

            // Strings (must be skipped)
            sheet.createRow(2).createCell(0).setCellValue("text");
            sheet.createRow(3).createCell(0).setCellValue("123text");

            // Empty cell
            sheet.createRow(4).createCell(0); // пустая

            // Another one empty cell
            sheet.createRow(5).createCell(0).setCellValue(300);

            // Boolean (must be skipped)
            sheet.createRow(6).createCell(0).setCellValue(true);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }

        return file;
    }

    private File createTestExcelFileWithDecimals() throws IOException {
        File file = tempDir.resolve("decimals.xlsx").toFile();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("DecimalSheet");

            sheet.createRow(0).createCell(0).setCellValue(10.5);
            sheet.createRow(1).createCell(0).setCellValue(20.9);
            sheet.createRow(2).createCell(0).setCellValue(30.1);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }

        return file;
    }
}
