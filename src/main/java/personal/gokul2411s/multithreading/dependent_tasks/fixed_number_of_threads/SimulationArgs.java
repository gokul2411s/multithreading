package personal.gokul2411s.multithreading.dependent_tasks.fixed_number_of_threads;

import lombok.Data;
import org.kohsuke.args4j.Option;

@Data
final class SimulationArgs {

  @Option(name="-p", usage="parallelism")
  private int parallelism = 4;

  @Option(name="-n", usage="number of tasks")
  private int numTasks = 10;
}
