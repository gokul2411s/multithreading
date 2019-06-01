package personal.gokul2411s.multithreading.producer_consumer;

import personal.gokul2411s.multithreading.logging.LoggingQueue;

final class Consumer implements Runnable {

  private final int id;
  private final Buffer<Integer> buffer;

  Consumer(int id, Buffer<Integer> buffer) {
    this.id = id;
    this.buffer = buffer;
  }

  @Override
  public void run() {
    while (true) {
      try {
        LoggingQueue.INSTANCE.put("Consumer: " + id + " consumed item: " + buffer.get());
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
  }
}
