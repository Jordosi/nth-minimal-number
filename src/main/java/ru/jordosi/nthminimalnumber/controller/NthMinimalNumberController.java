package ru.jordosi.nthminimalnumber.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.jordosi.nthminimalnumber.dto.FindNumberRequest;
import ru.jordosi.nthminimalnumber.service.ExcelService;
import ru.jordosi.nthminimalnumber.service.QuickSelectService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name="N-th Minimal Number Controller", description = "API for searching Nth minimal number in XLSX files")
public class NthMinimalNumberController {
    private final ExcelService excelService;
    private final QuickSelectService quickSelectService;

    public NthMinimalNumberController(ExcelService excelService, QuickSelectService quickSelectService) {
        this.excelService = excelService;
        this.quickSelectService = quickSelectService;
    }

    @GetMapping("/find-k-min")
    @Operation(summary="Find N-th minimal number", description="Accepts a path to the local XLSX file" +
            "and N number, returns N-th minimal number and stats (N, total amount of numbers)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful search"),
            @ApiResponse(responseCode = "400", description = "Wrong request parameters or file does not contain proper data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getNthMinimalNumber(@RequestBody FindNumberRequest request) {
        try {
            if (request.getN() < 1) {
                return ResponseEntity.badRequest().body("N must be greater than or equal to 1");
            }

            Integer[] numbers = excelService.findNumbersFromExcel(request.getPath());

            if (request.getN() > numbers.length) {
                return ResponseEntity.badRequest().body("N must be less than or equal to numbers amount");
            }

            Integer result = quickSelectService.findNthMinimalNumber(numbers, request.getN());
            Map<String, Object> response = new HashMap<>();
            response.put("n", request.getN());
            response.put("result", result);
            response.put("totalNumbers", numbers.length);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Input error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Argument recognition error: " + e.getMessage());
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }
}
