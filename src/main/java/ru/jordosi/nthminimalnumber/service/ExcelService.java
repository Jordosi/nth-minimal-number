package ru.jordosi.nthminimalnumber.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Service for reading integer data from Excel files.
 * <p>
 * Expects Excel files with a single column containing integer values in the first sheet.
 * Empty cells and non-numeric values are automatically skipped during processing.
 * </p>
 * @see ru.jordosi.nthminimalnumber.controller.NthMinimalNumberController
 */
@Service
public class ExcelService {
    /**
     * Reads integer values from the first column of the first sheet in an Excel file.
     * <p>
     * The method processes only numeric cells in the first column, skipping any empty cells
     * or cells with non-numeric values. Decimal values are truncated to integers.
     * </p>
     *
     * @param path absolute path to the .xlsx file to read
     * @return array of integers found in the first column of the Excel file
     * @throws IOException if any I/O error occurs during file reading
     * @throws IllegalStateException if the file is empty or contains no numeric data
     *
     * @apiNote Supported Excel format: .xlsx (Excel 2007 and later)
     * @implNote Uses streaming approach for memory-efficient processing of large files
     */
    public Integer[] findNumbersFromExcel(String path) throws IOException {
        validateInput(path);
        List<Integer> numbers = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(path);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell cell = row.getCell(0);

                if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                    numbers.add((int) cell.getNumericCellValue());
                }
            }
        }

        if (numbers.isEmpty()) {
            throw new IOException("No numeric data found in the first column of the Excel file");
        }

        return numbers.toArray(new Integer[0]);
    }

    /**
     * File path validator
     * @param path input file absolute path
     */
    private void validateInput(String path) {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }

        if (!path.toLowerCase().endsWith(".xlsx")) {
            throw new IllegalArgumentException("Only .xlsx files are supported");
        }
    }
}
