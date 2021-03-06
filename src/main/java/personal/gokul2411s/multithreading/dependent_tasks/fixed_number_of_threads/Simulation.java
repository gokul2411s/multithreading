package personal.gokul2411s.multithreading.dependent_tasks.fixed_number_of_threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
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

    // 3] Create a task execution object for keeping track of pending and yet to be picked up tasks.
    TaskExecution taskExecution = new TaskExecution(taskBlockers);

    // 4] Start task execution.
    System.out.println("-------------------------");
    System.out.println("Showing task execution");
    System.out.println("-------------------------");
    ExecutorService threadPool = Executors.newFixedThreadPool(simulationArgs.getParallelism());
    List<Future<?>> taskExecutorFutures = new ArrayList<>();
    for (int i = 0; i < simulationArgs.getParallelism(); i++) {
      taskExecutorFutures.add(threadPool.submit(new TaskExecutor(taskExecution)));
    }

    // 5] Gracefully terminate once all tasks are complete.
    while (taskExecution.pending()) {
      Thread.sleep(10);
    }
    for (Future<?> taskExecutorFuture : taskExecutorFutures) {
      taskExecutorFuture.cancel(true);
    }
    threadPool.shutdown();
    threadPool.awaitTermination(10, TimeUnit.SECONDS);
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
