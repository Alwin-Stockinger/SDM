package main;

public class TimeMeasurement {
	private long start;
	private long last;
	
	public void Start()	{
		start=System.currentTimeMillis();
	}
	
	public  void Stop()	{
		last=System.currentTimeMillis()-start;
	}
	
	public  long get()	{
		return last;
	}
}
