package org.sef.kata;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class RoverTest {

    public static Stream<Arguments> rightRotationCases() {
        Vector pos = new Vector(1, 1);
        return Stream.of(Arguments.of(new Rover(pos, Direction.NORTH), Direction.EAST),
                         Arguments.of(new Rover(pos, Direction.EAST), Direction.SOUTH),
                         Arguments.of(new Rover(pos, Direction.SOUTH), Direction.WEST),
                         Arguments.of(new Rover(pos, Direction.WEST), Direction.NORTH)
                        );
    }

    public static Stream<Arguments> leftRotationCases() {
        Vector pos = new Vector(1, 1);
        return Stream.of(Arguments.of(new Rover(pos, Direction.NORTH), Direction.WEST),
                         Arguments.of(new Rover(pos, Direction.WEST), Direction.SOUTH),
                         Arguments.of(new Rover(pos, Direction.SOUTH), Direction.EAST),
                         Arguments.of(new Rover(pos, Direction.EAST), Direction.NORTH)
                        );
    }

    //TRANSLATE FORWARD
    @Test
    public void movingForwardShouldSumFacingDirectionsYComponentWithRoversYComponentNorthFacingRover() {
        Rover northFacingRover = new Rover(1, 1, Direction.NORTH);
        Map m = new Map(5);
        northFacingRover.setMap(m);
        Vector move = northFacingRover.move(Command.FORWARD);
        Assertions.assertEquals(1, northFacingRover.pos.x);
        Assertions.assertEquals(2, move.y);
    }

    @Test
    public void movingForwardShouldSumFacingDirectionsXComponentWithRoversXComponentEastFacingRover() {
        Rover eastFacingRover = new Rover(1, 1, Direction.EAST);
        Map m = new Map(5);
        eastFacingRover.setMap(m);
        Vector move = eastFacingRover.move(Command.FORWARD);
        Assertions.assertEquals(0, move.x);
        Assertions.assertEquals(1, move.y);
    }

    @Test
    public void movingForwardShouldSubstractFacingDirectionsXComponentWithRoversXComponentWestFacingRover() {
        Rover westFacingRover = new Rover(1, 1, Direction.WEST);
        Map m = new Map(5);
        westFacingRover.setMap(m);
        Vector move = westFacingRover.move(Command.FORWARD);
        Assertions.assertEquals(2, move.x);
        Assertions.assertEquals(1, move.y);
    }

    @Test
    public void movingForwardShouldSubstractFacingDirectionsYComponentWithRoversYComponentSouthFacingRover() {
        Rover southFacingRover = new Rover(1, 1, Direction.SOUTH);
        Map m = new Map(5);
        southFacingRover.setMap(m);
        Vector move = southFacingRover.move(Command.FORWARD);
        Assertions.assertEquals(1, move.x);
        Assertions.assertEquals(0, move.y);
    }

    //TRANSLATE BACKWARD
    @Test
    public void movingBackwardShouldSubstractFacingDirectionsYComponentWithRoversYComponentNorth() {
        Rover northFacingRover = new Rover(1, 1, Direction.NORTH);
        Map m = new Map(5);
        northFacingRover.setMap(m);
        Vector move = northFacingRover.move(Command.BACKWARD);
        Assertions.assertEquals(1, move.x);
        Assertions.assertEquals(0, move.y);
    }

    @Test
    public void movingBackwardShouldSubstractFacingDirectionsYComponentWithRoversYComponentSouth() {
        Rover southFacingRover = new Rover(1, 1, Direction.SOUTH);
        Map m = new Map(5);
        southFacingRover.setMap(m);
        Vector move = southFacingRover.move(Command.BACKWARD);
        Assertions.assertEquals(1, move.x);
        Assertions.assertEquals(2, move.y);
    }

    @Test
    public void movingBackwardShouldSubstractFacingDirectionsXComponentWithRoversXComponentEast() {
        Rover eastFacingRover = new Rover(1, 1, Direction.EAST);
        Map m = new Map(5);
        eastFacingRover.setMap(m);
        Vector move = eastFacingRover.move(Command.BACKWARD);
        Assertions.assertEquals(2, move.x);
        Assertions.assertEquals(1, move.y);
    }

    @Test
    public void movingBackwardShouldSumFacingDirectionsXComponentWithRoversXComponentWest() {
        Rover westFacingRover = new Rover(1, 1, Direction.WEST);
        Map m = new Map(5);
        westFacingRover.setMap(m);
        Vector move = westFacingRover.move(Command.BACKWARD);
        Assertions.assertEquals(0, move.x);
        Assertions.assertEquals(1, move.y);
    }

    //ROTATE RIGHT
    @ParameterizedTest
    @MethodSource("rightRotationCases")
    public void rightRotationShouldTurnRoverToExpectedDirection(Rover rover, Direction expectedDirection) {
        rover.rotateRight();
        Assertions.assertEquals(expectedDirection, rover.facingDirection);
    }

    //ROTATE LEFT
    @ParameterizedTest
    @MethodSource("leftRotationCases")
    public void leftRotationShouldTurnRoverToExpectedDirection(Rover rover, Direction expectedDirection) {
        rover.rotateLeft();
        Assertions.assertEquals(expectedDirection, rover.facingDirection);
    }

    //MovementInMap
    @Test
    public void roverShouldMoveFreelyInEmptyMap() {
        Map m = new Map(5);
        List<Command> commands = List.of(Command.FORWARD, Command.LEFT, Command.FORWARD);
        Rover rv = new Rover(new Vector(1, 1), Direction.NORTH, m, commands);
        int nbMove = rv.move();
        int posContent = m.getPosContent(rv.pos);
        Assertions.assertEquals(3, nbMove);
        Assertions.assertEquals(1, posContent);
        Assertions.assertEquals(rv.pos.x, 2);
        Assertions.assertEquals(rv.pos.y, 2);
    }

    @Test
    public void multipleRoversShouldMoveFreelyInEmptyMap() {
        Map m = new Map(5);
        List<Command> commands = List.of(Command.FORWARD, Command.LEFT, Command.FORWARD);
        ArrayList<Rover> rvs = new ArrayList<>();
        rvs.add(new Rover(new Vector(1, 1), Direction.NORTH, m, commands));
        rvs.add(new Rover(new Vector(2, 3), Direction.NORTH, m, commands));
        rvs.add(new Rover(new Vector(2, 2), Direction.NORTH, m, commands));
        rvs.add(new Rover(new Vector(1, 2), Direction.NORTH, m, commands));
        rvs.add(new Rover(new Vector(2, 1), Direction.NORTH, m, commands));
        m.initRoversPosition(rvs);
        ExecutorService service = Executors.newFixedThreadPool(5);
        CountDownLatch gate = new CountDownLatch(1);
        for(int i=0;i<rvs.size();i++){
            final int finalI = i;
            service.submit(() -> {
                rvs.get(finalI).move();
            });
        }
        gate.countDown();
        service.shutdown();
        Assertions.assertEquals(1, rvs.get(0).pos.x);
        Assertions.assertEquals(1, rvs.get(0).pos.y);
    }
}
