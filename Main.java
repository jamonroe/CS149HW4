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
		
		System.out.println("=========== Page Simulation ===========");
		
		// true - print all information about the runs
		// false - only print statistic information about the Average Hit Ratios
		PageSimulator ps = new PageSimulator(true);
		ps.fifo();
		ps.lru();
		ps.lfu();
		ps.mfu();
		ps.randomPick();
	}
	
}
