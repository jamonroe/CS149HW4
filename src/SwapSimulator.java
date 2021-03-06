import java.util.LinkedList;
import java.util.Queue;
import java.math.*;

/**
 * Simulator for the swapper
 * This is where the running occurs
 * 
 * @author DerrL
 *
 */
public class SwapSimulator
{
	private SwapList list; //Memory space for processes to run on
	private Queue<SwapProcess> q; //Queue of processes
	private String alg; //Algorithm to use "FF"/"NF"/"BF"
	private int[] sizes = {5, 11, 17, 31};
	
	public static final int TWO = 2;
	public static final int MAX_TIME = 60;
	public static final int RUNS = 5;
	
	public static void main(String[] args) {
		String[] algorithms = {"FF", "NF", "BF"};
		int results[] = {0, 0, 0};
		for (int i = 0; i < algorithms.length; i++) {
			for (int j = 0; j < RUNS; j++) {
				System.out.println(algorithms[i] + " run #" + (j + 1));
				results[i] += new SwapSimulator(algorithms[i]).run();
				System.out.println();
			}
		}
		for (int i = 0; i < algorithms.length; i++) {
			System.out.println("Average processes swapped in for " + algorithms[i] + ": " + results[i]/RUNS);
		}
	}
	
	/**
	 * constructor
	 * @param alg name of the algorithm to use
	 */
	public SwapSimulator(String alg)
	{
		this.alg = alg;
		q = new LinkedList<SwapProcess>();
		processGenerator();
		list = new SwapList();
	}
	
	/**
	 * this method creates a random int within a given range
	 * 
	 * @param min minimum value of the range
	 * @param max maximum value of the range
	 * @return a random int within the range
	 */
	private int rangedRandom(int min, int max)
	{
	   int range = (max - min) + 1;     
	   return (int)(Math.random() * range) + min;
	}
	
	/**
	 * generates a ton of random processes so we will never run out!
	 * 260 processes? can't hurt to have more!
	 */
	private void processGenerator()
	{
		for (int j = 0; j < 2; j++)
		{
			for (int i = 0; i < 26; i++)
			{
				char c = (char) ('A' + i);
				int time = rangedRandom(1, 5); //1-5 for duration
				int index = rangedRandom(0, 3); //indexes 0-3
				int size = sizes[index];
				
				q.add(new SwapProcess(c, size, time));
			}
			
			for (int i = 0; i < 26; i++)
			{
				char c = (char) ('a' + i);
				int time = rangedRandom(1, 5);
				int index = rangedRandom(0, 3);
				int size = sizes[index];
				
				q.add(new SwapProcess(c, size, time));
			}
			
			for (int i = 0; i < 10; i++)
			{
				char c = (char) ('0' + i);
				int time = rangedRandom(1, 5);
				int index = rangedRandom(0, 3);
				int size = sizes[index];
				
				q.add(new SwapProcess(c, size, time));
			}
		}
	}

	/**
	 * This method is where the running occurs
	 * runs based on what algorithm was passed in from constructor
	 * 
	 * swaps processes in, removes finished processes, runs the list
	 * 
	 * @return number of processes successfully swapped in
	 */
	public int run()
	{
		int count = 0;
		
		for (int i = 0; i <= MAX_TIME; i++)
		{
			int index = 0;
			
			//remove completed processes at beginning of quanta
			for (int z = 0; z < list.size(); z++)
			{
				SwapProcess sp = list.get(z);
				
				if (sp.isComplete())
				{
					list.remove(sp);
					printSwappedOut(sp, i);
				}
			}
			
			//add new processes at beginning of quanta
			if (alg == "FF")
			{
				while (list.swapInFirstFit(q.peek()))
				{
					SwapProcess gotten = q.remove();
					printSwappedIn(gotten, i);
					count++;
				}
			}
			else if (alg == "NF")
			{
				while (list.swapInNextFit(q.peek(), index))
				{
					SwapProcess gotten = q.remove();
					printSwappedIn(gotten, i);
					index = list.indexOf(gotten);
					count++;
				}
			}
			else if (alg == "BF")
			{
				while (list.swapInBestFit(q.peek()))
				{
					SwapProcess gotten = q.remove();
					printSwappedIn(gotten, i);
					count++;
				}
			}
			
			list.run(); //run all the processes
		}
		
		return count;
	}
	
	public void printSwappedIn(SwapProcess sp, int time)
	{
		if (time == 60)
			System.out.printf("01:00 Swapped  in %c (Size %2d, Duration %d) : %s\n", sp.getName(), 
					sp.getSize(), sp.getDuration(), list.toString());
		else
			System.out.printf("00:%02d Swapped  in %c (Size %2d, Duration %d) : %s\n", time, sp.getName(), 
					sp.getSize(), sp.getDuration(), list.toString());
	}
	
	public void printSwappedOut(SwapProcess sp, int time)
	{
		if (time == 60)
			System.out.printf("01:00 Swapped out %c (Size %2d, Duration %d) : %s\n", sp.getName(), 
					sp.getSize(), sp.getDuration(), list.toString());
		else
			System.out.printf("00:%02d Swapped out %c (Size %2d, Duration %d) : %s\n", time, sp.getName(), 
					sp.getSize(), sp.getDuration(), list.toString());
		
	}

}
