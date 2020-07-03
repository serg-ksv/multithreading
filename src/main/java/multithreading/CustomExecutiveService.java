package multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CustomExecutiveService {
    private final int numberOfThreads;
    private final List<Long> elements;

    public CustomExecutiveService(int numberOfThreads, List<Long> elements) {
        this.numberOfThreads = numberOfThreads;
        this.elements = elements;
    }

    public List<Callable<Long>> splitTask() {
        List<Callable<Long>> callableTasks = new ArrayList<>();
        int numberOfTasks = elements.size() / numberOfThreads;
        for (int i = 0; i < numberOfThreads; i++) {
            int fromIndex = numberOfTasks * i;
            int toIndex = numberOfTasks * (i + 1);
            var callableTask = new CustomCallableTask(elements.subList(fromIndex, toIndex));
            callableTasks.add(callableTask);
        }
        return callableTasks;
    }

    public List<Future<Long>> getFutureList(List<Callable<Long>> callableTasks) {
        var executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Future<Long>> futureList;
        try {
            futureList = executorService.invokeAll(callableTasks);
        } catch (InterruptedException e) {
            throw new RuntimeException("Can't invoke callable tasks", e);
        }
        executorService.shutdown();
        return futureList;
    }

    public long getSumOfElements(List<Future<Long>> futureList) {
        long sumOfElements = 0;
        for (var future : futureList) {
            try {
                sumOfElements += future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Can't retrieve elements from futures", e);
            }
        }
        return sumOfElements;
    }
}
