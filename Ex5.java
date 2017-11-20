import java.util.concurrent.atomic.*;
import java.util.concurrent.*;
import java.util.concurrent.ThreadLocalRandom;

// exercise five for CS 1530
public class Ex5{

  // used Atomic.java from concurrency sample code to Start/help begin this
  public static AtomicLong _a = new AtomicLong(0);

  // creating an array of threads to begin
  public Thread[] _t;

  // function used to calculate monte carlo example
  // detailed description of this:  http://mathfaculty.fullerton.edu/mathews/n2003/montecarlopimod.html
  public void monteCarlo(long numThreads, long totIters){
    // set thread size (type cast)
    _t = new Thread[(int) numThreads];

    // long value to be used later
    final long iters = totIters/numThreads;

    // all from Atomic.java -- creating dem threads
    for (int j=0; j<_t.length; j++){
      _t[j] = new Thread(() ->{
        int c1 = 0;
        int c2 = 0;
        while (c1++ < iters){
          // generating random values for x and y..this values MUST be between 0 and 1
          double x = ThreadLocalRandom.current().nextDouble(1);
          double y = ThreadLocalRandom.current().nextDouble(1);
          // squaring x to use later in formula
          double squareX = x*x;
          // squaring y to use later in formula
          double squareY = y*y;
          // calculating z to use for our if statement must satisify x^2+y^2<1
          double z = squareX * squareY;

          // testing to see if the poin satisifies and is in the circle
          if (z < 1){
            // update count
            c2++;
          }
          else {
            // self-explanatory
            System.out.println("Z is > 1");
          }
        }

        _a.addAndGet(c2);
      });
    }

    // again - taken from Atomic.java - starting the threads here
    for (int j=0; j<_t.length; j++){
      _t[j].start();
    }

    // joining the threads here
    for (int j=0; j<_t.length; j++){
      try{
        _t[j].join();
      }catch (Exception ex){ }
    }

    // t = total iterations, c = circle points
    long t = totIters;
    long c = _a.get();

    // ratio = circle points/total iterations - can't forget to cast both of these
    double ratio = ((double) c/(double)t);
    // according to formula you have to multiply by 4 for piVal (four different quadrants in a circle)
    double piVal = ratio*4;

    // Print statement with results
    System.out.println("Total: " + t + '\n' + "Inside: " + c + '\n' + "Ratio: " + ratio +'\n' + "Pi: " + piVal);

  }

    public static void main(String[] args){

      // Values entered that dictate the results - exercise description says they must be long values
      long numThreads = Long.parseLong(args[0]);
      long totIters = Long.parseLong(args[1]);

      // creating an instance to run then running!
      Ex5 ex5 = new Ex5();
      ex5.monteCarlo(numThreads, totIters);
    }
}
