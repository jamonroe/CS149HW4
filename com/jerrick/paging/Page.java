package com.jerrick.paging;

import java.util.Comparator;

public class Page 
{
	private int index;//Name of the page
	private int timeSinceUsage;//Time since page was last used
	private int frequency;//Amount of times the page was used
	
	public Page(int index)
	{
		this.index = index;
	}
	
	public int getIndex(){ return index; }
	public int getTimeSinceUsage(){ return timeSinceUsage; }
	public int getFrequency(){ return frequency; }
	
	public void incrementTimeSinceUsage()
	{
		timeSinceUsage++;
	}
	
	public void incrementFrequency()
	{
		frequency++;
	}
	
	public void resetTimeSinceUsage()
	{
		timeSinceUsage = 0;
	}
	
	public void resetFrequency()
	{
		frequency = 0;
	}
	
	public static Comparator<Page> recentComparator()
	{
		return new Comparator<Page>() 
		{
			@Override
			public int compare(Page pageA, Page pageB) 
			{
				if (pageB.getTimeSinceUsage() - pageA.getTimeSinceUsage() < 0) return -1;
				if (pageB.getTimeSinceUsage() - pageA.getTimeSinceUsage() > 0) return 1;
				return 0;
			}
		};
	}
	
	public static Comparator<Page> leastFreqComparator() 
	{
		return new Comparator<Page>() 
		{
			@Override
			public int compare(Page pageA, Page pageB) 
			{
				if (pageB.getFrequency() - pageA.getFrequency() < 0) return 1;
				if (pageB.getFrequency() - pageA.getFrequency() > 0) return -1;
				return 0;
			}
		};
	}
	
	public static Comparator<Page>	mostFreqComparator() 
	{
		return new Comparator<Page>() 
		{
			@Override
			public int compare(Page pageA, Page pageB) 
			{
				if (pageB.getFrequency() - pageA.getFrequency() < 0) return -1;
				if (pageB.getFrequency() - pageA.getFrequency() > 0) return 1;
				return 0;
			}
		};
	}
}
