package personal.gokul2411s.multithreading.producer_consumer;

import lombok.Data;
import org.kohsuke.args4j.Option;

@Data
final class SimulationArgs {

  @Option(name="-p", usage="number of producers")
  private int numProducers = 2;

  @Option(name="-c", usage="number of consumers")
  private int numConsumers = 2;

  @Option(name="-b", usage="buffer bound, < 0 implies unbounded.")
  private int bufferBound = -1;
}
