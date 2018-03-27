package dataGenerator;

import java.util.Random;



public class XYPoint {
	double x;
	double y;

	public XYPoint(int cluster)	{
		random(cluster);
	}
	public void random(int cluster)	{
		Random r = new Random();
		
		int o=2;
		int xo = 0,yo=0;
		
		switch (cluster)	{
		case 0:
			xo=-o; yo=-o;
			break;
		case 1:
			xo=-o; yo=o;
			break;
		case 2:
			xo=o; yo=o;
			break;
		case 3:
			xo=o; yo=-o;
			break;
		default:
			System.out.println("ERRROR random");
		}
		
		x = r.nextGaussian() * 1 + xo;
		y = r.nextGaussian() * 1 + yo;
		
	}
	public XYPoint(double x, double y)	{
		set(x,y);
	}
	public void set(double x, double y)	{
		this.x=x;
		this.y=y;
	}
	public double X()	{
		return x;
	}
	public double Y()	{
		return y;
	}
	
	
}
