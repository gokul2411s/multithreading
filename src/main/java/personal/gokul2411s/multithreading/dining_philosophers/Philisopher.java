package personal.gokul2411s.multithreading.dining_philosophers;

import java.util.Random;
import personal.gokul2411s.multithreading.logging.LoggingQueue;

final class Philisopher extends Thread {

  private static final Random RANDOM = new Random();

  private final int index;

  private final Fork firstFork;

  private final Fork secondFork;

  private final int maxThinkingTimeMillis;

  private final int maxEatingTimeMillis;

  Philisopher(
      int index,
      Fork leftFork,
      Fork rightFork,
      int maxThinkingTimeMillis,
      int maxEatingTimeMillis) {
    this.index = index;
    // doing this ensures that the graph of dependencies on forks does not have a
    // cycle. also, the algorithm is roughly symmetrical for all parties, therefore
    // this is as fair as it gets.
    if (index % 2 == 0) {
      firstFork = leftFork;
      secondFork = rightFork;
    } else {
      firstFork = rightFork;
      secondFork = leftFork;
    }

    this.maxThinkingTimeMillis = maxThinkingTimeMillis;
    this.maxEatingTimeMillis = maxEatingTimeMillis;
  }

  @Override
  public void run() {

    while (true) {
      try {
        logThinking();
        firstFork.pickup();
        try {
          logPickup(firstFork);
          secondFork.pickup();
          try {
            logPickup(secondFork);
            logEating();
          } finally {
            secondFork.putdown();
            logPutdown(secondFork);
          }
        } finally {
          firstFork.putdown();
          logPutdown(firstFork);
        }
      } catch (InterruptedException e) {
        interrupt();
        break;
      }
    }
  }

  private void logThinking() throws InterruptedException {
    logAction("thinking", maxThinkingTimeMillis);
  }

  private void logEating() throws InterruptedException {
    logAction("eating", maxEatingTimeMillis);
  }

  private void logAction(String action, int maxActionTimeMillis) throws InterruptedException {
    LoggingQueue.INSTANCE.put("Philosopher : " + index + " " + action);
    Thread.sleep(RANDOM.nextInt(maxActionTimeMillis));
  }

  private void logPickup(Fork fork) throws InterruptedException {
    LoggingQueue.INSTANCE.put(
        "Philosopher : " + index + " picked up fork: " + fork.getIndex());
  }

  private void logPutdown(Fork fork) throws InterruptedException {
    LoggingQueue.INSTANCE.put(
        "Philosopher : " + index + " put down up fork: " + fork.getIndex());
  }

}
