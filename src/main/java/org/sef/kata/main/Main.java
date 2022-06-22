package org.sef.kata.main;

import org.sef.kata.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.sef.kata.Command.*;

public class Main {

    public static void main(String[] args){
        Map m = new Map(5);
        ArrayList<Command> commands = new ArrayList<>();
        commands.add(FORWARD);
        commands.add(LEFT);
        commands.add(FORWARD);
        Vector initPos = new Vector(1, 1);
        Rover r = new Rover(initPos, Direction.NORTH,m, commands);
        ExecutorService service = Executors.newFixedThreadPool(1);
        Future<Integer> submit = service.submit(() -> r.move());
        try {
            Integer nbMove = submit.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println(r);
        }
        String s = m.toString();
        System.out.println(s);
    }
}
