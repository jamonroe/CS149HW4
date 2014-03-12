import java.util.Comparator;


public class SwapProcess
{
	private char name; //A-Z
	private int size; //4,8,16, or 32 MB
	private int duration; // 1-5 seconds
	private int start_point; //"index" of memory where this process begins
	private int end_point;
	private int remaining_time;
	
	public SwapProcess(char name, int size, int duration)
	{
		this.name = name;
		this.size = size;
		this.duration = duration;
		this.remaining_time = duration;
		start_point = -1;
		end_point = -1;
	}
	
	public char getName() { return name; }
	public int getSize() { return size; }
	public int getDuration() { return duration; }
	public int getStartPoint() {return start_point; }
	public int getEndPoint() { return end_point; }
	public int getRemainingTime() { return remaining_time; }
	
	public void setStartPoint(int z)
	{
		start_point = z;
		end_point = z + duration - 1;
	}
	
	public boolean isComplete() { return remaining_time == 0; }
	public void run() { remaining_time--; }
	
	public static Comparator<SwapProcess> startPointComparator() {
		return new Comparator<SwapProcess>() {
			@Override
			public int compare(SwapProcess procA, SwapProcess procB) {
				if (procB.getStartPoint() - procA.getStartPoint() < 0) return 1;
				if (procB.getStartPoint() - procA.getStartPoint() > 0) return -1;
				return 0;
			}
		};
	}
	
}
