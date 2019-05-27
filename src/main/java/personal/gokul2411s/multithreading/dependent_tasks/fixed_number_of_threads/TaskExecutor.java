package personal.gokul2411s.multithreading.dependent_tasks.fixed_number_of_threads;

import personal.gokul2411s.multithreading.dependent_tasks.Task;

final class TaskExecutor implements Runnable {

  private final TaskExecution taskExecution;

  TaskExecutor(TaskExecution taskExecution) {
    this.taskExecution = taskExecution;
  }

  @Override
  public void run() {

    while (true) {
      try {
        Task task = taskExecution.getNextTask();
        task.execute();
        taskExecution.markTaskCompleted(task);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
  }
}
