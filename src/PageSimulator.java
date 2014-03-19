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
	
	private boolean printAll;
	
	public static final int RUNS = 5;
	
	public static void main(String [] args)
	{
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
	
	public PageSimulator(boolean printStatsOnly)
	{
		rand = new Random();
		
		program = new ArrayList<Page>();
		memory = new ArrayList<Page>();
		for(int i=0;i<PROGRAM_SIZE;i++)
			program.add(new Page(i));
		
		this.printAll = printStatsOnly;
	}
	
	/**
	 * First In First Out (FIFO) paging algorithm
	 * @return Hit Ratio
	 */
	public double fifo()
	{
		if(printAll)
			System.out.println("~~~~~ First In First Out ~~~~~");
		
		int hits = 0;
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
			else
				hits++;
			
			if(printAll)
			{
				printMemory();
				if(evictedPage != null)
					System.out.printf("   Page Referenced: %d | Page %d evicted\n", nextPage, evictedPage.getIndex());
				else
					System.out.printf("   Page Referenced: %d | No eviction\n", nextPage);
			}
		}
		
		resetProgram();
		memory.clear();
		if(printAll)
			System.out.println();
		
		return (double) hits / (double) PAGE_REFERENCES;
	}
	
	/**
	 * Least Recently Used (LRU) paging algorithm
	 * @return Hit Ratio
	 */
	public double lru()
	{
		if(printAll)
			System.out.println("~~~~~ Least Recently Used ~~~~~");
		
		int hits = 0;
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
			
			//Print time since last usage
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
			else
				hits++;
			
			if(printAll)
			{
				printMemory();
			
				if(evictedPage != null)
					System.out.printf("   Page Referenced: %d | Page %d evicted\n", nextPage, evictedPage.getIndex());
				else
					System.out.printf("   Page Referenced: %d | No eviction\n", nextPage);
			}
		}
		
		resetProgram();
		memory.clear();
		if(printAll)
			System.out.println();
		
		return (double) hits / (double) PAGE_REFERENCES;
	}
	
	/**
	 * Least Frequently Used (LFU) paging algorithm
	 * @return Hit Ratio
	 */
	public double lfu()
	{
		if(printAll)
			System.out.println("~~~~~ Least Frequently Used ~~~~~");
		
		int hits = 0;
		Page evictedPage;
		int prevPageRef = -1;
		int nextPage;
		
		for(int i=0;i<PAGE_REFERENCES;i++)
		{
			evictedPage = null;
			nextPage = nextReference(prevPageRef);
			prevPageRef = nextPage;
			
			//Print page frequencies
			//for(int x=0;x<memory.size();x++)
			//	System.out.printf("%d: %d\n", memory.get(x).getIndex(), memory.get(x).getFrequency());
			
			if(!pageInMemory(nextPage))
			{
				//Add page into memory
				if(memory.size() != MEMORY_SIZE)//Is memory full?
				{//No
					program.get(nextPage).incrementFrequency();
					memory.add(program.get(nextPage));
				}
				else
				{//Yes
					Collections.sort(memory, Page.leastFreqComparator());
					Page page = program.get(nextPage);
					page.incrementFrequency();
					evictedPage = memory.remove(0);
					evictedPage.resetFrequency();
					memory.add(page);
				}
			}
			else
				hits++;
			
			if(printAll)
			{
				printMemory();
			
				if(evictedPage != null)
					System.out.printf("   Page Referenced: %d | Page %d evicted\n", nextPage, evictedPage.getIndex());
				else
					System.out.printf("   Page Referenced: %d | No eviction\n", nextPage);
			}
		}
		
		resetProgram();
		memory.clear();	
		if(printAll)
			System.out.println();
		
		return (double) hits / (double) PAGE_REFERENCES;	
	}

	/**
	 * Most Frequently Used (MFU) paging algorithm
	 * @return Hit Ratio
	 */
	public double mfu()
	{
		if(printAll)
			System.out.println("~~~~~ Most Frequently Used ~~~~~");
		
		int hits = 0;
		Page evictedPage;
		int prevPageRef = -1;
		int nextPage;
		
		for(int i=0;i<PAGE_REFERENCES;i++)
		{
			evictedPage = null;
			nextPage = nextReference(prevPageRef);
			prevPageRef = nextPage;
			
			//Print page frequencies
			//for(int x=0;x<memory.size();x++)
			//	System.out.printf("%d: %d\n", memory.get(x).getIndex(), memory.get(x).getFrequency());
			
			if(!pageInMemory(nextPage))
			{
				//Add page into memory
				if(memory.size() != MEMORY_SIZE)//Is memory full?
				{//No
					program.get(nextPage).incrementFrequency();
					memory.add(program.get(nextPage));
				}
				else
				{//Yes
					Collections.sort(memory, Page.mostFreqComparator());
					Page page = program.get(nextPage);
					page.incrementFrequency();
					evictedPage = memory.remove(0);
					evictedPage.resetFrequency();
					memory.add(page);
				}
			}
			else
				hits++;
			
			if(printAll)
			{
				printMemory();
			
				if(evictedPage != null)
					System.out.printf("   Page Referenced: %d | Page %d evicted\n", nextPage, evictedPage.getIndex());
				else
					System.out.printf("   Page Referenced: %d | No eviction\n", nextPage);
			}
		}
		
		resetProgram();
		memory.clear();	
		if(printAll)
			System.out.println();
		
		return (double) hits / (double) PAGE_REFERENCES;
	}

	/**
	 * Random Pick paging algorithm
	 * @return Hit ratio
	 */
	public double randomPick()
	{
		if(printAll)
			System.out.println("~~~~~ Random Pick ~~~~~");
		
		int hits = 0;
		Page evictedPage;
		int prevPageRef = -1;
		int nextPage;
		
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
					//Select a random page to evict
					Page page = program.get(nextPage);
					evictedPage = memory.remove(rand.nextInt(4));
					memory.add(page);
				}
			}
			else
				hits++;
			
			if(printAll)
			{
				printMemory();
			
				if(evictedPage != null)
					System.out.printf("   Page Referenced: %d | Page %d evicted\n", nextPage, evictedPage.getIndex());
				else
					System.out.printf("   Page Referenced: %d | No eviction\n", nextPage);
			}
			
		}
		
		resetProgram();
		memory.clear();	
		if(printAll)
			System.out.println();	
		
		return (double) hits / (double) PAGE_REFERENCES;
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
	
	private void resetProgram()
	{
		for(int i=0;i<PROGRAM_SIZE;i++)
		{
			Page page = program.get(i);
			page.resetTimeSinceUsage();
			page.resetFrequency();
		}
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
