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

  private final Thread thread;

  LoggingQueue() {
    this.logs = new ArrayBlockingQueue<>(10000);
    this.thread =
        new Thread(() -> {
          while (true) {
            try {
              System.out.println(this.logs.take());
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              break;
            }
          }
        });
    this.thread.start();
  }

  public void put(String str) throws InterruptedException {
    logs.put(str);
  }
}
