package personal.gokul2411s.multithreading.dining_philosophers;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.Data;

@Data
final class Fork {

  private final int index;
  private final Lock lock = new ReentrantLock();

  void pickup() {
    lock.lock();
  }

  void putdown() {
    lock.unlock();
  }
}
