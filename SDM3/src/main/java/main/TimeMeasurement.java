package main;

import java.util.ArrayList;

public class TimeMeasurement {
	private long start;
	private long last;
	ArrayList<Long> allTime=new ArrayList<Long>();
	
	public void Start()	{
		start=System.currentTimeMillis();
	}
	public  void Stop()	{
		last=System.currentTimeMillis()-start;
	}
	public void Snap()	{
		allTime.add(last);
	}
	
	public String AvgTime()	{
		return avg(allTime)+" Miliseconds";		
	}
	public String Snaps()	{
		String s = "";
		for(double t:allTime) {
			s+=t+"ms ";
//			System.out.println(t);
		}
		return s;
	}
	
	public  long get_ms()	{
		return last;
	}
	public  String get()	{
		return last+" Miliseconds";
	}	
	
	
	static <T extends Number> double avg(ArrayList<T> vec) {
		double avg = 0;
		for(T x:vec)	avg+=x.doubleValue();
		return avg/=vec.size();
	}	
}
