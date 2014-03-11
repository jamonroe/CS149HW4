
public class SwapProcess
{
	private char name; //A-Z
	private int size; //4,8,16, or 32 MB
	private int duration; // 1-5 seconds
	private int start_time;
	private int end_time;
	
	public SwapProcess(char name, int size, int duration)
	{
		this.name = name;
		this.size = size;
		this.duration = duration;
	}
	
	public char getName() { return name; }
	public int getSize() { return size; }
	public int getDuration() { return duration; }
	public int getStartTime() { return start_time; }
	public int getEndTime() { return end_time; }
	
	public void setStartTime(int start_time) { this.start_time = start_time; }
	public void setEndTime(int end_time) { this.end_time = end_time; }
}
