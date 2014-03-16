package com.jerrick.paging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PageSimulator 
{
	private final int MEMORY_SIZE = 4;//Max pages in memory;
	private final int PROGRAM_SIZE = 10;//Number of pages in the program
	private final int PAGE_REFERENCES = 100;//Number of times a page is picked (Paging simulation)
	
	private Random rand;
	
	private ArrayList<Page> program;
	private ArrayList<Page> memory;
	
	public PageSimulator()
	{
		rand = new Random();
		
		program = new ArrayList<Page>();
		memory = new ArrayList<Page>();
		for(int i=0;i<PROGRAM_SIZE;i++)
			program.add(new Page(i));
	}
	
	/**
	 * First In First Out (FIFO) paging algorithm
	 */
	public void fifo()
	{
		System.out.println("~~~~~ First In First Out ~~~~~");
		int prevPageRef = -1;
		int nextPage;
		Page evictedPage;
		
		for(int i=0;i<PAGE_REFERENCES;i++)
		{
			evictedPage = null;
			nextPage = nextReference(prevPageRef);
			prevPageRef = nextPage;
			
			if(!pageInMemory(nextPage))
			{
				//Add page into memory
				if(memory.size() != MEMORY_SIZE)//Is memory full?
				{//No
					memory.add(program.get(nextPage));
				}
				else
				{//Yes
					Page page = program.get(nextPage);
					evictedPage = memory.remove(0);
					memory.add(page);
				}
			}
			
			printMemory();
			if(evictedPage != null)
				System.out.printf("   Page Referenced: %d | Page %d evicted\n", nextPage, evictedPage.getIndex());
			else
				System.out.printf("   Page Referenced: %d | No eviction\n", nextPage);
		}
		
		memory.clear();
		System.out.println();
	}
	
	/**
	 * Least Recently Used (LRU) paging algorithm
	 */
	public void lru()
	{
		System.out.println("~~~~~ Least Recently Used ~~~~~");
		
		Page evictedPage;
		int prevPageRef = -1;
		int nextPage;
		
		for(int i=0;i<PAGE_REFERENCES;i++)
		{
			evictedPage = null;
			nextPage = nextReference(prevPageRef);
			prevPageRef = nextPage;
			
			for(int index=0;index<memory.size();index++)
				memory.get(index).incrementTimeSinceUsage();
			
			//for(int x=0;x<memory.size();x++)
			//	System.out.printf("%d: %d\n", memory.get(x).getIndex(), memory.get(x).getTimeSinceUsage());
			
			if(!pageInMemory(nextPage))
			{
				//Add page into memory
				if(memory.size() != MEMORY_SIZE)//Is memory full?
				{//No
					memory.add(program.get(nextPage));
				}
				else
				{//Yes
					Collections.sort(memory, Page.recentComparator());
					Page page = program.get(nextPage);
					evictedPage = memory.remove(0);
					memory.add(page);
				}
			}
			
			printMemory();
			if(evictedPage != null)
				System.out.printf("   Page Referenced: %d | Page %d evicted\n", nextPage, evictedPage.getIndex());
			else
				System.out.printf("   Page Referenced: %d | No eviction\n", nextPage);
		}
		
		memory.clear();
		System.out.println();
	}
	
	/**
	 * Least Frequently Used (LFU) paging algorithm
	 */
	public void lfu()
	{
		
	}
	
	private boolean pageInMemory(int pageIndex)
	{
		for(int i=0;i<memory.size();i++)
		{
			if(memory.get(i).getIndex() == pageIndex)
			{
				memory.get(i).resetTimeSinceUsage();
				memory.get(i).incrementFrequency();
				return true;
			}
		}
		
		return false;
	}
	
	private int nextReference(int prevRefIndex)
	{
		if(prevRefIndex == -1)
			return rand.nextInt(10);
		
		int offset = 0;
		
		int locRef = rand.nextInt(10);
		if(locRef < 7)
			offset = rand.nextInt(3) - 1;
		else	
			offset = rand.nextInt(7) + 2;
		
		int index = prevRefIndex + offset;
		
		if(index == -1)//Wrap to the end
			return 9;
		
		if(index > 9)//Wrap to the front
			return index % PROGRAM_SIZE;
		
		return index;
	}
	
	private void printMemory()
	{
		System.out.print("Memory: [");
		int emptyMemory = MEMORY_SIZE - memory.size();
		for(int i=0;i<memory.size();i++)
			System.out.printf(" %d",memory.get(i).getIndex());
		for(int i=0;i<emptyMemory;i++)
			System.out.print(" X");
		System.out.print(" ]");
	}
}
