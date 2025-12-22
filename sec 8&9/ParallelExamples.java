import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class ParallelExamples {

    public static void semaphoreExample() {
        System.out.println("\n=== Semaphore Example ===");
        Semaphore semaphore = new Semaphore(3);
        
        Runnable task = () -> {
            try {
                semaphore.acquire();
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + " entered critical section");
                Thread.sleep(1000);
                System.out.println(threadName + " leaving critical section");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        };
        
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(task, "Thread-" + (i + 1));
            threads[i].start();
        }
        
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void atomicExample() {
        System.out.println("\n=== Atomic Classes Example ===");
        AtomicInteger counter = new AtomicInteger(0);
        
        Runnable incrementTask = () -> {
            for (int i = 0; i < 1000; i++) {
                counter.incrementAndGet();
            }
        };
        
        Thread t1 = new Thread(incrementTask);
        Thread t2 = new Thread(incrementTask);
        
        t1.start();
        t2.start();
        
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Final counter value: " + counter.get() + " (expected 2000)");
    }

    public static void volatileExample() {
        System.out.println("\n=== Volatile Keyword Example ===");
        class SharedData {
            volatile boolean flag = false;
        }
        
        SharedData data = new SharedData();
        
        Thread writer = new Thread(() -> {
            try {
                Thread.sleep(1000);
                data.flag = true;
                System.out.println("Writer: Flag set to true");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        Thread reader = new Thread(() -> {
            while (!data.flag) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Reader: Flag is now true");
        });
        
        reader.start();
        writer.start();
        
        try {
            reader.join();
            writer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void threadPoolExample() {
        System.out.println("\n=== ThreadPoolExecutor Example ===");
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2,
            4,
            1,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10)
        );
        
        Runnable task = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " executing task");
                Thread.sleep(500);
                System.out.println(Thread.currentThread().getName() + " completed task");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        
        for (int i = 0; i < 10; i++) {
            executor.execute(task);
        }
        
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void scheduledThreadPoolExample() {
        System.out.println("\n=== ScheduledThreadPoolExecutor Example ===");
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        
        System.out.println("Scheduling tasks...");
        
        scheduler.schedule(() -> {
            System.out.println("Task executed after 2 second delay");
        }, 2, TimeUnit.SECONDS);
        
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Fixed rate task - every 1 second");
        }, 0, 1, TimeUnit.SECONDS);
        
        scheduler.scheduleWithFixedDelay(() -> {
            try {
                System.out.println("Fixed delay task - 500ms after completion");
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        scheduler.shutdown();
    }

    public static void main(String[] args) {
        System.out.println("===== Parallel Computation Examples =====");
        
        semaphoreExample();
        atomicExample();
        volatileExample();
        threadPoolExample();
        scheduledThreadPoolExample();
        
        System.out.println("\n===== All Examples Completed =====");
    }
}