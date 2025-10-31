import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Runnable> tasks = Arrays.asList(
            () -> System.out.println("Task 1 running"),
            () -> System.out.println("Task 2 running"),
            () -> System.out.println("Task 3 running")
        );

        MultiExecutor executor = new MultiExecutor(tasks);
        executor.executeAll();
    }
}