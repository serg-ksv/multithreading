package multithreading;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Main {
    private static final int SIZE = 1_000_000;
    private static final int NUMBER_OF_THREADS = 4;

    public static void main(String[] args) throws Exception {
        List<Long> elements = LongStream.range(0, SIZE)
                .boxed()
                .collect(Collectors.toList());

        var customExecutiveService = new CustomExecutiveService(NUMBER_OF_THREADS, elements);
        List<Callable<Long>> tasks = customExecutiveService.splitTask();
        List<Future<Long>> futureList = customExecutiveService.getFutureList(tasks);
        long sumOfElements = customExecutiveService.getSumOfElements(futureList);
        System.out.println(sumOfElements);

        var forkJoinPool = ForkJoinPool.commonPool();
        var customRecursiveTask = new CustomRecursiveTask(elements);
        long forkJoinSum = forkJoinPool.invoke(customRecursiveTask);
        System.out.println(forkJoinSum);
    }
}
