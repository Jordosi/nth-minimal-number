package ru.jordosi.nthminimalnumber.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FindNumberRequestTest {

    @Test
    void testDefaultConstructor() {
        FindNumberRequest request = new FindNumberRequest();

        assertNull(request.getPath());
        assertNull(request.getN());
    }

    @Test
    void testParameterizedConstructor() {
        String expectedPath = "/test/file.xlsx";
        Integer expectedN = 5;

        FindNumberRequest request = new FindNumberRequest(expectedPath, expectedN);

        assertEquals(expectedPath, request.getPath());
        assertEquals(expectedN, request.getN());
    }

    @Test
    void testSettersAndGetters() {
        FindNumberRequest request = new FindNumberRequest();

        String path = "/test/path.xlsx";
        Integer n = 10;

        request.setPath(path);
        request.setN(n);

        assertEquals(path, request.getPath());
        assertEquals(n, request.getN());
    }

    @Test
    void testNullHandling() {
        FindNumberRequest request = new FindNumberRequest();

        request.setPath(null);
        request.setN(null);

        assertNull(request.getPath());
        assertNull(request.getN());
    }
}