package personal.gokul2411s.multithreading.dependent_tasks.thread_per_task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import personal.gokul2411s.multithreading.dependent_tasks.Tasks;
import personal.gokul2411s.multithreading.dependent_tasks.Task;

public class Simulation {

  public static void main(String[] args) throws InterruptedException {

    Map<Task, List<Task>> taskBlockers = Tasks.generate(10);
    Map<Task, TaskThread> taskThreads = new HashMap<>();
    for (Task task : taskBlockers.keySet()) {
      taskThreads.put(task, new TaskThread(task));
    }
    System.out.println("Dependencies...");
    for (Map.Entry<Task, List<Task>> entry : taskBlockers.entrySet()) {
      Task task = entry.getKey();
      System.out.print(task + ": ");
      TaskThread taskThread = taskThreads.get(task);
      for (Task blocker : entry.getValue()) {
        System.out.print(blocker + ", ");
        taskThread.addBlocker(taskThreads.get(blocker));
      }
      System.out.println();
    }

    for (TaskThread taskThread : taskThreads.values()) {
      taskThread.start();
    }
    for (TaskThread taskThread : taskThreads.values()) {
      taskThread.join();
    }
  }

  private Simulation() {
  }

}
