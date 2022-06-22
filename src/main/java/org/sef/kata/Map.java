package org.sef.kata;

import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;

import static org.sef.kata.MapCodif.ROVER;

//Top left corner coordinate must be 1;1
public class Map {

    protected final AtomicIntegerArray map;
    protected final int size;

    public Map(int size) {
        this.size = size;
        this.map = new AtomicIntegerArray(size * size);
        for (int i = 0; i < map.length(); i++) {
            map.set(i, 0);
        }
    }

    public void initRoversPosition(List<Rover> rovers) {
        for (Rover r : rovers) {
            this.updateCell(r.pos.x, r.pos.y, ROVER);
        }
    }

    /**
     * Update the map's cell given (x;y) integer coordinates.
     * The cell's content is incremented by one indicating a rover is inside.
     *
     * @param x
     * @param y
     */
    public void updateCell(int x, int y, MapCodif value) {
        int coordinate = x + 5 * y - 6;
        synchronized(this.map) {
            this.map.set(coordinate, value.value);
        }
    }

    public AtomicIntegerArray getMap() {
        return this.map;
    }

    /**
     * Return the content of a cell using the following maping
     * If the given coordinate are outside of the map, returns -1.
     * 0: Empty cell
     * 1: Rover inside
     * 2: Obstacle
     *
     * @param coordinate
     * @return
     */
    public int getPosContent(Vector coordinate) {
        int pos = coordinate.x + 5 * coordinate.y - 6;
        int content = 0;
        if (coordinate.x < 0 || coordinate.y < 0 || pos >= size * size) {
            content = -1;
        } else {
            content = this.map.get(pos);
        }
        return content;
    }

    /**
     * @param v
     * @return
     */
    public boolean isAvailablePos(Vector v) {
        int coordinate = v.x + 5 * v.y - 6;
        if (v.x < 0 || v.y < 0 || coordinate >= size * size) {
            return false;
        }
        return this.map.get(coordinate) == 0;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        synchronized(this.map) {
            for (int i = 0; i < map.length(); i++) {
                output.append(map.get(i));
                if (i != 0 && (i + 1) % this.size == 0) {
                    output.append("\n");
                }
            }
        }
        return output.toString();
    }
}
