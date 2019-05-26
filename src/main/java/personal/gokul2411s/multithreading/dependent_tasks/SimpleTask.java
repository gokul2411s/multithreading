package personal.gokul2411s.multithreading.dependent_tasks;

import java.util.Random;
import personal.gokul2411s.multithreading.logging.LoggingQueue;

final class SimpleTask implements Task {

  private static final Random RANDOM = new Random();

  /**
   * Should be unique. No two {@link SimpleTask} objects should have the same ID.
   */
  private final int id;

  SimpleTask(int id) {
    this.id = id;
  }

  @Override
  public void execute() {
    try {
      Thread.sleep(1 + RANDOM.nextInt(1000));
      LoggingQueue.INSTANCE.put("I am task number: " + id);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  @Override
  public boolean equals(Object o) {
    if ((o == null) || !(o.getClass().equals(SimpleTask.class))) {
      return false;
    }
    SimpleTask other = (SimpleTask) o;
    if (id != other.id) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(id);
  }

  @Override
  public String toString() {
    return "T" + id;
  }
}
