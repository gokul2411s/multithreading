package personal.gokul2411s.multithreading.locks;

/**
 * Generalization of Peterson's lock for N threads, which works with any N.
 */
final class FilterLock {

  private final int[] levels;
  private final int[] victim;

  FilterLock(int N) {
    levels = new int[N];
    victim = new int[N];
  }

  void lock() {
    int tid = threadId();
    for (int level = 1; level < levels.length; level++) {
      levels[tid] = level; // showing willingness to cross level.
      victim[level] = tid; // allowing other threads to cross level.

      while (true) {
        boolean canProgress = true; // hopeful.
        for (int otid = 0; otid < levels.length; otid++) {
          if (otid == tid) {
            continue;
          }
          if (levels[otid] >= level && victim[level] == tid) {
            canProgress = false;
            break;
          }
        }

        if (canProgress) {
          break;
        }
      }
    }
  }

  void unlock() {
    levels[threadId()] = 0;
  }


  private int threadId() {
    // assumes thread name to be one of "0", "1", ... "N - 1".
    return Integer.valueOf(Thread.currentThread().getName());
  }
}
