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
	
	public static final int TWO = 2;
	public static final int MAX_TIME = 60;
	
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
		for (int j = 0; j < 5; j++)
		{
			for (int i = 0; i < 26; i++)
			{
				char c = (char) ('A' + i);
				int time = rangedRandom(1, 5); //1-5 for duration
				int pow = rangedRandom(2, 5); //2-5 for size
				int size = (int)(Math.pow(TWO, pow)); 
				
				q.add(new SwapProcess(c, size, time));
			}
			
			for (int i = 0; i < 26; i++)
			{
				char c = (char) ('a' + i);
				int time = rangedRandom(1, 5);
				int pow = rangedRandom(2, 5);
				int size = (int)(Math.pow(TWO, pow));
				
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
			
			if (alg == "FF")
			{
				while (list.swapInFirstFit(q.peek()))
				{
					SwapProcess gotten = q.remove();
					printSwappedIn(gotten, i);
					count++;
				}
				
				for (int z = 0; z < list.size(); z++)
				{
					SwapProcess sp = list.get(z);
					
					if (sp.isComplete())
					{
						list.remove(sp);
						printSwappedOut(sp, i);
					}
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
				
				for (int z = 0; z < list.size(); z++)
				{
					SwapProcess sp = list.get(z);
					
					if (sp.isComplete())
					{
						list.remove(sp);
						printSwappedOut(sp, i);
					}
				}
			}
			
			else if (alg == "BF") //ADDED A STUPID SEMICOLON RIGHT HERE AND I WASTED 40 MINS TRYING TO 
				//FIGURE OUT WHAT WAS GOING ON!!! OMG!!!!
			{
				while (list.swapInBestFit(q.peek())) //ADDED ONE RIGHT HERE TOO! WHAT WAS I THINKING?
				{
					SwapProcess gotten = q.remove();
					printSwappedIn(gotten, i);
					count++;
				}
				
				for (int z = 0; z < list.size(); z++)
				{
					SwapProcess sp = list.get(z);
					
					if (sp.isComplete())
					{
						list.remove(sp);
						printSwappedOut(sp, i);
					}
				}
			}
			
			list.run();
		}
		
		return count;
	}
	
	public void printSwappedIn(SwapProcess sp, int time)
	{
		if (time == 60)
			System.out.printf("01:00 Swapped  in %c : %s\n", sp.getName(), list.toString());
		else
			System.out.printf("00:%02d Swapped  in %c : %s\n", time, sp.getName(), list.toString());
	}
	
	public void printSwappedOut(SwapProcess sp, int time)
	{
		if (time == 60)
			System.out.printf("01:00 Swapped out %c : %s\n", sp.getName(), list.toString());
		else
			System.out.printf("00:%02d Swapped out %c : %s\n", time, sp.getName(), list.toString());
		
	}

}
