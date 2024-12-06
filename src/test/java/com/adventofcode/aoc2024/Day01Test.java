package com.adventofcode.aoc2024;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Day01Test {
    @Test
    void readDay1TestFile() throws URISyntaxException, IOException {
        String fileName = "input/day01-test.txt";
        Path filePath = Path.of(
                Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).toURI()
        );

        String content = Files.readString(filePath);
        assertNotNull(content, "File content should not be null");

        System.out.println(content);
    }
}
