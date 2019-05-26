package personal.gokul2411s.multithreading.dependent_tasks.thread_per_task;

import java.util.ArrayList;
import java.util.List;
import personal.gokul2411s.multithreading.dependent_tasks.Task;
import personal.gokul2411s.multithreading.logging.LoggingQueue;

final class TaskThread extends Thread {

  private final Task task;

  private final List<TaskThread> blockers;

  TaskThread(Task task) {
    this.task = task;
    this.blockers = new ArrayList<>();
  }

  void addBlocker(TaskThread blocker) {
    blockers.add(blocker);
  }

  @Override
  public void run() {
    for (TaskThread blocker : blockers) {
      try {
        blocker.join();
        LoggingQueue.INSTANCE.put(task + " completed waiting for " + blocker.task);
      } catch (InterruptedException e) {
        interrupt();
      }
    }

    task.execute();
  }
}
