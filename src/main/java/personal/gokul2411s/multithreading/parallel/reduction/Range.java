package personal.gokul2411s.multithreading.parallel.reduction;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
final class Range {

  /**
   * Inclusive start index.
   */
  private final int startIndex;

  private final int count;

  /**
   * Exclusive end index.
   */
  public int getEndIndex() {
    return startIndex + count;
  }
}
