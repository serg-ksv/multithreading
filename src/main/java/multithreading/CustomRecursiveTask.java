package multithreading;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class CustomRecursiveTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 10000;
    private final List<Long> elements;

    public CustomRecursiveTask(List<Long> elements) {
        this.elements = elements;
    }

    @Override
    protected Long compute() {
        if (elements.size() > THRESHOLD) {
            return ForkJoinTask.invokeAll(createSubTasks()).stream()
                    .mapToLong(ForkJoinTask::join)
                    .sum();
        }
        return elements.stream().reduce(0L, Long::sum);
    }
 
    private Collection<CustomRecursiveTask> createSubTasks() {
        List<CustomRecursiveTask> dividedTasks = new ArrayList<>();
        int middle = elements.size() / 2;
        dividedTasks.add(new CustomRecursiveTask(elements.subList(0, middle)));
        dividedTasks.add(new CustomRecursiveTask(elements.subList(middle, elements.size())));
        return dividedTasks;
    }
}
