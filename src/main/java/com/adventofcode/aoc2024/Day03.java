package com.adventofcode.aoc2024;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 {
    @With
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ArgumentsPair {
        Integer left;
        Integer right;
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        new Day03().solve();
    }

    private void solve() throws IOException, URISyntaxException {
        String fileName = "input/day03.txt";
        Path filePath = Path.of(
                Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).toURI()
        );
        String content = Files.readString(filePath);

        // Parse valid operations
        List<ArgumentsPair> pairs = parseValidOperationsSwitchable(content);

        // Calculate and print the sum of multiplication of pairs
        int sumOfProducts = pairs.stream()
                .mapToInt(pair -> pair.left * pair.right)
                .sum();

        System.out.println("Sum of multiplication of pairs: " + sumOfProducts);
    }

    private List<ArgumentsPair> parseValidOperations(String content) {
        // Regex to match mul(x, y) where x and y are 1-3 digit integers
        Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
        Matcher matcher = pattern.matcher(content);

        // Collect all valid matches into a list of ArgumentsPair
        List<ArgumentsPair> pairs = new ArrayList<>();
        while (matcher.find()) {
            Integer left = Integer.parseInt(matcher.group(1));
            Integer right = Integer.parseInt(matcher.group(2));
            pairs.add(new ArgumentsPair(left, right));
        }

        return pairs;
    }

    private List<ArgumentsPair> parseValidOperationsSwitchable(String content) {
        // Regex pattern to match mul(x, y), do(), and don't()
        Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)|do\\(\\)|don't\\(\\)");
        Matcher matcher = pattern.matcher(content);

        List<ArgumentsPair> pairs = new ArrayList<>();
        boolean isMulEnabled = true; // Initial state: mul is enabled

        while (matcher.find()) {
            String match = matcher.group();

            if (match.equals("do()")) {
                isMulEnabled = true; // Enable mul operations
            } else if (match.equals("don't()")) {
                isMulEnabled = false; // Disable mul operations
            } else if (isMulEnabled && match.startsWith("mul")) {
                // If mul is enabled, extract the numbers and add to pairs
                int left = Integer.parseInt(matcher.group(1));
                int right = Integer.parseInt(matcher.group(2));
                pairs.add(new ArgumentsPair(left, right));
            }
        }

        return pairs;
    }
}
