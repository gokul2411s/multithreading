package personal.gokul2411s.multithreading.producer_consumer;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

final class BoundedBuffer<T> implements Buffer<T> {

  private final Lock lock = new ReentrantLock(true);
  private final Semaphore notEmpty = new Semaphore(0);
  private final Semaphore notFull;

  private final Queue<T> items = new ArrayDeque<>();

  BoundedBuffer(int bound) {
    if (bound < 0) {
      throw new IllegalArgumentException("Negative bound for bounded buffer");
    }
    notFull = new Semaphore(bound);
  }

  @Override
  public T get() throws InterruptedException {
    notEmpty.acquire();
    lock.lock();
    try {
      T item = items.remove();
      notFull.release();
      return item;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void put(T item) throws InterruptedException {
    notFull.acquire();
    lock.lock();
    try {
      items.add(item);
      notEmpty.release();
    } finally {
      lock.unlock();
    }
  }
}
