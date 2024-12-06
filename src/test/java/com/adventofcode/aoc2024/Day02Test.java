package com.adventofcode.aoc2024;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.adventofcode.aoc2024.Day02.DIFFERENCE_OK;
import static com.adventofcode.aoc2024.Day02.IS_DECREASING;
import static com.adventofcode.aoc2024.Day02.IS_INCREASING;
import static com.adventofcode.aoc2024.Day02.IS_SAFE;
import static com.adventofcode.aoc2024.Day02.IS_SAFE_WITH_ONE_REMOVAL;
import static org.junit.jupiter.api.Assertions.*;

class Day02Test {
    @Test
    void testIsIncreasing() {
        assertTrue(IS_INCREASING.test(List.of(1, 2, 3, 4, 5)), "Should be increasing");
        assertFalse(IS_INCREASING.test(List.of(5, 4, 3, 2, 1)), "Should not be increasing");
        assertFalse(IS_INCREASING.test(List.of(1, 2, 2, 4, 5)), "Should not be increasing with equal values");
    }

    @Test
    void testIsDecreasing() {
        assertTrue(IS_DECREASING.test(List.of(5, 4, 3, 2, 1)), "Should be decreasing");
        assertFalse(IS_DECREASING.test(List.of(1, 2, 3, 4, 5)), "Should not be decreasing");
        assertFalse(IS_DECREASING.test(List.of(5, 4, 4, 2, 1)), "Should not be decreasing with equal values");
    }

    @Test
    void testDifferenceOk() {
        assertTrue(DIFFERENCE_OK.test(List.of(1, 3, 6, 7, 9)), "All differences should be within 1 and 3");
        assertFalse(DIFFERENCE_OK.test(List.of(1, 2, 7, 8, 9)), "Difference of 5 (2 to 7) is too large");
        assertFalse(DIFFERENCE_OK.test(List.of(9, 7, 6, 2, 1)), "Difference of 4 (6 to 2) is too large");
    }

    @Test
    void testIsSafe() {
        // Safe examples
        assertTrue(IS_SAFE.test(List.of(7, 6, 4, 2, 1)), "Safe because levels decrease by 1 or 2");
        assertTrue(IS_SAFE.test(List.of(1, 3, 6, 7, 9)), "Safe because levels increase by 1, 2, or 3");

        // Unsafe examples
        assertFalse(IS_SAFE.test(List.of(1, 2, 7, 8, 9)), "Unsafe due to large increase (2 to 7)");
        assertFalse(IS_SAFE.test(List.of(9, 7, 6, 2, 1)), "Unsafe due to large decrease (6 to 2)");
        assertFalse(IS_SAFE.test(List.of(1, 3, 2, 4, 5)), "Unsafe because it alternates increase/decrease");
        assertFalse(IS_SAFE.test(List.of(8, 6, 4, 4, 1)), "Unsafe due to non-increasing/decreasing segment (4 to 4)");
    }

    @Test
    void testIsSafeWithOneRemoval() {
        // Safe without removal
        assertTrue(IS_SAFE_WITH_ONE_REMOVAL.test(List.of(7, 6, 4, 2, 1)), "Already safe (decreasing by 1 or 2)");
        assertTrue(IS_SAFE_WITH_ONE_REMOVAL.test(List.of(1, 3, 6, 7, 9)), "Already safe (increasing by 1, 2, or 3)");

        // Unsafe even with removal
        assertFalse(IS_SAFE_WITH_ONE_REMOVAL.test(List.of(1, 2, 7, 8, 9)), "Cannot be made safe by removing any level");
        assertFalse(IS_SAFE_WITH_ONE_REMOVAL.test(List.of(9, 7, 6, 2, 1)), "Cannot be made safe by removing any level");

        // Safe with one level removal
        assertTrue(IS_SAFE_WITH_ONE_REMOVAL.test(List.of(1, 3, 2, 4, 5)), "Safe after removing level 3");
        assertTrue(IS_SAFE_WITH_ONE_REMOVAL.test(List.of(8, 6, 4, 4, 1)), "Safe after removing level 4");
    }
}
