import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumerExample {
    // Shared buffer between all threads
    private final Queue<String> buffer = new LinkedList<>();
    private final int BUFFER_CAPACITY = 5;
    private final int PRODUCER_COUNT = 2;
    private final int CONSUMER_COUNT = 2;
    private final int MESSAGES_PER_PRODUCER = 10;
    
    public static void main(String[] args) {
        ProducerConsumerExample example = new ProducerConsumerExample();
        example.startSimulation();
    }
    
    public void startSimulation() {
        System.out.println("=== Starting Producer-Consumer Simulation ===");
        System.out.println("Buffer Capacity: " + BUFFER_CAPACITY);
        System.out.println("Producers: " + PRODUCER_COUNT + " | Consumers: " + CONSUMER_COUNT);
        System.out.println("Messages per Producer: " + MESSAGES_PER_PRODUCER + "\n");
        
        // Create and start producers
        Thread[] producers = new Thread[PRODUCER_COUNT];
        for (int i = 0; i < PRODUCER_COUNT; i++) {
            producers[i] = new Thread(new Producer(), "Producer-" + (i + 1));
            producers[i].start();
        }
        
        // Create and start consumers
        Thread[] consumers = new Thread[CONSUMER_COUNT];
        for (int i = 0; i < CONSUMER_COUNT; i++) {
            consumers[i] = new Thread(new Consumer(), "Consumer-" + (i + 1));
            consumers[i].start();
        }
        
        // Wait for all producers to finish
        for (Thread producer : producers) {
            try {
                producer.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Add poison pills to stop consumers
        synchronized (buffer) {
            for (int i = 0; i < CONSUMER_COUNT; i++) {
                buffer.add("POISON_PILL");
            }
            buffer.notifyAll();
        }
        
        // Wait for all consumers to finish
        for (Thread consumer : consumers) {
            try {
                consumer.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println("\n=== Simulation Completed ===");
        System.out.println("Final buffer size: " + buffer.size());
    }
    
    // Producer class
    class Producer implements Runnable {
        private int messageCount = 0;
        
        @Override
        public void run() {
            String producerName = Thread.currentThread().getName();
            
            try {
                for (int i = 0; i < MESSAGES_PER_PRODUCER; i++) {
                    String message = "Msg-" + (++messageCount) + "-from-" + producerName;
                    
                    synchronized (buffer) {
                        // Wait if buffer is full
                        while (buffer.size() >= BUFFER_CAPACITY) {
                            System.out.println(producerName + " waiting - Buffer FULL (" + buffer.size() + "/" + BUFFER_CAPACITY + ")");
                            buffer.wait();
                        }
                        
                        // Add message to buffer
                        buffer.add(message);
                        System.out.println(producerName + " PRODUCED: " + message + 
                                         " [Buffer: " + buffer.size() + "/" + BUFFER_CAPACITY + "]");
                        
                        // Notify waiting consumers
                        buffer.notifyAll();
                    }
                    
                    // Simulate production time
                    Thread.sleep((int)(Math.random() * 500) + 100);
                }
                
                System.out.println(producerName + " finished producing " + MESSAGES_PER_PRODUCER + " messages");
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(producerName + " interrupted");
            }
        }
    }
    
    // Consumer class
    class Consumer implements Runnable {
        @Override
        public void run() {
            String consumerName = Thread.currentThread().getName();
            int messagesConsumed = 0;
            
            try {
                while (true) {
                    String message;
                    
                    synchronized (buffer) {
                        // Wait if buffer is empty
                        while (buffer.isEmpty()) {
                            System.out.println(consumerName + " waiting - Buffer EMPTY");
                            buffer.wait();
                        }
                        
                        // Get message from buffer
                        message = buffer.poll();
                        
                        // Check for poison pill (termination signal)
                        if ("POISON_PILL".equals(message)) {
                            System.out.println(consumerName + " received POISON_PILL - Stopping");
                            break;
                        }
                        
                        messagesConsumed++;
                        System.out.println(consumerName + " CONSUMED: " + message + 
                                         " [Buffer: " + buffer.size() + "/" + BUFFER_CAPACITY + "]");
                        
                        // Notify waiting producers
                        buffer.notifyAll();
                    }
                    
                    // Simulate consumption/processing time
                    Thread.sleep((int)(Math.random() * 800) + 200);
                }
                
                System.out.println(consumerName + " finished - Consumed " + messagesConsumed + " messages");
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(consumerName + " interrupted");
            }
        }
    }
}