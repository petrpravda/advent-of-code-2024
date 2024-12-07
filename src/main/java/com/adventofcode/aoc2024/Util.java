package com.adventofcode.aoc2024;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Util {
    public static String readResourceFile(String fileName) throws URISyntaxException, IOException {
        Path filePath = Path.of(
                Objects.requireNonNull(Util.class.getClassLoader().getResource(fileName)).toURI()
        );
        return Files.readString(filePath);
    }

}
