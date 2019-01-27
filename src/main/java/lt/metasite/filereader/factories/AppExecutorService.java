package lt.metasite.filereader.factories;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutorService {

    private static int threads = 0;
    private ExecutorService executorService;

    public AppExecutorService() {
       threads = Runtime.getRuntime().availableProcessors() * 2;
    }

    public AppExecutorService(int threads) {
        this.threads = threads;
    }

    public ExecutorService getInstance(){
        return Executors.newFixedThreadPool(threads);
    }
}
