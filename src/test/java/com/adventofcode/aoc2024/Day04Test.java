package com.adventofcode.aoc2024;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.adventofcode.aoc2024.Util.readResourceFile;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day04Test {
    @Test
    void testSolve() throws URISyntaxException, IOException {
        String content = readResourceFile("input/day04-test.txt");

        Day04 solver = new Day04();
        List<List<Character>> matrix = solver.parseCharsMatrix(content);
        List<List<String>> vectors = solver.prepareVectors(matrix);
        for (List<String> vector : vectors) {
            System.out.println(vector);
        }
//        int count = solver.countXmasSubstrings(vectors);
//
//        assertEquals(18, count, "The count of XMAS substrings should match the expected value");
    }
}
