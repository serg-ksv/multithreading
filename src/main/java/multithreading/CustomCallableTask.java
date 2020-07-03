package multithreading;

import java.util.List;
import java.util.concurrent.Callable;

public class CustomCallableTask implements Callable<Long> {
    private final List<Long> elements;

    public CustomCallableTask(List<Long> elements) {
        this.elements = elements;
    }

    @Override
    public Long call() {
        return elements.stream()
                .mapToLong(Long::longValue)
                .sum();
    }
}
