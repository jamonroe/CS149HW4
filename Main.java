import com.jerrick.paging.PageSimulator;


public class Main
{
	public static final int RUNS = 5;
	
	public static void main(String [] args)
	{
		int ff_avg = 0, nf_avg = 0, bf_avg = 0;
		
		for (int i = 0; i < RUNS; i++)
		{
			int curr_run_count;
			SwapSimulator FF = new SwapSimulator("FF");
			System.out.printf("~~~~~ FIRST FIT RUN %d HAS BEGUN ~~~~~\n\n", i + 1);
			curr_run_count = FF.run();
			ff_avg += curr_run_count;
			System.out.printf("\nFIRST FIT RUN %d PROCESSES SWAPPED IN : %d\n\n", i + 1, curr_run_count);
		}
		
		for (int i = 0; i < RUNS; i++)
		{
			int curr_run_count;
			SwapSimulator NF = new SwapSimulator("NF");
			System.out.printf("~~~~~ NEXT FIT RUN %d HAS BEGUN ~~~~~\n\n", i + 1);
			curr_run_count = NF.run();
			nf_avg += curr_run_count;
			System.out.printf("\nNEXT FIT RUN %d PROCESSES SWAPPED IN : %d\n\n", i + 1, curr_run_count);
		}
		
		for (int i = 0; i < RUNS; i++)
		{
			int curr_run_count;
			SwapSimulator BF = new SwapSimulator("BF");
			System.out.printf("~~~~~ BEST FIT RUN %d HAS BEGUN ~~~~~\n\n", i + 1);
			curr_run_count = BF.run();
			bf_avg += curr_run_count;
			System.out.printf("\nBEST FIT RUN %d PROCESSES SWAPPED IN : %d\n\n", i + 1, curr_run_count);
		}
		
		
		ff_avg /= RUNS;
		nf_avg /= RUNS;
		bf_avg /= RUNS;
		
		System.out.println("The average number of processes swapped in for First Fit is: " + ff_avg);
		System.out.println("The average number of processes swapped in for Next Fit is: " + nf_avg);
		System.out.println("The average number of processes swapped in for Best Fit is: " + bf_avg);
		
		System.out.println();
		System.out.println("=========== Page Simulation ===========");
		double hitRatioAvgs[] = new double[5];
		// true - print all information about the runs
		// false - only print statistic information about the Average Hit Ratios
		PageSimulator ps = new PageSimulator(true);
		
		System.out.println("----------- FIFO Start -----------");
		for (int i = 0; i < 5; i++) {
			double hitRatio = ps.fifo() * 100;
			hitRatioAvgs[0]+=hitRatio;
			System.out.printf("Run %d Hit Ratio: %.0f%%\n", (i+1), hitRatio);
		}
		hitRatioAvgs[0]/=5;

		System.out.println("----------- LRU Start -----------");
		for (int i = 0; i < 5; i++) {
			double hitRatio = ps.lru() * 100;
			hitRatioAvgs[1]+=hitRatio;
			System.out.printf("Run %d Hit Ratio: %.0f%%\n", (i+1), hitRatio);
		}
		hitRatioAvgs[1]/=5;
		
		System.out.println("----------- LFU Start -----------");
		for (int i = 0; i < 5; i++) {
			double hitRatio = ps.lfu() * 100;
			hitRatioAvgs[2]+=hitRatio;
			System.out.printf("Run %d Hit Ratio: %.0f%%\n", (i+1), hitRatio);
		}
		hitRatioAvgs[2]/=5;
		
		System.out.println("----------- MFU Start -----------");
		for (int i = 0; i < 5; i++) {
			double hitRatio = ps.mfu() * 100;
			hitRatioAvgs[3]+=hitRatio;
			System.out.printf("Run %d Hit Ratio: %.0f%%\n", (i+1), hitRatio);
		}
		hitRatioAvgs[3]/=5;
		
		System.out.println("----------- Random Pick Start -----------");
		for (int i = 0; i < 5; i++) {
			double hitRatio = ps.randomPick() * 100;
			hitRatioAvgs[4]+=hitRatio;
			System.out.printf("Run %d Hit Ratio: %.0f%%\n", (i+1), hitRatio);
		}
		hitRatioAvgs[4]/=5;
		
		System.out.println();
		System.out.printf("Average Hit Ratio for \"FIFO\": %.2f%%\n", hitRatioAvgs[0]);
		System.out.printf("Average Hit Ratio for \"LRU\": %.2f%%\n", hitRatioAvgs[1]);
		System.out.printf("Average Hit Ratio for \"LFU\": %.2f%%\n", hitRatioAvgs[2]);
		System.out.printf("Average Hit Ratio for \"MFU\": %.2f%%\n", hitRatioAvgs[3]);
		System.out.printf("Average Hit Ratio for \"Random Pick\": %.2f%%\n", hitRatioAvgs[4]);
	}
	
}
