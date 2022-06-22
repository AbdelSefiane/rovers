package org.sef.kata;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DirectionTest {

    private static Stream<Arguments> vectorToDirectionInput() {
        return Stream.of(Arguments.of(0, 1, Direction.NORTH),
                         Arguments.of(0, -1, Direction.SOUTH),
                         Arguments.of(1, 0, Direction.EAST),
                         Arguments.of(-1, 0, Direction.WEST)
                        );
    }

    @ParameterizedTest
    @MethodSource(value = "vectorToDirectionInput")
    public void vectorToDirectionShouldReturnExpectedDirection(int x, int y, Direction expectedDirection) {
        Assertions.assertEquals(Direction.vectorToDirection(x, y), expectedDirection);
    }

}
