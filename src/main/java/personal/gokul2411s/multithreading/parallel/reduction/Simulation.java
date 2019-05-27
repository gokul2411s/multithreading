package personal.gokul2411s.multithreading.parallel.reduction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public final class Simulation {

  private static final Random RANDOM = new Random();

  public static void main(String[] args) {
    SimulationArgs simulationArgs = parseArgs(args);

    // Using short rather than integer to save on memory.
    List<Short> integers = new ArrayList<>(simulationArgs.getNumItems());
    for (int i = 0; i < simulationArgs.getNumItems(); i++) {
      integers.add((short) RANDOM.nextInt(100));
    }

    ForkJoinPool threadPool = new ForkJoinPool(simulationArgs.getParallelism());
    ParallelReducer<Short> reducer =
        new ParallelReducer<>(
            integers,
            Range.builder().startIndex(0).count(integers.size()).build(),
            (integer, integer2) -> (short) (integer + integer2),
            (short) 0);

    long startTime = System.currentTimeMillis();
    threadPool.invoke(reducer);
    System.out.println(String.format("Took %d ms to sum", System.currentTimeMillis() - startTime));
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

  private Simulation() {
  }
}
