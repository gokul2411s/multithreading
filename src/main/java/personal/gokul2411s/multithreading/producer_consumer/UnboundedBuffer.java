package personal.gokul2411s.multithreading.producer_consumer;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

final class UnboundedBuffer<T> implements Buffer<T> {

  private final Lock lock = new ReentrantLock(true);
  private final Semaphore notEmpty = new Semaphore(0);

  private final Queue<T> items = new ArrayDeque<>();

  @Override
  public T get() throws InterruptedException {
    notEmpty.acquire();
    lock.lock();
    try {
      return items.remove();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void put(T item) throws InterruptedException {
    lock.lock();
    try {
      items.add(item);
      notEmpty.release();
    } finally {
      lock.unlock();
    }
  }
}
