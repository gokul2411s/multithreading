package personal.gokul2411s.multithreading.dependent_tasks.thread_per_task;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import personal.gokul2411s.multithreading.dependent_tasks.Task;
import personal.gokul2411s.multithreading.dependent_tasks.Tasks;
import personal.gokul2411s.multithreading.logging.LoggingQueue;

public final class Simulation {

  public static void main(String[] args) throws InterruptedException {
    SimulationArgs simulationArgs = parseArgs(args);

    // 1] Activate serialized logging.
    LoggingQueue.INSTANCE.activateLogging();

    // 2] Randomly generate a set of tasks and blockers for these tasks.
    Map<Task, Set<Task>> taskBlockers = Tasks.generate(simulationArgs.getNumTasks());
    System.out.println("-------------------------");
    System.out.println("Task dependency graph, showing tasks which block a given task...");
    System.out.println("-------------------------");
    printTaskGraph(taskBlockers);

    // 3] Create a separate thread, one for each task.
    Map<Task, TaskThread> taskThreads = new HashMap<>();
    for (Task task : taskBlockers.keySet()) {
      taskThreads.put(task, new TaskThread(task));
    }

    // 4] Wire up the task dependencies in the form of thread dependencies.
    for (Map.Entry<Task, Set<Task>> entry : taskBlockers.entrySet()) {
      TaskThread taskThread = taskThreads.get(entry.getKey());
      for (Task blocker : entry.getValue()) {
        taskThread.addBlocker(taskThreads.get(blocker));
      }
    }

    // 5] Start the task threads so that blocking threads start before blocked threads.
    // This is necessary. Starting all the task threads can lead to a scenario
    // where a thread T1 starts before a thread T2, which should block T1 but does
    // not because T1 called T2.join() even before T2 started, and T2.join()
    // immediately returned.
    Queue<Task> unblockedTasks = new ArrayDeque<>();
    Map<Task, Set<Task>> tasksBlockedBy = new HashMap<>();
    for (Map.Entry<Task, Set<Task>> entry : taskBlockers.entrySet()) {
      Task task = entry.getKey();
      Set<Task> blockers = entry.getValue();
      for (Task blocker : blockers) {
        Set<Task> blockedByBlocker = tasksBlockedBy.get(blocker);
        if (blockedByBlocker == null) {
          blockedByBlocker = new HashSet<>();
          tasksBlockedBy.put(blocker, blockedByBlocker);
        }
        blockedByBlocker.add(task);
      }

      if (blockers.isEmpty()) {
        unblockedTasks.add(task);
      }
    }

    System.out.println("-------------------------");
    System.out.println("Showing task execution");
    System.out.println("-------------------------");
    while (!unblockedTasks.isEmpty()) {
      Task task = unblockedTasks.remove();
      TaskThread taskThread = taskThreads.get(task);
      taskThread.start();

      Set<Task> freedTasks = tasksBlockedBy.get(task);
      if (freedTasks == null) {
        continue;
      }
      for (Task freedTask : freedTasks) {
        Set<Task> blockersForFreedTask = taskBlockers.get(freedTask);
        blockersForFreedTask.remove(task);
        if (blockersForFreedTask.isEmpty()) {
          unblockedTasks.add(freedTask);
        }
      }
    }

    // 6] Deactivate logging after all task threads are done, so that we can shutdown.
    for (TaskThread taskThread : taskThreads.values()) {
      taskThread.join();
    }
    LoggingQueue.INSTANCE.deactivateLogging();
  }

  private static SimulationArgs parseArgs(String[] args) {
    SimulationArgs simulationArgs = new SimulationArgs();
    CmdLineParser cmdLineParser = new CmdLineParser(simulationArgs);
    try {
      cmdLineParser.parseArgument(args);
    } catch (CmdLineException e) {
      cmdLineParser.printUsage(System.err);
      System.exit(1);
    }

    return simulationArgs;
  }

  private static void printTaskGraph(Map<Task, Set<Task>> graph) {
    for (Map.Entry<Task, Set<Task>> entry : graph.entrySet()) {
      System.out.print(entry.getKey() + ": ");
      for (Task blocker : entry.getValue()) {
        System.out.print(blocker + ",");
      }
      System.out.println();
    }
  }

  private Simulation() {
  }
}
