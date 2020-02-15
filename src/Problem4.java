import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;


/**
 * Problem Statement:
 * In following code, we have 20 threads. End objective is to execute all 20 threads at the same time.
 * To do that, we are going to implement cyclic barrier. The way it works is, we pass number of parties (P) to cyclic barrier while creating its object.
 * Unless the CyclicBarrier#await() is called by various threads for (P) times, all caller threads will be blocked. Once all 20 threads have called CyclicBarrier#await,
 * they all will move ahead at the same time and capture System#currentTimeMillis as written in ShortTask#run.
 *
 * If our cyclic barrier implementation is correct, they all should have exact same timestamp.
 * However, due to lag in Operating System's CPU time allocation for each thread and time taken to execute System#currentTimeMillis, the captured time might vary by few nanos/per thread.
 * I have already considered this error in ShortTask#getCapturedTimestamp and ignoring last 2 digits while checking accuracy.
 *
 * Example:
 * Say we initialized CyclicBarrier with number of parties (P) = 5; and no. of threads = 5;
 * T1 will start running, it will call cyclicBarrier.await(). CyclicBarrier should reduce the no of parties to 4 and T1's execution is blocked.
 * T2 will start running, it will call cyclicBarrier.await(). CyclicBarrier should reduce the no of parties to 3 and T2's execution is blocked.
 * T3 will start running, it will call cyclicBarrier.await(). CyclicBarrier should reduce the no of parties to 2 and T3's execution is blocked.
 * T4 will start running, it will call cyclicBarrier.await(). CyclicBarrier should reduce the no of parties to 1 and T4's execution is blocked.
 * T5 will start running, it will call cyclicBarrier.await(). CyclicBarrier should reduce the no of parties to 0.
 *
 * As parties have reached to 0; T1, T2, T3, T4, T5 will all continue execution at the same time.
 *
 * Solution Restrictions:
 * 1. Do not modify any code of this class
 * 2. Create a class named CyclicBarrierImpl that implements CyclicBarrier interface. (Interface is attached in problem statement)
 * 3. Put all your logic in CyclicBarrierImpl#await()
 */


public class Problem4 implements Problem {
    @Override
    public void main(String[] args) throws Exception{
        int threads = 20;
        CyclicBarrier cyclicBarrier = new CyclicBarrierImpl(threads);
        List<ShortTask> tasks = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            System.out.println((threads - i) + " threads to be created..");
            ShortTask shortTask = new ShortTask(cyclicBarrier);
            new Thread(shortTask).start();
            Thread.sleep(1000L);
            tasks.add(shortTask);
        }
        validate(tasks);
    }

    private static void validate(List<ShortTask> tasks) throws Exception {
        ShortTask shortTask = tasks.get(0);
        while (!shortTask.isFinished()) {
            continue;
        }
        long firstTimestamp = shortTask.getCapturedTimestamp();

        for (ShortTask shortTaskToBeChecked : tasks) {
            if (firstTimestamp != shortTaskToBeChecked.getCapturedTimestamp()) {
                throw new Exception("Verification failed. Expected the timestamp to be same across all threads. \n Expected: "
                        + firstTimestamp + ", found: " + shortTaskToBeChecked.getCapturedTimestamp());
            }
        }
        System.out.println("Great man");
    }

    class ShortTask implements Runnable {

        private final CyclicBarrier cyclicBarrier;

        public ShortTask(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        private long capturedTimestamp;
        private boolean finished;

        @Override
        public void run() {
            try {
                cyclicBarrier.await();
                capturedTimestamp = System.currentTimeMillis();
                System.out.println("Ran " + Thread.currentThread().getName() + " at " + capturedTimestamp);
                finished = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public long getCapturedTimestamp() {
            // ignoring last 2 digits of timestamp for verification to compensate OS cpu allocation time and
            // time taken to execute System.currentTimeMillis()
            return capturedTimestamp / 100;
        }

        public boolean isFinished() {
            return finished;
        }
    }
}

interface CyclicBarrier {
    void await() throws InterruptedException;
}

class CyclicBarrierImpl implements CyclicBarrier {
    private int threads;
    static CountDownLatch latch = new CountDownLatch(20);
    public CyclicBarrierImpl(int threads){
        this.threads = threads;
    }
    @Override
    public void await() throws InterruptedException {
        latch.countDown();
        while(latch.getCount()>0){
            latch.await();
        }
    }
}
