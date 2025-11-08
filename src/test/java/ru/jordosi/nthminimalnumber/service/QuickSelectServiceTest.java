package ru.jordosi.nthminimalnumber.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuickSelectServiceTest {

    private QuickSelectService quickSelectService;

    @BeforeEach
    void setUp() {
        quickSelectService = new QuickSelectService();
    }

    @Test
    @DisplayName("Should find 1st minimum in sorted array")
    void testFindNthMinimum_FirstMinimum() {
        Integer[] array = {1, 2, 3, 4, 5};

        int result = quickSelectService.findNthMinimalNumber(array, 1);

        assertEquals(1, result);
    }

    @Test
    @DisplayName("Should find last minimum in sorted array")
    void testFindNthMinimum_LastMinimum() {
        Integer[] array = {1, 2, 3, 4, 5};

        int result = quickSelectService.findNthMinimalNumber(array, 5);

        assertEquals(5, result);
    }

    @Test
    @DisplayName("Should find middle minimum in unsorted array")
    void testFindNthMinimum_MiddleMinimum() {
        Integer[] array = {5, 3, 1, 4, 2};

        int result = quickSelectService.findNthMinimalNumber(array, 3);

        assertEquals(3, result);
    }

    @Test
    @DisplayName("Should handle array with duplicates")
    void testFindNthMinimum_WithDuplicates() {
        Integer[] array = {3, 2, 1, 2, 3, 1};

        int result = quickSelectService.findNthMinimalNumber(array, 4);

        assertEquals(2, result);
    }

    @Test
    @DisplayName("Should handle single element array")
    void testFindNthMinimum_SingleElement() {
        Integer[] array = {42};

        int result = quickSelectService.findNthMinimalNumber(array, 1);

        assertEquals(42, result);
    }

    @Test
    @DisplayName("Should throw exception for null array")
    void testFindNthMinimum_NullArray() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> quickSelectService.findNthMinimalNumber(null, 1));

        assertTrue(exception.getMessage().contains("cannot be null"));
    }

    @Test
    @DisplayName("Should throw exception for empty array")
    void testFindNthMinimum_EmptyArray() {
        Integer[] emptyArray = {};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> quickSelectService.findNthMinimalNumber(emptyArray, 1));

        assertTrue(exception.getMessage().contains("cannot be null") ||
                exception.getMessage().contains("empty"));
    }

    @Test
    @DisplayName("Should throw exception for N less than 1")
    void testFindNthMinimum_NLessThanOne() {
        Integer[] array = {1, 2, 3};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> quickSelectService.findNthMinimalNumber(array, 0));

        assertTrue(exception.getMessage().contains("in range from 1 to"));
    }

    @Test
    @DisplayName("Should throw exception for N greater than array length")
    void testFindNthMinimum_NGreaterThanLength() {
        Integer[] array = {1, 2, 3};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> quickSelectService.findNthMinimalNumber(array, 5));

        assertTrue(exception.getMessage().contains("in range from 1 to 3"));
    }

    @Test
    @DisplayName("Should handle large array")
    void testFindNthMinimum_LargeArray() {
        Integer[] largeArray = new Integer[1000];
        for (int i = 0; i < 1000; i++) {
            largeArray[i] = 1000 - i; // обратный порядок
        }

        int result = quickSelectService.findNthMinimalNumber(largeArray, 500);

        assertEquals(500, result);
    }

    @Test
    @DisplayName("Should handle negative numbers")
    void testFindNthMinimum_NegativeNumbers() {
        Integer[] array = {-5, -1, -3, -2, -4};

        int result = quickSelectService.findNthMinimalNumber(array, 3);

        assertEquals(-3, result);
    }

    @Test
    @DisplayName("Should handle mixed positive and negative numbers")
    void testFindNthMinimum_MixedNumbers() {
        Integer[] array = {-2, 3, -1, 0, 2, -3, 1};

        int result = quickSelectService.findNthMinimalNumber(array, 4);

        assertEquals(0, result);
    }
}
