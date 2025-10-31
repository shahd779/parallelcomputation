public class ThreadStateExamples {
    // Shows basic thread states
    static void demonstrateStates() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            System.out.println("State inside run(): " + Thread.currentThread().getState());
            try {
                Thread.sleep(100); // Add some execution time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // State 1: NEW
        System.out.println("Before start(): " + t1.getState());
        
        // State 2: RUNNABLE
        t1.start();
        System.out.println("After start(): " + t1.getState());
        
        // Wait until the thread finishes
        t1.join();
        
        // State 3: TERMINATED
        System.out.println("After completion: " + t1.getState());
    }

    // Additional example showing TIMED_WAITING state
    static void demonstrateTimedWaiting() throws InterruptedException {
        Thread sleepingThread = new Thread(() -> {
            try {
                Thread.sleep(1000); // Will be in TIMED_WAITING
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        sleepingThread.start();
        Thread.sleep(100); // Give time for the other thread to enter sleep
        System.out.println("Sleeping thread state: " + sleepingThread.getState());
        sleepingThread.join(); // Wait for completion
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Basic Thread States ===");
        demonstrateStates();
        
        System.out.println("\n=== TIMED_WAITING State Example ===");
        demonstrateTimedWaiting();
    }
}