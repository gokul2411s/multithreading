package personal.gokul2411s.multithreading.dependent_tasks.fixed_number_of_threads;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import personal.gokul2411s.multithreading.dependent_tasks.Task;
import personal.gokul2411s.multithreading.logging.LoggingQueue;

final class TaskExecution {

  // All maps, sets and queues below are concurrent.

  private final Map<Task, Set<Task>> taskBlockers;
  private final Map<Task, Set<Task>> tasksBlockedBy;
  private final BlockingQueue<Task> unblockedTasks;
  private final Set<Task> pendingTasks;

  TaskExecution(Map<Task, Set<Task>> taskBlockers) {

    // Deep clone this, since we don't want to mutate the original data structure.
    this.taskBlockers = new ConcurrentHashMap<>();
    for (Map.Entry<Task, Set<Task>> entry : taskBlockers.entrySet()) {

      Set<Task> blockersCopy = Collections.newSetFromMap(new ConcurrentHashMap<>());
      blockersCopy.addAll(entry.getValue());
      this.taskBlockers.put(entry.getKey(), blockersCopy);
    }

    // Create and initialize auxiliary structures.
    tasksBlockedBy = new ConcurrentHashMap<>();
    unblockedTasks = new ArrayBlockingQueue<>(taskBlockers.size());
    pendingTasks = Collections.newSetFromMap(new ConcurrentHashMap<>());
    initAuxiliaryStructures();
  }

  private void initAuxiliaryStructures() {
    for (Map.Entry<Task, Set<Task>> entry : taskBlockers.entrySet()) {
      Task task = entry.getKey();
      Set<Task> blockers = entry.getValue();
      for (Task blocker : blockers) {
        Set<Task> blockedByBlocker = tasksBlockedBy.get(blocker);
        if (blockedByBlocker == null) {
          blockedByBlocker = Collections.newSetFromMap(new ConcurrentHashMap<>());
          tasksBlockedBy.put(blocker, blockedByBlocker);
        }
        blockedByBlocker.add(task);
      }

      if (blockers.isEmpty()) {
        unblockedTasks.add(task);
      }
    }

    this.pendingTasks.addAll(this.taskBlockers.keySet());
  }

  Task getNextTask() throws InterruptedException {
    return unblockedTasks.take();
  }

  void markTaskCompleted(Task completedTask) throws InterruptedException {
    if (!this.pendingTasks.remove(completedTask)) {
      return;
    }

    Set<Task> potentiallyUnblockedTasks = tasksBlockedBy.get(completedTask);
    if (potentiallyUnblockedTasks == null) {
      return;
    }
    for (Task potentiallyUnblockedTask : potentiallyUnblockedTasks) {
      Set<Task> blockersForPotentiallyUnblockedTask = taskBlockers.get(potentiallyUnblockedTask);
      blockersForPotentiallyUnblockedTask.remove(completedTask);
      if (blockersForPotentiallyUnblockedTask.isEmpty()) {
        unblockedTasks.add(potentiallyUnblockedTask);
        LoggingQueue.INSTANCE.put("Unblocked task: " + potentiallyUnblockedTask);
      }
    }
  }

  boolean pending() {
    return !this.pendingTasks.isEmpty();
  }
}
