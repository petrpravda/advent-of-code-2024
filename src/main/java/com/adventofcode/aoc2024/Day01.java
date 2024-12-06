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
import java.util.stream.IntStream;

public class Day01 {

    @Getter
    @Setter
    @With
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ListPair {
        List<Integer> left;
        List<Integer> right;
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        new Day01().solve();
    }

    private void solve() throws URISyntaxException, IOException {
        String fileName = "input/day01.txt";
        Path filePath = Path.of(
            Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).toURI()
        );

        String content = Files.readString(filePath);

        ListPair lists = parseInputToListPair(content);

        System.out.println(computeSimilarity(lists));
    }

    public static ListPair parseInputToListPair(String input) {
        List<Integer> leftColumn = new ArrayList<>();
        List<Integer> rightColumn = new ArrayList<>();

        try (Scanner scanner = new Scanner(input)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split("\\s+");
                    if (parts.length == 2) {
                        leftColumn.add(Integer.parseInt(parts[0]));
                        rightColumn.add(Integer.parseInt(parts[1]));
                    } else {
                        throw new IllegalArgumentException("Invalid input format: " + line);
                    }
                }
            }
        }

        return new ListPair(leftColumn, rightColumn);
    }

    public static int computeTotalDistance(ListPair pair) {
        List<Integer> sortedLeft = pair.getLeft().stream()
                .sorted()
                .toList();
        List<Integer> sortedRight = pair.getRight().stream()
                .sorted()
                .toList();

        return IntStream.range(0, sortedLeft.size())
                .map(i -> Math.abs(sortedLeft.get(i) - sortedRight.get(i)))
                .sum();
    }

    public static int computeSimilarity(ListPair pair) {
        Map<Integer, Integer> frequency = pair.getRight().stream()
            .collect(HashMap::new,
                (map, num) -> map.put(num, map.getOrDefault(num, 0) + 1),
                HashMap::putAll);

        return pair.getLeft().stream()
            .map(n -> frequency.getOrDefault(n, 0) * n)
            .reduce(0, Integer::sum);
    }
}
