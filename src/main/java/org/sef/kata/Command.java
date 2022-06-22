package org.sef.kata;

public enum Command {

    FORWARD("f"),
    BACKWARD("b"),
    RIGHT("r"),
    LEFT("l");

    private String label;

    Command(String label) {
        this.label = label;
    }

    public static boolean isMovementCommand(Command c){
        return FORWARD.equals(c) || FORWARD.equals(BACKWARD);
    }
}
