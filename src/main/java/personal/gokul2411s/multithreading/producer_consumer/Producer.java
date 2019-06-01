package personal.gokul2411s.multithreading.producer_consumer;

import java.util.Random;
import personal.gokul2411s.multithreading.logging.LoggingQueue;

final class Producer implements Runnable {

  private static final Random RANDOM = new Random();

  private final int id;
  private final Buffer<Integer> buffer;

  Producer(int id, Buffer<Integer> buffer) {
    this.id = id;
    this.buffer = buffer;
  }

  @Override
  public void run() {
    while (true) {
      int item = RANDOM.nextInt(10);
      try {
        buffer.put(item);
        LoggingQueue.INSTANCE.put("Producer: " + id + " produced item: " + item);
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
  }
}
