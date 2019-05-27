package personal.gokul2411s.multithreading.dependent_tasks.thread_per_task;

import lombok.Data;
import org.kohsuke.args4j.Option;

@Data
final class SimulationArgs {

  @Option(name="-n", usage="number of tasks")
  private int numTasks = 10;
}
