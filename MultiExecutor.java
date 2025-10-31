import java.util.*;

public class MultiExecutor {
    private List<Runnable> tasks;

    public MultiExecutor(List<Runnable> tasks) {
        this.tasks = tasks;
    }

    public void executeAll() {
        for (Runnable task : tasks) {
            Thread thread = new Thread(task);
            thread.start();
        }
    }
}
