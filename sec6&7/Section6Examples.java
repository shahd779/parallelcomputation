import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

class SynchronizedExample {
    private final Object monitor = new Object();

    public void criticalSection() {
        synchronized (monitor) {
            System.out.println("Executing critical section");
        }
    }
}

class ReentrantLockExample {
    private final ReentrantLock lock = new ReentrantLock();

    public void criticalSection() {
        lock.lock();
        try {
            System.out.println("Executing critical section");
        } finally {
            lock.unlock();
        }
    }
}

public class Section6Examples {

    static class SharedData {
        boolean dataReady = false;
    }

    public static void reentrantExample() {
        System.out.println("\n=== ReentrantLock - Reentrant Example ===");
        ReentrantLock lock = new ReentrantLock();

        Thread thread = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Thread acquired lock first time");
                lock.lock();
                try {
                    System.out.println("Same thread acquired lock second time");
                } finally {
                    lock.unlock();
                    System.out.println("First unlock");
                }
            } finally {
                lock.unlock();
                System.out.println("Second unlock");
            }
        });
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void tryLockExample() {
        System.out.println("\n=== ReentrantLock - tryLock() Example ===");
        ReentrantLock lock = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Thread 1 got the lock, sleeping for 1 second");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println("Thread 1 released the lock");
            }
        });

        Thread t2 = new Thread(() -> {
            if (lock.tryLock()) {
                try {
                    System.out.println("Thread 2 got the lock!");
                } finally {
                    lock.unlock();
                }
            } else {
                System.out.println("Thread 2 could not get the lock");
            }
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

    public static void tryLockWithTimeoutExample() {
        System.out.println("\n=== ReentrantLock - tryLock with Timeout Example ===");
        ReentrantLock lock = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Thread 1 got the lock, sleeping for 3 seconds");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println("Thread 1 released the lock");
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                if (lock.tryLock(2, TimeUnit.SECONDS)) {
                    try {
                        System.out.println("Thread 2 got the lock within 2 seconds");
                    } finally {
                        lock.unlock();
                    }
                } else {
                    System.out.println("Thread 2 timeout after 2 seconds");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
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

    public static void fairnessExample() {
        System.out.println("\n=== ReentrantLock - Fairness Example ===");
        ReentrantLock fairLock = new ReentrantLock(true);

        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " trying to acquire lock");
            fairLock.lock();
            try {
                System.out.println(threadName + " acquired lock");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                fairLock.unlock();
                System.out.println(threadName + " released lock");
            }
        };

        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(task, "Thread-" + (i + 1));
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void lockInterruptiblyExample() {
        System.out.println("\n=== ReentrantLock - lockInterruptibly Example ===");
        ReentrantLock lock = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Thread 1 got the lock, holding for 5 seconds");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println("Thread 1 released the lock");
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                System.out.println("Thread 2 trying to acquire lock interruptibly");
                lock.lockInterruptibly();
                try {
                    System.out.println("Thread 2 acquired lock");
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                System.out.println("Thread 2 was interrupted while waiting for lock");
            }
        });

        t1.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start();

        try {
            Thread.sleep(1000);
            System.out.println("Interrupting Thread 2");
            t2.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void conditionExample() {
        System.out.println("\n=== ReentrantLock - Condition Example ===");
        ReentrantLock lock = new ReentrantLock();
        var condition = lock.newCondition();
        SharedData sharedData = new SharedData();

        Thread producer = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Producer: Producing data...");
                Thread.sleep(1000);
                sharedData.dataReady = true;
                System.out.println("Producer: Data ready, signaling consumer");
                condition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        Thread consumer = new Thread(() -> {
            lock.lock();
            try {
                while (!sharedData.dataReady) {
                    System.out.println("Consumer: Waiting for data...");
                    condition.await();
                }
                System.out.println("Consumer: Processing data");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        consumer.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        producer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("===== Section 6: ReentrantLock Examples =====");

        reentrantExample();
        tryLockExample();
        tryLockWithTimeoutExample();
        fairnessExample();
        lockInterruptiblyExample();
        conditionExample();

        System.out.println("\n===== End of Examples =====");
    }
}