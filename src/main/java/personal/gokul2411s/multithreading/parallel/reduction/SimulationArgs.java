package personal.gokul2411s.multithreading.parallel.reduction;

import lombok.Data;
import org.kohsuke.args4j.Option;

@Data
final class SimulationArgs {

  @Option(name="-n", usage="number of items")
  private int numItems = 100000000;

  @Option(name="-p", usage="parallelism")
  private int parallelism = 10;

}
