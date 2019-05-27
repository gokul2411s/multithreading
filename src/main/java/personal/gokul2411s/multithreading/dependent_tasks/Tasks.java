package personal.gokul2411s.multithreading.dependent_tasks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public final class Tasks {

  private static final Random RANDOM = new Random();
  private static final double DEPENDENCY_PROBABILITY = 0.2;

  /**
   * Generates a graph of tasks in adjecency list representation.
   *
   * @param numTasks number of tasks to generate.
   * @return map, where key is a task and the value is a set of tasks that it depends on.
   */
  public static Map<Task, Set<Task>> generate(int numTasks) {

    Map<Integer, Task> tasks = new HashMap<>();
    Map<Task, Set<Task>> taskBlockers = new HashMap<>();
    for (int taskNum = numTasks - 1; taskNum >= 0; taskNum--) {
      Task task = new SimpleTask(taskNum);
      Set<Task> blockers = new HashSet<>();
      for (int blockedByTaskNum = taskNum + 1; blockedByTaskNum < numTasks; blockedByTaskNum++) {
        if (RANDOM.nextInt(10000) < DEPENDENCY_PROBABILITY * 10000) {
          blockers.add(tasks.get(blockedByTaskNum));
        }
      }
      tasks.put(taskNum, task);
      taskBlockers.put(task, blockers);
    }

    return taskBlockers;
  }

  private Tasks() {
  }
}
