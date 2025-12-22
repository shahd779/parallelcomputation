public class UserThreadExample {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            System.out.println("Inside user thread: " + Thread.currentThread().getName());
        }, "UserThread-1");
        t.start();
        System.out.println("Main thread ends: " + Thread.currentThread().getName());
    }
}