import java.util.ArrayList;
import java.util.Collections;


public class SwapList extends ArrayList<SwapProcess>
{
	public static final int MAX_SIZE = 99;
	
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
						if (curr.getStartPoint() - 0 >= sp.getDuration())
						{
							return 0;
						}
					}
					else
					{
						prev = get(i - 1);
						diff = curr.getStartPoint() - prev.getEndPoint() - 1;
						if (diff >= sp.getDuration())
						{
							return prev.getEndPoint() + 1;
						}
					}
				}
				
				diff = MAX_SIZE - curr.getEndPoint() - 1;
			}
			
			return -1;
		}
		
		/* ~~~~~NEXT FIT ALGORITHM HERE~~~~~ */
		/* ~~~~~NEXT FIT ALGORITHM HERE~~~~~ */
		else if ( s == "NF" )
		{
			return 0;
		}
		
		/* ~~~~~BEST FIT ALGORITHM HERE~~~~~ */
		/* ~~~~~BEST FIT ALGORITHM HERE~~~~~ */
		
		else if ( s == "BF" )
		{
			return 0;
		}
		
		else
			return -1;
	}
	
	/**
	 * 
	 * @param sp process to swap in
	 * @return true if process gets swapped in, false otherwise
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
			return false;
	}
	
	public boolean swapInNextFit(SwapProcess sp, int index)
	{
		int slot = hasSpace(sp, "NF", index);
		if (slot != -1)
		{
			return true;
		}
		else
			return false;
	}
	
	public boolean swapInBestFit(SwapProcess sp)
	{
		int slot = hasSpace(sp, "BF", 0);
		if (slot != -1)
		{
			return true;
		}
		else
			return false;
	}
	
	public void run()
	{
		for (SwapProcess sp : this)
		{
			sp.run();
		}
		
		for (SwapProcess sp : this)
		{
			if (sp.getRemainingTime() == 0)
				{ remove(sp); }
		}
	}
	
	/**
	 * toString method to print out the entire list
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
