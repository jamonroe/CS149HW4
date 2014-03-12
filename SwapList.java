import java.util.ArrayList;
import java.util.Collections;


public class SwapList extends ArrayList<SwapProcess>
{
	public static final int MAX_SIZE = 99;
	
	public int hasSpace(SwapProcess sp, String s)
	{
		/* FIRST FIT ALGORITHM HERE */
		if (s == "FF") // for first fit
		{
			if(this.isEmpty())
				return 0;
			else
			{
				SwapProcess curr, prev;
				int diff;
				
				curr = this.get(0); //in case the ArrayList only has 1 item
				
				for (int i = 0; i < this.size(); i++)
				{
					curr = this.get(i);
					if (i == 0)
					{
						if (curr.getStartPoint() - 0 >= sp.getDuration())
						{
							return 0;
						}
					}
					else
					{
						prev = this.get(i - 1);
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
		
		
		
		return -1;
	}
	
	public boolean swapInFirstFit(SwapProcess sp)
	{
		int slot = hasSpace(sp, "FF");
		if (slot != -1)
		{
			
			return true;
		}
		else
			return false;
	}
	
	public boolean swapInNextFit(SwapProcess sp)
	{
		int slot = hasSpace(sp, "NF");
		if (slot != -1)
		{
			return true;
		}
		else
			return false;
	}
	
	public boolean swapInBestFit(SwapProcess sp)
	{
		int slot = hasSpace(sp, "BF");
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
	}
}
