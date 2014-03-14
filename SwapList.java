import java.util.ArrayList;
import java.util.Collections;

/**
 * SwapList acts as the "LinkedList"
 * This is the "memory for the processes to run"
 * @author DerrL
 *
 */
public class SwapList extends ArrayList<SwapProcess>
{
	public static final int MAX_SIZE = 99; //amount of memory space
	
	/**
	 * Checks if there is space for a process
	 * 
	 * @param sp process to find space for
	 * @param s string to determine which algorithm
	 * @param index used mainly for NextFit algorithm
	 * @return index of "memory" for a slot in memory that 
	 * 		fits for the process based on which algorithm to use
	 */
	public int hasSpace(SwapProcess sp, String s, int index)
	{
		/* ~~~~~FIRST FIT ALGORITHM HERE~~~~~ */
		/* ~~~~~FIRST FIT ALGORITHM HERE~~~~~ */
		
		if (s == "FF") // for first fit
		{
			if(isEmpty())
				return 0;
			else
			{
				SwapProcess curr, prev;
				int diff;
				
				curr = get(0); //in case the ArrayList only has 1 item
				
				for (int i = 0; i < size(); i++)
				{
					curr = get(i);
					if (i == 0)
					{
						//check if the space before the first entry is big enough
						if (curr.getStartPoint() - 0 >= sp.getSize())
						{
							return 0;
						}
					}
					else //check if spaces between each entry is big enough
					{
						prev = get(i - 1);
						diff = curr.getStartPoint() - prev.getEndPoint() - 1;
						if (diff >= sp.getSize())
						{
							return prev.getEndPoint() + 1;
						}
					}
				}
				
				diff = MAX_SIZE - curr.getEndPoint() - 1;
				//check if the space after the last entry is big enough
				if (diff >= sp.getSize())
				{
					return curr.getEndPoint() + 1;
				}
			}
			
			return -1;
		}
		
		/* ~~~~~NEXT FIT ALGORITHM HERE~~~~~ */
		/* ~~~~~NEXT FIT ALGORITHM HERE~~~~~ */
		else if ( s == "NF" )
		{
			if (isEmpty())
				return 0;
			else
			{
				SwapProcess curr, next, prev;
				int diff;
				
				next = get(index); //in case this is the last entry of the list
				
				for (int i = index; i < size() - 1; i++)
				//check between each entry (after the index of previous hasSpace call)
				{
					curr = get(i);
					next = get(i + 1);
					
					diff = next.getStartPoint() - curr.getEndPoint() - 1;
					if (diff >= sp.getSize())
					{
						return curr.getEndPoint() + 1;
					}
				}
				
				diff = MAX_SIZE - next.getEndPoint() - 1;
				//check if the space after the last entry is big enough
				if (diff >= sp.getSize())
				{
					return next.getEndPoint() + 1;
				}
				
				for (int i = 0; i <= index; i++)
				{
					curr = get(i);
					if (i == 0)
					{
						//check if the space before the first entry is big enough
						if (curr.getStartPoint() - 0 >= sp.getSize())
						{
							return 0;
						}
					}
					else //check spaces between each entry (before index of previous hasSpace call)
					{
						curr = get(i);
						prev = get(i - 1);
						
						diff = curr.getStartPoint() - prev.getEndPoint() - 1;
						if (diff >= sp.getSize())
						{
							return prev.getEndPoint() + 1;
						}
						
					}
				}
				
			}
			
			return -1;
		}
		
		/* ~~~~~BEST FIT ALGORITHM HERE~~~~~ */
		/* ~~~~~BEST FIT ALGORITHM HERE~~~~~ */
		
		else if ( s == "BF" )
		{
			if(isEmpty())
				return 0;
			else
			{
				SwapProcess curr, prev;
				int smallest_location = -1;
				int diff;
				int smallest_diff = 99;
				
				curr = get(0); //in case the ArrayList only has 1 item
				
				for (int i = 0; i < size(); i++)
				{
					curr = get(i);
					if (i == 0)
					{
						//check if the space before the first entry is big enough
						diff = curr.getStartPoint() - 0;
						if (diff >= sp.getSize() && diff < smallest_diff)
						{
							smallest_location = 0;
						}
					}
					else //check if spaces between each entry is big enough
					{
						prev = get(i - 1);
						diff = curr.getStartPoint() - prev.getEndPoint() - 1;
						if (diff >= sp.getSize() && diff < smallest_diff)
						{
							smallest_diff = diff;
							smallest_location = prev.getEndPoint() + 1;
						}
					}
				}
				
				diff = MAX_SIZE - curr.getEndPoint() - 1;
				//check if the space after the last entry is big enough
				if (diff >= sp.getSize() && diff < smallest_diff)
				{
					smallest_diff = diff;
					smallest_location = curr.getEndPoint() + 1; 
				}
				
				return smallest_location;
			}
		}
		
		else
		{
			System.out.println("INCORRECT ALGORITHM!!!");
			return -1;
		}
	}
	
	/**
	 * calls hasSpace using the First Fit algorithm
	 * adds to the list then sorts the list
	 * @param sp process to swap in
	 * @return true if swapping in succeeded, false if otherwise
	 */
	public boolean swapInFirstFit(SwapProcess sp)
	{
		int slot = hasSpace(sp, "FF", 0);
		if (slot != -1)
		{
			sp.setStartPoint(slot);
			add(sp);
			Collections.sort(this, SwapProcess.startPointComparator());
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * calls hasSpace using the Next Fit algorithm
	 * adds to the list then sorts the list
	 * @param sp SwapProcess to swap in
	 * @param index of previous swapped in process
	 * @return true if swapping in succeeded, false if otherwise
	 */
	public boolean swapInNextFit(SwapProcess sp, int index)
	{
		int slot = hasSpace(sp, "NF", index);
		if (slot != -1)
		{
			sp.setStartPoint(slot);
			add(sp);
			Collections.sort(this, SwapProcess.startPointComparator());
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * calls hasSpace using the Best Fit algorithm
	 * adds to the list then sorts the list
	 * @param sp SwapProcess to swap in to the list
	 * @return true if swapping in succeeded, false if otherwise
	 */
	public boolean swapInBestFit(SwapProcess sp)
	{
		int slot = hasSpace(sp, "BF", 0);
		if (slot != -1)
		{
			sp.setStartPoint(slot);
			add(sp);
			Collections.sort(this, SwapProcess.startPointComparator());
			return true;
		}
		else
			return false;
	}
	
	/**
	 * calls the run method on each process
	 * which just decrements the time remaining of each process
	 */
	public void run()
	{
		for (SwapProcess sp : this)
		{
			sp.run();
		}
		
	}
	
	/**
	 * toString method to create a string for list
	 */
	public String toString()
	{
		String s = "";

		if (isEmpty())
		{
			for (int i = 0; i < MAX_SIZE; i++)
			{
				s += ".";
			}
			
			return s;
		}
		
		SwapProcess curr = get(0);
		SwapProcess prev;
		char procName;
		int diff;
		
		for (int i = 0; i < size(); i++)
		{
			//print . for any empty space before the first entry
			//also print the first entry
			if (i == 0)
			{
				curr = get(i);
				procName = curr.getName();
				
				for (int j = 0; j < curr.getStartPoint(); j++)
				{
					s += ".";
				}
				
				for (int j = 0; j < curr.getSize(); j++)
				{
					s += procName;
				}
			}
			
			//print . for all empty space between current and previous entry
			//also print current entry
			else 
			{
				curr = get(i);
				procName = curr.getName();
				prev = get(i - 1);
				diff = curr.getStartPoint() - prev.getEndPoint() - 1;
				
				for (int j = 0; j < diff; j++)
				{
					s += ".";
				}
				
				for (int j = 0; j < curr.getSize(); j++)
				{
					s += procName;
				}
			}
		}
		
		diff = MAX_SIZE - curr.getEndPoint() - 1;
		
		//print . for all empty memory space after last entry
		for (int j = 0; j < diff; j++)
		{
			s += ".";
		}
		
		return s;
	}
}
