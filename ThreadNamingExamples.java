public class ThreadNamingExamples {
    // Example 1: Creating thread with name in constructor
    static void example1() {
        Thread t1 = new Thread("MyFirstThread");
        t1.start();
        System.out.println("Example 1 - Thread Name: " + t1.getName());
    }

    // Example 2: Setting thread name after creation
    static void example2() {
        Thread t2 = new Thread();
        t2.setName("WorkerThread-1");
        t2.start();
        System.out.println("Example 2 - Thread Name: " + t2.getName());
    }

    // Example 3: Using Runnable with named thread
    static void example3() {
        Runnable task = () -> System.out.println("Example 3 - Running in: " + Thread.currentThread().getName());
        Thread t3 = new Thread(task, "RunnableThread");
        t3.start();
    }

    // Example 4: Thread priorities
    static void example4() {
        Thread t1 = new Thread(() -> 
            System.out.println("Example 4 - Thread 1 Priority: " + Thread.currentThread().getPriority()));
        Thread t2 = new Thread(() -> 
            System.out.println("Example 4 - Thread 2 Priority: " + Thread.currentThread().getPriority()));
        
        t1.setPriority(Thread.MIN_PRIORITY); // 1
        t2.setPriority(Thread.MAX_PRIORITY); // 10
        t1.start();
        t2.start();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread Naming Examples ===");
        example1();
        Thread.sleep(100); // small delay between examples
        
        System.out.println("\n=== Thread Naming with setName() ===");
        example2();
        Thread.sleep(100);
        
        System.out.println("\n=== Thread Naming with Runnable ===");
        example3();
        Thread.sleep(100);
        
        System.out.println("\n=== Thread Priorities ===");
        example4();
    }
}