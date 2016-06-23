import java.io.*;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Created by cdlvsheng on 2016/5/16.
 */
public class ParallelSequentialContrast {

	int                    coreSize = Runtime.getRuntime().availableProcessors();
	ThreadPoolExecutor     exec     = new ThreadPoolExecutor(coreSize * 4, coreSize * 5, 0, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>(10000), new ThreadPoolExecutor.CallerRunsPolicy());
	Queue<Future<Integer>> queue    = new ConcurrentLinkedQueue<Future<Integer>>();
	volatile int sum = 0;
	private int countLineNum(File f) {
		if (!f.getName().endsWith("java") && !f.getName().endsWith(".js") && !f.getName().endsWith(".vm")) return 0;

		int sum = 0;
		try {
			BufferedReader br  = new BufferedReader(new FileReader(f));
			String         str = null;
			while ((str = br.readLine()) != null) sum++;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sum;
	}

	private class Task implements Callable<Integer> {
		File f;

		public Task(File f) {
			this.f = f;
		}

		public Integer call() throws Exception {
			int sum = 0;

			if (f.isDirectory()) {
				File[] fs = f.listFiles();
				for (File file : fs) {
					if (file.isDirectory()) queue.add(exec.submit(new Task(file)));
					else sum += countLineNum(file);
				}
			} else sum += countLineNum(f);

			return sum;
		}
	}

	public int parallelTraverse(File f) {
		queue.add(exec.submit(new Task(f)));
		int sum = 0;
		while (!queue.isEmpty()) {
			try {
				Future<Integer> future = queue.poll();
				sum += future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		exec.shutdown();
		return sum;
	}


	public int sequentialTraverse(File f) {
		int sum = 0;

		if (f.isDirectory()) {
			File[] fs = f.listFiles();
			for (File file : fs) {
				if (file.isDirectory()) sum += sequentialTraverse(file);
				else sum += countLineNum(file);
			}
		} else sum += countLineNum(f);

		return sum;
	}

	public void parallelTest(ParallelSequentialContrast psc, String pathname) {
		long start    = System.currentTimeMillis();
		int  sum      = psc.parallelTraverse(new File(pathname));
		long duration = System.currentTimeMillis() - start;
		System.out.println(String.format("parallel test, %d lines of code were found, time cost is %d ms", sum, duration));
	}

	public void sequentialTest(ParallelSequentialContrast psc, String pathname) {
		long start    = System.currentTimeMillis();
		int  sum      = psc.sequentialTraverse(new File(pathname));
		long duration = System.currentTimeMillis() - start;
		System.out.println(String.format("sequential test, %d lines of code were found, time cost is %d ms", sum, duration));
	}

	public static void main(String[] args) {
		ParallelSequentialContrast psc      = new ParallelSequentialContrast();
		String                     pathname = "D:\\Code_Git";
		psc.sequentialTest(psc, pathname);
		psc.parallelTest(psc, pathname);
	}
}
