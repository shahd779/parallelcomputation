import java.lang.Thread.UncaughtExceptionHandler;
class Worker extends Thread {
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + " started.");
            int result = 10 / 0;
        } catch (Exception e) {
            System.out.println(Thread.currentThread().getName() + " caught: " + e);
        }
        System.out.println(Thread.currentThread().getName() + " finished.");
    }
}

class WorkerThread extends Thread {
    public void run() {
        System.out.println("Thread started: " + getName());
        int x = 10 / 0;
    }
}

public class ParallelComputationExamples {

    public static void multiThreadExceptionExample() {
        System.out.println("\n===  threads  try-catch  ===");
        Worker t1 = new Worker();
        Worker t2 = new Worker();
        Worker t3 = new Worker();
        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void exampleUncaughtHandler() {
        System.out.println("\n===  UncaughtExceptionHandler  ===");
        WorkerThread t1 = new WorkerThread();
        t1.setUncaughtExceptionHandler((thread, exception) -> {
            System.out.println("▲ Exception in " + thread.getName() + ": " + exception.getMessage());
        });
        t1.start();

        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void defaultHandlerExample() {
        System.out.println("\n===  DefaultUncaughtExceptionHandler ===");

        Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
            System.out.println("Global handler caught exception in: " + thread.getName());
            System.out.println("Error: " + exception.getMessage());
        });

        Thread t1 = new Thread(() -> {
            throw new RuntimeException("Thread crashed!");
        });

        Thread t2 = new Thread(() -> {
            throw new ArithmeticException("Division by zero!");
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int sharedCounter = 0;

    public static void raceConditionExample() {
        System.out.println("\n===  Race Condition ===");

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                sharedCounter++;
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final counter value (expected 2000): " + sharedCounter);
    }

    public static void comparisonExample() {
        System.out.println("\n=== comparison between UncaughtExceptionHandler و DefaultUncaughtExceptionHandler ===");

        Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
            System.out.println("[Default] Exception in " + thread.getName() + ": " + exception.getMessage());
        });

        Thread t1 = new Thread(() -> {
            throw new RuntimeException("Thread 1 exception");
        });

        Thread t2 = new Thread(() -> {
            throw new RuntimeException("Thread 2 exception");
        });

        t1.setUncaughtExceptionHandler((thread, exception) -> {
            System.out.println("[Custom] Exception in " + thread.getName() + ": " + exception.getMessage());
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("===== Parallel Computation - Exception Handling & Race Conditions =====");

        multiThreadExceptionExample();
        exampleUncaughtHandler();
        defaultHandlerExample();
        raceConditionExample();
        comparisonExample();

        System.out.println("\n===== End of Examples =====");
    }
}