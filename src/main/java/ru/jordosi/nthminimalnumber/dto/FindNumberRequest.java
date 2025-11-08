package ru.jordosi.nthminimalnumber.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** Request body
 * <p>
 * Contains path to the local .xlsx file and value N representing
 * the position of the minimum number to find (e.g., 1st minimum, 2nd minimum, etc.).
 * </p>
 *
 */
@Schema(description = "Request for N-th minimal number search")
public class FindNumberRequest {
    /**
     * Absolute path to the Excel file containing numbers in a single column
     */
    @Schema(description = "Absolute path to the .xlsx file", example = "C:/data/numbers.xlsx", requiredMode = Schema.RequiredMode.REQUIRED)
    private String path;
    /**
     * The position of the minimum number to find (1-based index)
     * <p>Example: n=1 returns the smallest number, n=2 returns the second smallest, etc.
     * </p>
     */
    @Schema(description = "Order number of minimal number (1 - less)", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer n;

    public FindNumberRequest() {}
    public FindNumberRequest(String path, Integer n) {
        this.path = path;
        this.n = n;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public Integer getN() {
        return n;
    }
    public void setN(Integer n) {
        this.n = n;
    }
}
