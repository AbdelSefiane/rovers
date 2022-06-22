package org.sef.kata;

public enum Direction {

    NORTH(new Vector(0, 1)),
    EAST(new Vector(-1, 0)),
    SOUTH(new Vector(0, -1)),
    WEST(new Vector(1, 0));

    protected Vector direction;

    Direction(int x, int y) {
        this.direction=new Vector(x,y);
    }

    Direction(Vector direction) {
        java.util.Vector v = new java.util.Vector<Integer>();
        this.direction=direction;
    }

    public static Direction vectorToDirection(Vector v){
        return vectorToDirection(v.x,v.y);
    }

    public static Direction vectorToDirection(int x, int y){
        Direction n = Direction.SOUTH;
        if(x==0){
            if(y==-1){
                n = Direction.NORTH;
            }
        } else if(y == 0){
            if(x == -1){
                n= Direction.WEST;
            } else {
                  n= Direction.EAST;
            }
        }
        return n;
    }

    public static Direction random(){
        Direction[] values = Direction.values();
        int rand = (int)(Math.random()*values.length);
        return values[rand];
    }

    @Override
    public String toString() {
        return this.name();
    }
}
