package personal.gokul2411s.multithreading.dependent_tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class Tasks {

  private static final Random RANDOM = new Random();
  private static final double DEPENDENCY_PROBABILITY = 0.2;

  /**
   * Generates a graph of tasks in adjecency list representation.
   *
   * @param numTasks number of tasks to generate.
   * @return map, where key is a task and the value is a list of tasks that it depends on.
   */
  public static Map<Task, List<Task>> generate(int numTasks) {

    Map<Integer, Task> tasks = new HashMap<>();
    Map<Task, List<Task>> taskBlockers = new HashMap<>();
    for (int taskNum = numTasks - 1; taskNum >= 0; taskNum--) {
      Task task = new SimpleTask(taskNum);
      List<Task> blockers = new ArrayList<>();
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
