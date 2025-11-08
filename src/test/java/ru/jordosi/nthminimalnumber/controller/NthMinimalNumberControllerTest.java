package ru.jordosi.nthminimalnumber.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.jordosi.nthminimalnumber.service.ExcelService;
import ru.jordosi.nthminimalnumber.service.QuickSelectService;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NthMinimalNumberController.class)
class NthMinimalNumberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExcelService excelService;

    @MockBean
    private QuickSelectService quickSelectService;

    @Test
    void testFindNthMinimum_Success() throws Exception {
        // Given
        Integer[] mockNumbers = {10, 20, 30, 40, 50};
        when(excelService.findNumbersFromExcel(anyString())).thenReturn(mockNumbers);
        when(quickSelectService.findNthMinimalNumber(mockNumbers, 3)).thenReturn(30);

        String requestBody = """
            {
                "path": "/test/file.xlsx",
                "n": 3
            }
            """;

        // When & Then
        mockMvc.perform(get("/find-nth-min")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.n").value(3))
                .andExpect(jsonPath("$.result").value(30))
                .andExpect(jsonPath("$.totalNumbers").value(5));

        verify(excelService).findNumbersFromExcel("/test/file.xlsx");
        verify(quickSelectService).findNthMinimalNumber(mockNumbers, 3);
    }

    @Test
    void testFindNthMinimum_FileNotFound() throws Exception {
        // Given
        when(excelService.findNumbersFromExcel(anyString()))
                .thenThrow(new IOException("File not found"));

        String requestBody = """
            {
                "path": "/nonexistent/file.xlsx",
                "n": 1
            }
            """;

        // When & Then
        mockMvc.perform(get("/find-nth-min")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("File not found")));
    }

    @Test
    void testFindNthMinimum_EmptyFile() throws Exception {
        // Given
        when(excelService.findNumbersFromExcel(anyString()))
                .thenThrow(new IOException("No numeric data found"));

        String requestBody = """
            {
                "path": "/empty/file.xlsx",
                "n": 1
            }
            """;

        // When & Then
        mockMvc.perform(get("/find-nth-min")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("No numeric data")));
    }

    @Test
    void testFindNthMinimum_InvalidN() throws Exception {
        // Given
        Integer[] mockNumbers = {10, 20, 30};
        when(excelService.findNumbersFromExcel(anyString())).thenReturn(mockNumbers);
        when(quickSelectService.findNthMinimalNumber(mockNumbers, 5))
                .thenThrow(new IllegalArgumentException("N must be in range from 1 to 3"));

        String requestBody = """
            {
                "path": "/test/file.xlsx",
                "n": 5
            }
            """;

        // When & Then
        mockMvc.perform(get("/find-nth-min")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("N must be less than or equal")));
    }

    @Test
    void testFindNthMinimum_EmptyPath() throws Exception {
        when(excelService.findNumbersFromExcel("")).thenThrow(new IllegalArgumentException("File path cannot be null or empty"));
        String requestBody = """
            {
                "path": "",
                "n": 1
            }
            """;

        mockMvc.perform(get("/find-nth-min")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFindNthMinimum_NullPath() throws Exception {
        when(excelService.findNumbersFromExcel(null)).thenThrow(new IllegalArgumentException("File path cannot be null or empty"));

        String requestBody = """
            {
                "n": 1
            }
            """;

        mockMvc.perform(get("/find-nth-min")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFindNthMinimum_InvalidNValue() throws Exception {
        String requestBody = """
            {
                "path": "/test/file.xlsx",
                "n": 0
            }
            """;

        mockMvc.perform(get("/find-nth-min")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFindNthMinimum_InternalServerError() throws Exception {
        // Given
        when(excelService.findNumbersFromExcel(anyString()))
                .thenThrow(new RuntimeException("Unexpected error"));

        String requestBody = """
            {
                "path": "/test/file.xlsx",
                "n": 1
            }
            """;

        // When & Then
        mockMvc.perform(get("/find-nth-min")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is5xxServerError());
    }
}
