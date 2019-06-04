package personal.gokul2411s.multithreading.dining_philosophers;

import java.util.ArrayList;
import java.util.List;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import personal.gokul2411s.multithreading.logging.LoggingQueue;

public final class Simulation {

  public static void main(String[] args) {
    SimulationArgs simulationArgs = parseArgs(args);

    LoggingQueue.INSTANCE.activateLogging();

    int N = simulationArgs.getNumPhilosophers();

    List<Philisopher> philisophers = new ArrayList<>();
    List<Fork> forks = new ArrayList<>();
    for (int i = 0; i < N; i++) {
      forks.add(new Fork(i));
    }
    for (int i = 0; i < N; i++) {
      Philisopher philisopher =
          new Philisopher(
              i,
              forks.get(i),
              forks.get((i + 1) %  N),
              simulationArgs.getMaxThinkingTimeMillis(),
              simulationArgs.getMaxEatingTimeMillis());
      philisopher.start();
      philisophers.add(philisopher);
    }
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

}
