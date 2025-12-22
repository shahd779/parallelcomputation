import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class ProducerConsumerExample {
    private final Queue<String> buffer = new LinkedList<>();
    private final int CAPACITY = 5;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition bufferNotFull = lock.newCondition();
    private final Condition bufferNotEmpty = lock.newCondition();

    class Producer extends Thread {
        private String name;
        
        public Producer(String name) {
            this.name = name;
        }
        
        public void run() {
            for (int i = 1; i <= 10; i++) {
                try {
                    Thread.sleep((int)(Math.random() * 1000));
                    
                    lock.lock();
                    try {
                        while (buffer.size() == CAPACITY) {
                            System.out.println(name + ": Buffer full, waiting...");
                            bufferNotFull.await();
                        }
                        
                        String message = name + " - Message " + i;
                        buffer.add(message);
                        System.out.println(name + " produced: " + message);
                        System.out.println("Buffer size: " + buffer.size());
                        
                        bufferNotEmpty.signalAll();
                    } finally {
                        lock.unlock();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(name + " finished producing.");
        }
    }

    class Consumer extends Thread {
        private String name;
        
        public Consumer(String name) {
            this.name = name;
        }
        
        public void run() {
            for (int i = 1; i <= 10; i++) {
                try {
                    Thread.sleep((int)(Math.random() * 1500));
                    
                    lock.lock();
                    try {
                        while (buffer.isEmpty()) {
                            System.out.println(name + ": Buffer empty, waiting...");
                            bufferNotEmpty.await();
                        }
                        
                        String message = buffer.poll();
                        System.out.println(name + " consumed: " + message);
                        System.out.println("Buffer size: " + buffer.size());
                        
                        bufferNotFull.signalAll();
                    } finally {
                        lock.unlock();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(name + " finished consuming.");
        }
    }

    public void startSimulation() {
        System.out.println("===== Producer-Consumer Simulation =====");
        System.out.println("Buffer Capacity: " + CAPACITY);
        System.out.println("Each producer produces 10 messages");
        System.out.println("Each consumer consumes 10 messages");
        System.out.println("----------------------------------------");
        
        Producer[] producers = {
            new Producer("Producer-1"),
            new Producer("Producer-2")
        };
        
        Consumer[] consumers = {
            new Consumer("Consumer-1"),
            new Consumer("Consumer-2"),
            new Consumer("Consumer-3")
        };
        
        for (Producer p : producers) {
            p.start();
        }
        
        for (Consumer c : consumers) {
            c.start();
        }
        
        for (Producer p : producers) {
            try {
                p.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        for (Consumer c : consumers) {
            try {
                c.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("----------------------------------------");
        System.out.println("Simulation completed!");
        System.out.println("Final buffer size: " + buffer.size());
        System.out.println("========================================");
    }

    public static void main(String[] args) {
        ProducerConsumerExample example = new ProducerConsumerExample();
        example.startSimulation();
    }
}