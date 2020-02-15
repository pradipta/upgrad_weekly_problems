import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Problem1 implements Runnable, Problem {
    private static int count = 0;
    private static final int loopTill = 10000;
    private static Lock lock = new ReentrantLock();

    //private static Boolean lock;

    @Override
    public void run() {
        try{
            lock.lock();
            for (int i = 0; i < loopTill; i++) {
                count++;
            }
        } catch (Exception ex){
            System.out.println(ex);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void main(String[] args) {
        int noOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(noOfThreads);
        for (int i = 0; i < noOfThreads; i++) {
            executorService.execute(new Problem1());
        }
        executorService.shutdown();

        System.out.println("Expected count: " + (noOfThreads * loopTill));
        System.out.println("Actual count: " + count);
    }
}
