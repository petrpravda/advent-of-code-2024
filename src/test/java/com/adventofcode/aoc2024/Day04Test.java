package com.adventofcode.aoc2024;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.adventofcode.aoc2024.Util.readResourceFile;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day04Test {
    @Test
    void testSolvePart1() throws URISyntaxException, IOException {
        String content = readResourceFile("input/day04-test.txt");

        Day04 solver = new Day04();
        List<List<Character>> matrix = solver.parseCharsMatrix(content);
        List<List<String>> vectors = solver.prepareVectors(matrix);
//        for (List<String> vector : vectors) {
//            System.out.println(vector);
//        }
        int count = solver.countXmasSubstrings(vectors);
//
        assertEquals(18, count, "The count of XMAS substrings should match the expected value");
    }

    @Test
    void testSolvePart2() throws URISyntaxException, IOException {
        String content = readResourceFile("input/day04-testII.txt");

        Day04 solver = new Day04();
        List<List<Character>> matrix = solver.parseCharsMatrix(content);
        int count = solver.countXmasCrosses(matrix);
        //int count = solver.countXmasCrosses(solver.flipMatrixDiagonally(matrix));
//
        assertEquals(9, count);
    }
}
