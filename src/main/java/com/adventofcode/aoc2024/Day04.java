package com.adventofcode.aoc2024;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static com.adventofcode.aoc2024.Util.readResourceFile;

public class Day04 {
    @Getter
    @Setter
    @With
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Direction {
        Integer rowDelta;
        Integer colDelta;

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Direction direction = (Direction) o;
            return Objects.equals(rowDelta, direction.rowDelta) && Objects.equals(colDelta, direction.colDelta);
        }

        @Override
        public int hashCode() {
            return Objects.hash(rowDelta, colDelta);
        }
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        new Day04().solve();
    }

    private void solve() throws IOException, URISyntaxException {
        String content = readResourceFile("input/day04.txt");

        List<List<Character>> matrix = parseCharsMatrix(content);
        List<List<String>> vectors = prepareVectors(matrix);
        int count = countXmasSubstrings(vectors);
        System.out.println(count);
    }

    int countXmasSubstrings(List<List<String>> vectors) {
        return vectors.stream()
                .flatMap(List::stream) // Flatten nested lists
                .mapToInt(vector -> countOccurrences(vector, "XMAS")) // Count occurrences of "XMAS" in each string
                .sum();
    }

    List<List<String>> prepareVectors(List<List<Character>> matrix) {
        return List.of(
                generateStringsByDirection(matrix, new Direction(0, 1)),  // Horizontal
                generateStringsByDirection(matrix, new Direction(0, -1)), // Horizontal Reverse
                generateStringsByDirection(matrix, new Direction(1, 0)),  // Vertical
                generateStringsByDirection(matrix, new Direction(-1, 0)), // Vertical Reverse
                generateStringsByDirection(matrix, new Direction(1, 1)),  // Diagonal
                generateStringsByDirection(matrix, new Direction(-1, -1)), // Diagonal Reverse
                generateStringsByDirection(matrix, new Direction(1, -1)), // Antidiagonal
                generateStringsByDirection(matrix, new Direction(-1, 1))  // Antidiagonal Reverse
        );
    }

    List<List<Character>> parseCharsMatrix(String content) {
        // Trim extra newlines at the end of the input and trailing spaces from each line
        List<String> lines = content.lines()
                .map(String::stripTrailing) // Remove trailing spaces from each line
                .filter(line -> !line.isBlank()) // Remove entirely blank lines
                .toList();

        int size = lines.size();

        // Validate the matrix is square
        for (String line : lines) {
            if (line.length() != size) {
                throw new IllegalArgumentException("The input matrix is not square.");
            }
        }

        // Parse the matrix
        List<List<Character>> matrix = new ArrayList<>();
        for (String line : lines) {
            List<Character> row = new ArrayList<>();
            for (char ch : line.toCharArray()) {
                row.add(ch);
            }
            matrix.add(row);
        }

        return matrix;
    }

//    List<String> generateStringsByDirection(List<List<Character>> matrix, Direction direction) {
//        int size = matrix.size();
//        List<String> result = new ArrayList<>();
//
//        // Lambda to compute if a position is within bounds
//        BiPredicate<Integer, Integer> isValid = (row, col) -> row >= 0 && row < size && col >= 0 && col < size;
//
//        // Iterate over each possible starting point
//        for (int startRow = 0; startRow < size; startRow++) {
//            for (int startCol = 0; startCol < size; startCol++) {
//                StringBuilder sb = new StringBuilder();
//                int row = startRow, col = startCol;
//
//                // Build string along the given direction
//                while (isValid.test(row, col)) {
//                    sb.append(matrix.get(row).get(col));
//                    row += direction.getRowDelta();
//                    col += direction.getColDelta();
//                }
//
//                if (!sb.isEmpty()) {
//                    result.add(sb.toString());
//                }
//            }
//        }
//
//        // Reverse the result if direction is negative in either row or column
//        if (direction.getRowDelta() < 0 || direction.getColDelta() < 0) {
//            result = result.stream()
//                    .map(s -> new StringBuilder(s).reverse().toString())
//                    .collect(Collectors.toList());
//        }
//
//        return result;
//    }

    List<String> generateStringsByDirection(List<List<Character>> matrix, Direction direction) {
        int size = matrix.size();
        List<String> result = new ArrayList<>();

        if (direction.equals(new Direction(0, 1))) { // Horizontal
            for (List<Character> row : matrix) {
                result.add(row.stream().map(String::valueOf).collect(Collectors.joining()));
            }
        } else if (direction.equals(new Direction(0, -1))) { // Horizontal Reverse
            for (List<Character> row : matrix) {
                result.add(new StringBuilder(row.stream().map(String::valueOf).collect(Collectors.joining())).reverse().toString());
            }
        } else if (direction.equals(new Direction(1, 0))) { // Vertical
            for (int col = 0; col < size; col++) {
                StringBuilder sb = new StringBuilder();
                for (int row = 0; row < size; row++) {
                    sb.append(matrix.get(row).get(col));
                }
                result.add(sb.toString());
            }
        } else if (direction.equals(new Direction(-1, 0))) { // Vertical Reverse
            for (int col = 0; col < size; col++) {
                StringBuilder sb = new StringBuilder();
                for (int row = 0; row < size; row++) {
                    sb.append(matrix.get(row).get(col));
                }
                result.add(sb.reverse().toString());
            }
        } else if (direction.equals(new Direction(1, 1))) { // Diagonal
            for (int d = 0; d < size; d++) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i <= d; i++) {
                    sb.append(matrix.get(d - i).get(i));
                }
                result.add(sb.toString());
            }
            for (int d = 1; d < size; d++) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < size - d; i++) {
                    sb.append(matrix.get(size - 1 - i).get(d + i));
                }
                result.add(sb.toString());
            }
        } else if (direction.equals(new Direction(-1, -1))) { // Diagonal Reverse
            for (int d = 0; d < size; d++) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i <= d; i++) {
                    sb.append(matrix.get(d - i).get(i));
                }
                result.add(sb.reverse().toString());
            }
            for (int d = 1; d < size; d++) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < size - d; i++) {
                    sb.append(matrix.get(size - 1 - i).get(d + i));
                }
                result.add(sb.reverse().toString());
            }
        } else if (direction.equals(new Direction(1, -1))) { // Antidiagonal
            for (int d = 0; d < size; d++) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i <= d; i++) {
                    sb.append(matrix.get(i).get(d - i));
                }
                result.add(sb.toString());
            }
            for (int d = 1; d < size; d++) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < size - d; i++) {
                    sb.append(matrix.get(d + i).get(size - 1 - i));
                }
                result.add(sb.toString());
            }
        } else if (direction.equals(new Direction(-1, 1))) { // Antidiagonal Reverse
            for (int d = 0; d < size; d++) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i <= d; i++) {
                    sb.append(matrix.get(i).get(d - i));
                }
                result.add(sb.reverse().toString());
            }
            for (int d = 1; d < size; d++) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < size - d; i++) {
                    sb.append(matrix.get(d + i).get(size - 1 - i));
                }
                result.add(sb.reverse().toString());
            }
        }

        return result;
    }

    private int countOccurrences(String text, String substring) {
        int count = 0;
        int index = 0;

        while ((index = text.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length(); // Move index to avoid overlapping matches
        }

        return count;
    }
}
