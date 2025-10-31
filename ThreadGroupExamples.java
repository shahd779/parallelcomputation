public class ThreadGroupExamples {
    // Original ThreadGroup example
    static void basicThreadGroup() {
        ThreadGroup group = new ThreadGroup("MyGroup");
        Thread t1 = new Thread(group, () -> {
            System.out.println(Thread.currentThread().getName() + " in " +
                    Thread.currentThread().getThreadGroup().getName());
        }, "Thread-1");
        Thread t2 = new Thread(group, () -> {
            System.out.println(Thread.currentThread().getName() + " in " +
                    Thread.currentThread().getThreadGroup().getName());
        }, "Thread-2");
        t1.start();
        t2.start();
    }

    // List threads in group example
    static void listThreadsInGroup() throws InterruptedException {
        ThreadGroup group = new ThreadGroup("Workers");
        Thread t1 = new Thread(group, () -> {
            try {
                Thread.sleep(200); // Keep thread alive longer for demonstration
                System.out.println("Running: " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Worker-1");
        
        Thread t2 = new Thread(group, () -> {
            try {
                Thread.sleep(200); // Keep thread alive longer for demonstration
                System.out.println("Running: " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Worker-2");
        
        t1.start();
        t2.start();
        
        Thread.sleep(100); // wait for them to start
        System.out.println("Active Threads in Group: " + group.activeCount());
        group.list(); // prints details of threads in group
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Basic ThreadGroup Example ===");
        basicThreadGroup();
        Thread.sleep(500); // wait for first example to complete
        
        System.out.println("\n=== ThreadGroup Listing Example ===");
        listThreadsInGroup();
    }
}