package personal.gokul2411s.multithreading.barriers;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// TODO: Handle exception handling.
final class TwoPhaseBarrier implements Barrier {

  private final int nThreads;

  private final Lock lock = new ReentrantLock(true);

  /**
   * Arrival gate starts out closed.
   */
  private final Gate arrivalGate = Gate.closedGate();

  /**
   * Departure gate starts out open.
   */
  private final Gate departureGate = Gate.openedGate();

  private int nThreadsTransit = 0;

  TwoPhaseBarrier(int nThreads) {
    if (nThreads <= 0) {
      throw new IllegalArgumentException(
          "Barriers make sense only for a positive number of threads");
    }
    this.nThreads = nThreads;
  }

  @Override
  public void await() {
    arrive();
    depart();
  }

  private void arrive() {

    // START - CRITICAL SECTION
    // Critical section ensures only the last thread to reach the arrival gate opens the arrival gate.
    lock.lock();
    try {
      if (++nThreadsTransit == nThreads) {
        arrivalGate.open();

        try {
          // Threads should't go past the departure gate while some are still trying to get past the
          // arrival gate.
          departureGate.close();
        } catch (InterruptedException e) {
          // Won't happen.
        }
      }
    } finally {
      lock.unlock();
    }
    // END - CRITICAL SECTION

    try {
      // Each thread walks through the gate and then holds the door open for the next thread.
      // With a total of N threads, the departure gate opens (N + 1) times.
      arrivalGate.waitGoThroughAndThenClose();
      arrivalGate.open();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void depart() {

    // START - CRITICAL SECTION
    // Critical section ensures only the last thread to reach the departure gate opens the departure gate.
    lock.lock();
    try {
      if (--nThreadsTransit == 0) {
        departureGate.open();

        try {
          // Threads should't go past the arrival gate while some are still trying to get past the
          // departure gate.
          arrivalGate.close();
        } catch (InterruptedException e) {
          // Won't happen.
        }
      }
    } finally {
      lock.unlock();
    }
    // END - CRITICAL SECTION

    try {
      // Each thread walks through the gate and then holds the door open for the next thread.
      // With a total of N threads, the departure gate opens (N + 1) times.
      departureGate.waitGoThroughAndThenClose();
      departureGate.open();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * A readable wrapper on {@link Semaphore} in order to represent opening, closing and walking
   * through a gate.
   *
   * <p>Do not call {@link #open()} on an open gate.
   * <p>Do not call {@link #close()} on a closed gate. Instead use {@link
   * #waitGoThroughAndThenClose()}, which reads better.
   */
  private static class Gate {

    private final Semaphore impl;

    private Gate(boolean opened) {
      impl = new Semaphore(opened ? 1 : 0);
    }

    static Gate openedGate() {
      return new Gate(true);
    }

    static Gate closedGate() {
      return new Gate(false);
    }

    void open() {
      impl.release();
    }

    void close() throws InterruptedException {
      impl.acquire();
    }

    void waitGoThroughAndThenClose() throws InterruptedException {
      impl.acquire();
    }
  }
}
