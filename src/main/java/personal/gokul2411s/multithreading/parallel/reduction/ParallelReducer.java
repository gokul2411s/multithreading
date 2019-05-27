package personal.gokul2411s.multithreading.parallel.reduction;

import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.function.BiFunction;

final class ParallelReducer<T> extends RecursiveTask<T> {

  private static final int SEQUENTIAL_THRESHOLD = 10;

  private final List<T> items;
  private final Range range;
  private final BiFunction<T, T, T> binaryAssociativeOperator;
  private final T sentinel;

  ParallelReducer(
      List<T> items,
      Range range,
      BiFunction<T, T, T> binaryAssociativeOperator,
      T sentinel) {
    this.items = items;
    this.range = range;
    this.binaryAssociativeOperator = binaryAssociativeOperator;
    this.sentinel = sentinel;
  }

  @Override
  public T compute() {

    if (range.getCount() <= SEQUENTIAL_THRESHOLD) {
      T out = sentinel;
      for (int index = range.getStartIndex(); index < range.getEndIndex(); index++) {
        out = binaryAssociativeOperator.apply(out, items.get(index));
      }
      return out;
    }

    int leftRangeCount = range.getCount() / 2;
    Range leftRange =
        Range.builder()
            .startIndex(range.getStartIndex())
            .count(range.getCount() / 2)
            .build();
    ParallelReducer<T> leftRangeReducer =
        new ParallelReducer<>(
            items, leftRange, binaryAssociativeOperator, sentinel);
    leftRangeReducer.fork();

    Range rightRange =
        Range.builder()
            .startIndex(leftRange.getEndIndex())
            .count(range.getCount() - leftRangeCount)
            .build();
    ParallelReducer<T> rightRangeReducer =
        new ParallelReducer<>(
            items, rightRange, binaryAssociativeOperator, sentinel);

    T rightRangeResult = rightRangeReducer.compute();
    T leftRangeResult = leftRangeReducer.join();
    return binaryAssociativeOperator.apply(leftRangeResult, rightRangeResult);
  }
}
