public class DaemonThreadExample {
    public static void main(String[] args) {
        Thread daemon = new Thread(() -> {
            try {
                while (true) {
                    System.out.println("Daemon thread running: " + Thread.currentThread().getName());
                    Thread.sleep(500); // تم التصحيح: كان مكتوب 50s
                }
            } catch (InterruptedException e) {
                System.out.println("Daemon interrupted");
            }
        }, "MyDaemonThread");
        daemon.setDaemon(true); // mark as daemon
        daemon.start();
        System.out.println("Main thread ends: " + Thread.currentThread().getName());
    }
}