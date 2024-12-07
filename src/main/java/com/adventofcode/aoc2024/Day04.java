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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
        Boolean flipRows;
        Boolean flipCols;
        Boolean flipAxis;

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
//        List<List<String>> vectors = prepareVectors(matrix);
//        int count = countXmasSubstrings(vectors);
//        System.out.println(count);

        System.out.println(countXmasCrosses(matrix));
    }

    int countXmasSubstrings(List<List<String>> vectors) {
        return vectors.stream()
                .flatMap(List::stream) // Flatten nested lists
                .mapToInt(vector -> countOccurrences(vector, "XMAS")) // Count occurrences of "XMAS" in each string
                .sum();
    }

    List<List<String>> prepareVectors(List<List<Character>> matrix) {
        return List.of(
                generateStringsByDirection(matrix, new Direction(0, 1, false, false, false)),  // Horizontal
                generateStringsByDirection(matrix, new Direction(0, -1, false, true, false)), // Horizontal Reverse
                generateStringsByDirection(matrix, new Direction(1, 0, false, false, true)),  // Vertical
                generateStringsByDirection(matrix, new Direction(-1, 0, true, false, true)), // Vertical Reverse
                generateStringsByDirection(matrix, new Direction(1, 1, false, false, false)),  // Diagonal
                generateStringsByDirection(matrix, new Direction(-1, -1, true, false, false)), // Diagonal Reverse
                generateStringsByDirection(matrix, new Direction(1, -1, false, true, false)), // Antidiagonal
                generateStringsByDirection(matrix, new Direction(-1, 1, true, true, false))  // Antidiagonal Reverse
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

    List<String> generateStringsByDirection(List<List<Character>> matrix, Direction direction) {
        int size = matrix.size();
        List<List<Character>> transformedMatrix = new ArrayList<>(matrix);

        // Flip the matrix rows, columns, and axis based on the direction flags
        if (direction.flipRows != null && direction.flipRows) {
            transformedMatrix = flipMatrixRows(transformedMatrix);
        }
        if (direction.flipCols != null && direction.flipCols) {
            transformedMatrix = flipMatrixColumns(transformedMatrix);
        }
        if (direction.flipAxis != null && direction.flipAxis) {
            transformedMatrix = flipMatrixDiagonally(transformedMatrix);
        }

        List<String> result = new ArrayList<>();


        if (direction.rowDelta == 0 || direction.colDelta == 0) { // Horizontal or Vertical
            for (List<Character> row : transformedMatrix) {
                result.add(row.stream().map(String::valueOf).collect(Collectors.joining()));
            }
        } else {
            for (int d = 0; d < size; d++) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i <= d; i++) {
                    sb.append(transformedMatrix.get(d - i).get(i));
                }
                result.add(sb.toString());
            }
            for (int d = 1; d < size; d++) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < size - d; i++) {
                    sb.append(transformedMatrix.get(size - 1 - i).get(d + i));
                }
                result.add(sb.toString());
            }
        }

        return result;
    }

    List<List<Character>> flipMatrixRows(List<List<Character>> matrix) {
        List<List<Character>> flipped = new ArrayList<>(matrix);
        Collections.reverse(flipped);
        return flipped;
    }

    // Helper function to flip columns of the matrix
    List<List<Character>> flipMatrixColumns(List<List<Character>> matrix) {
        List<List<Character>> flipped = new ArrayList<>();
        for (List<Character> row : matrix) {
            List<Character> reversedRow = new ArrayList<>(row);
            Collections.reverse(reversedRow);
            flipped.add(reversedRow);
        }
        return flipped;
    }

    List<List<Character>> flipMatrixDiagonally(List<List<Character>> matrix) {
        int size = matrix.size();
        List<List<Character>> flipped = new ArrayList<>(size);

        // Create new lists for each row in the new flipped matrix
        for (int i = 0; i < size; i++) {
            flipped.add(new ArrayList<>(size));
        }

        // Perform the flip by swapping (i, j) and (j, i)
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                flipped.get(j).add(matrix.get(i).get(j));
            }
        }

        return flipped;
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

    public int countXmasCrosses(List<List<Character>> matrix) {
        int count = 0;
        int size = matrix.size();

        // Iterate through each cell of the matrix, considering it as a potential center of an X
        for (int i = 1; i < size - 1; i++) {
            for (int j = 1; j < size - 1; j++) {
                // Check for X pattern starting at (i, j)
                if (isXMasCross(matrix, i, j)) {
                    count++;
                }
            }
        }

        return count;
    }

    private boolean isXMasCross(List<List<Character>> matrix, int row, int col) {
        // Check for the "MAS" in the X-shape, we need to check in both diagonal directions
        if (matrix.get(row).get(col) != 'A') {
            return false;
        }

        // Fetch the diagonal characters
        Character topLeft = matrix.get(row - 1).get(col - 1);
        Character topRight = matrix.get(row - 1).get(col + 1);
        Character bottomLeft = matrix.get(row + 1).get(col - 1);
        Character bottomRight = matrix.get(row + 1).get(col + 1);

        // Check both diagonals for valid "M", "A", "S" pattern
        return checkDiagonal(topLeft, bottomRight) && checkDiagonal(topRight, bottomLeft);
    }

    private boolean checkDiagonal(Character first, Character second) {
        // Check if the set contains "M" and "S" (ignoring other characters)
        return new HashSet<>(Arrays.asList(first, second)).containsAll(Set.of('M', 'S'));
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

}
