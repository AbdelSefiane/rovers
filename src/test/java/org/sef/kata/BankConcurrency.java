package org.sef.kata;

import org.hamcrest.Matchers;
import org.hamcrest.junit.MatcherAssert;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * https://www.youtube.com/watch?v=rC17YwowURQ
 * Introduction to Multithreadting - Thread Safety
 */
public class BankConcurrency {

    class Bank {
        Map<String, Integer> balances = new ConcurrentHashMap<>(0);

        public int add(String userName, Integer dollars) {
            Integer newBalance = dollars;
            synchronized(this.balances) {
                newBalance = this.balances.compute(userName,
                                                   (key, val) -> val == null ? dollars : balances.get(userName) + dollars
                                                  );
            }
            return newBalance;
        }
    }

    class Bank2 {
        final Map<String, Integer> balances = new HashMap<>();

        public int add(String userName, int dollars) {
            int newBalance = 0;
            this.balances.putIfAbsent(userName, 0);
            synchronized(this.balances) {
                newBalance = this.balances.get(userName) + dollars;
                this.balances.put(userName, newBalance);
            }
            return newBalance;
        }
    }

    @Test
    public void test() throws InterruptedException, ExecutionException {
        final Bank b = new Bank();
        long b1b = System.currentTimeMillis();
        MatcherAssert.assertThat(b.add("RandomNamez", 1), Matchers.equalTo(1));
        ExecutorService service = Executors.newFixedThreadPool(1000);
        final CountDownLatch latch = new CountDownLatch(1);
        Future<?> RandomNamez = null;
        for (int i = 0; i < 1000; i++) {
            RandomNamez = service.submit(() -> {
                b.add("RandomNamez", 6);
            });
        }
        service.shutdown();
        latch.countDown();
        MatcherAssert.assertThat(b.add("RandomNamez", 0), Matchers.equalTo(6001));
        b.add("RandomNamez", 3);
        MatcherAssert.assertThat(b.add("RandomNamez", 0), Matchers.equalTo(6004));
        long b1e = System.currentTimeMillis();
        System.out.print(new StringBuilder().append("Bank: ")
                                 .append(b1e - b1b)
                                 .append('\n')
                                 .toString());
    }

    @Test
    public void test2() {
        final Bank2 b = new Bank2();
        long b1b = System.currentTimeMillis();
        MatcherAssert.assertThat(b.add("RandomNamez", 1), Matchers.equalTo(1));
        ExecutorService service = Executors.newFixedThreadPool(1000);
        final CountDownLatch latch = new CountDownLatch(1);
        Future<?> RandomNamez = null;
        for (int i = 0; i < 1000; i++) {
            RandomNamez = service.submit(() -> {

                b.add("RandomNamez", 6);
            });
        }
        service.shutdown();
        latch.countDown();
        MatcherAssert.assertThat(b.add("RandomNamez", 0), Matchers.equalTo(6001));
        b.add("RandomNamez", 3);
        MatcherAssert.assertThat(b.add("RandomNamez", 0), Matchers.equalTo(6004));
        long b1e = System.currentTimeMillis();
        System.out.print(new StringBuilder().append("Bank2: ")
                                 .append(b1e - b1b)
                                 .append('\n')
                                 .toString());
    }

}
