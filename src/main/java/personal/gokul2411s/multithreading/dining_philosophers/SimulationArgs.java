package personal.gokul2411s.multithreading.dining_philosophers;

import lombok.Data;
import org.kohsuke.args4j.Option;

@Data
final class SimulationArgs {

  @Option(name="-n", usage="number of philosophers")
  private int numPhilosophers = 5;

  @Option(name="-t", usage="max thinking time in millis")
  private int maxThinkingTimeMillis = 10000;

  @Option(name="-e", usage="max eating time in millis")
  private int maxEatingTimeMillis = 10000;
}
