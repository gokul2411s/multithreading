package personal.gokul2411s.multithreading.logging;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * A singleton logging queue into which multiple threads can put messages so in order to serialize
 * message order onto STDOUT.
 */
public enum LoggingQueue {

  INSTANCE();

  private final BlockingQueue<String> logs;

  private Thread thread = null;

  LoggingQueue() {
    logs = new ArrayBlockingQueue<>(10000, true);
  }

  /**
   * Activates logging from a separate thread.
   *
   * <p>Not calling this method will queue up logs. Therefore, prefer to call this method from
   * the main thread at the start of the application.
   */
  public synchronized void activateLogging() {
    if (thread != null) {
      return;
    }
    thread = new LoggingThread(logs);
    thread.start();
  }

  /**
   * Deactivates logging on the active logging thread, by turning off the thread.
   *
   * <p>If this method is not called after calling {@link #activateLogging()}, then
   * the application will run forever. Prefer to call this at the end of the main thread.
   */
  public synchronized void deactivateLogging() {
    if (thread != null) {
      thread.interrupt();
      thread = null;
    }
  }

  public void put(String str) throws InterruptedException {
    logs.put(str);
  }

  private static final class LoggingThread extends Thread {

    private final BlockingQueue<String> logs;

    LoggingThread(BlockingQueue<String> logs) {
      this.logs = logs;
    }

    @Override
    public void run() {
      while (true) {
        try {
          System.out.println(logs.take());
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          System.out.flush();
          break;
        }
      }
    }
  }
}
