package personal.gokul2411s.multithreading.producer_consumer;

interface Buffer<T> {

  T get() throws InterruptedException;

  void put(T item) throws InterruptedException;

}
