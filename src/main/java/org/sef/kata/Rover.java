package org.sef.kata;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;

import static org.sef.kata.Command.*;
import static org.sef.kata.Direction.*;
import static org.sef.kata.MapCodif.ROVER;

public class Rover {

    Vector pos;
    Direction facingDirection;
    private List<Command> command;
    private Map map;


    public Rover(Vector pos, Direction d, Map map, List<Command> command) {
        this.pos = pos;
        this.facingDirection = d;
        this.command = command;
        this.map = map;
    }

    public Rover(Vector pos, Map map, List<Command> command) {
        this.pos = pos;
        this.facingDirection = Direction.random();
        this.command = command;
        this.map = map;
    }

    protected Rover(Vector pos, Direction d) {
        this.pos = pos;
        this.facingDirection = d;
        this.command = new ArrayList<Command>();
        this.map = null;
    }

    protected Rover(int x, int y, Direction d) {
        this(new Vector(x, y), d);
    }

    public int move() {
        int nbMove = 0;
        AtomicIntegerArray map = this.map.map;
        boolean hasMoved = true;
        while (nbMove < command.size() && hasMoved) {
            Command cmd = command.get(nbMove);
            if (!isMovementCommand(cmd)) {
                rotate(cmd);
            } else {
                hasMoved = move(cmd, map);
            }
            nbMove++;
        }
        return nbMove;
    }

    private boolean move(Command cmd, AtomicIntegerArray map) {
        boolean hasMoved = true;
        Vector proposedNewPos;
        proposedNewPos = move(cmd);
        synchronized(map) {
            if (!this.map.isAvailablePos(proposedNewPos)) {
                hasMoved = false;
            } else {
                this.map.updateCell(proposedNewPos.x, proposedNewPos.y, ROVER);
                this.pos.x = proposedNewPos.x;
                this.pos.y = proposedNewPos.y;
            }
        }
        return hasMoved;
    }

    private void rotate(Command command) {
        if (RIGHT.equals(command)) {
            this.rotateRight();
        } else if (LEFT.equals(command)) {
            this.rotateLeft();
        }
    }

    public void setCommand(List<Command> command) {
        this.command = command;
    }

    protected Vector move(final Command command) {
        int moveFactor = 1;
        int newX = this.pos.x;
        int newY = this.pos.y;
        if (BACKWARD.equals(command)) {
            moveFactor = -1;
        }
        if (isVerticalMove()) {
            newY = this.pos.y + moveFactor * this.facingDirection.direction.y;
        }
        if (isHorizontalMove()) {
            newX = this.pos.x + moveFactor * this.facingDirection.direction.x;
        }

        Vector wrappedVector = this.wrapEdges(newX, newY);
        return wrappedVector;
    }

    private boolean isHorizontalMove() {
        return WEST.equals(this.facingDirection) || EAST.equals(this.facingDirection);
    }

    private boolean isVerticalMove() {
        return NORTH.equals(this.facingDirection) || SOUTH.equals(this.facingDirection);
    }

    //TODO TEST
    private Vector wrapEdges(int x, int y) {
        int wrappedX = x;
        int wrappedY = y;
        if (x > map.size) {
            wrappedX = 1;
        } else if (x < 0) {
            wrappedX = map.size;
        }
        if (y > map.size) {
            wrappedY = 1;
        } else if (y < 0) {
            wrappedY = map.size;
        }
        return new Vector(wrappedX, wrappedY);
    }

    protected void rotateRight() {
        Direction rotationResult = NORTH;
        if (NORTH.equals(facingDirection)) {
            rotationResult = Direction.EAST;
        } else if (Direction.EAST.equals(facingDirection)) {
            rotationResult = SOUTH;
        } else if (SOUTH.equals(facingDirection)) {
            rotationResult = Direction.WEST;
        }
        this.facingDirection = rotationResult;
    }

    protected void rotateLeft() {
        Direction rotationResult = NORTH;
        if (NORTH.equals(facingDirection)) {
            rotationResult = Direction.WEST;
        } else if (Direction.WEST.equals(facingDirection)) {
            rotationResult = SOUTH;
        } else if (SOUTH.equals(facingDirection)) {
            rotationResult = Direction.EAST;
        }
        this.facingDirection = rotationResult;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "Rover{" + "pos=" + pos + " - dir=" + facingDirection + '}';
    }
}
