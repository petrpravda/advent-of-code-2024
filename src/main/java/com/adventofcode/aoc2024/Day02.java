package com.adventofcode.aoc2024;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class Day02 {
    @Getter
    @AllArgsConstructor
    @ToString
    public static class LineInfo {
        private final List<Integer> line;
        private final boolean increasing;
        private final boolean decreasing;
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        new Day02().solve();
    }

    // Checks if the levels are strictly increasing
    public static final Predicate<List<Integer>> IS_INCREASING = list -> {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) >= list.get(i + 1)) {
                return false;
            }
        }
        return true;
    };

    // Checks if the levels are strictly decreasing
    public static final Predicate<List<Integer>> IS_DECREASING = list -> {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) <= list.get(i + 1)) {
                return false;
            }
        }
        return true;
    };

    // Ensures the difference between any two adjacent levels is within 1 and 3
    public static final Predicate<List<Integer>> DIFFERENCE_OK = list -> {
        for (int i = 0; i < list.size() - 1; i++) {
            int diff = Math.abs(list.get(i + 1) - list.get(i));
            if (diff < 1 || diff > 3) {
                return false;
            }
        }
        return true;
    };

    public static final Predicate<List<Integer>> IS_SAFE = list ->
            (IS_INCREASING.test(list) || IS_DECREASING.test(list)) && DIFFERENCE_OK.test(list);

    public static final Predicate<List<Integer>> IS_SAFE_WITH_ONE_REMOVAL = list -> {
        if (IS_SAFE.test(list)) {
            return true; // Already safe
        }

        // Use a stream to check if any sublist is safe after removing one level
        return java.util.stream.IntStream.range(0, list.size())
                .mapToObj(i -> {
                    // Create a new list excluding the element at index i
                    return IntStream.range(0, list.size())
                            .filter(j -> j != i)
                            .mapToObj(list::get)
                            .toList();
                })
                .anyMatch(IS_SAFE); // Check if any sublist is safe
    };

    private void solve() throws URISyntaxException, IOException {
        String fileName = "input/day02.txt";
        Path filePath = Path.of(
            Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).toURI()
        );

        String content = Files.readString(filePath);

        List<List<Integer>> lists = parseInputToListOLists(content);

        long count = lists.stream()
                .map(l -> new LineInfo(l, IS_INCREASING.test(l), IS_DECREASING.test(l)))
                //.filter(l -> IS_SAFE.test(l.getLine()))
                .filter(l -> IS_SAFE_WITH_ONE_REMOVAL.test(l.getLine()))
                .count();


        System.out.println(count);
    }

    public static List<List<Integer>> parseInputToListOLists(String content) {
        List<List<Integer>> result = new ArrayList<>();

        // Split the input string into lines
        String[] lines = content.split("\\R"); // \\R matches any line break (compatible with various OS)

        for (String line : lines) {
            List<Integer> currentList = new ArrayList<>();
            // Split each line by spaces
            String[] numbers = line.trim().split("\\s+");

            for (String number : numbers) {
                // Parse each number and add to the current list
                currentList.add(Integer.parseInt(number));
            }

            // Add the current list to the result
            result.add(currentList);
        }

        return result;
    }


}
