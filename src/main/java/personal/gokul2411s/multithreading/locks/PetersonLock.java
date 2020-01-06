package personal.gokul2411s.multithreading.locks;

/**
 * Works only for two threads.
 */
final class PetersonLock {

  private final boolean[] flag = new boolean[2];
  private volatile int victim;

  void lock() {
    int tid = threadId();
    flag[tid] = true; // showing willingness to enter critical section.
    victim = tid; // allowing other thread to enter it.

    while (flag[1 - tid] && victim == tid) {}
  }

  void unlock() {
    flag[threadId()] = false;
  }

  private int threadId() {
    // assumes thread name to be "0" or "1".
    return Integer.valueOf(Thread.currentThread().getName());
  }

}
