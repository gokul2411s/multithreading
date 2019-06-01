package personal.gokul2411s.multithreading.producer_consumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import personal.gokul2411s.multithreading.logging.LoggingQueue;

public final class Simulation {

  public static void main(String[] args) {
    SimulationArgs simulationArgs = parseArgs(args);

    LoggingQueue.INSTANCE.activateLogging();

    Buffer<Integer> buffer;
    if (simulationArgs.getBufferBound() < 0) {
      buffer = new UnboundedBuffer<>();
    } else {
      buffer = new BoundedBuffer<>(simulationArgs.getBufferBound());
    }

    ExecutorService executors =
        Executors.newFixedThreadPool(
            simulationArgs.getNumProducers() + simulationArgs.getNumConsumers());
    for (int i = 0; i < simulationArgs.getNumProducers(); i++) {
      executors.submit(new Producer(i, buffer));
    }
    for (int i = 0; i < simulationArgs.getNumConsumers(); i++) {
      executors.submit(new Consumer(i, buffer));
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
