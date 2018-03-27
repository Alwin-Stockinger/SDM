package dataGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/* Punkte von beliebiger dimension
 * constructor:
 * 		Point(ArrayList<T> point)					punkt kann als ArrayList übergeben werden  
 * 		Point(int dim)  							lehrer Punkt (0,...) mit Dimension dim wird generiert
 *  randomise(Point<T> center, T zufalls_bereich)	ein Punkt der den center angibt, von ein punkt 
 *  												irgendwo im bereich erzeugt wird
 *  
 * 
 */

public class Point<T> {
	ArrayList<T> point=new ArrayList<T>();;
	int dim=0;
	
	private T cast(Object obj) {
		   return (T) obj;
	}	
	private double castd(T obj) {
			   return (double) (obj);

	}	
	
	public Point(int dim)	{
		this.dim=dim;
		for(int i=0;i<dim;i++) {
			point.add(cast(0));
    	}
	}	
	public Point(ArrayList<T> point)	{
		this.point=point;
		dim=point.size();
	}
	
	public T get(int pos)	{
		return point.get(pos);
	}
	public void set(int pos, T data)	{
		point.set(pos,data);
	}
	public int dim()	{
		return this.dim;
	}
	public int size()	{
		return dim();
	}
	ArrayList<T> point()	{
		return this.point;
	}

	
	public void randomise(Point<T> center, T zufalls_bereich)	{
		Random r = new Random();
		for(int i=0;i<dim;i++)	{
			double d1= Double.parseDouble(zufalls_bereich.toString());	d1=d1-(d1/2);
			double d2=Double.parseDouble(center.get(i).toString());
			double d = r.nextGaussian()*d1+d2;
			set(i, cast(d));
		}
	}
	
    public String toString() {
    	String s="[";
    	for(int i=0;i<dim;i++) {
    		if(i>0) s+=",";
    		s+=get(i);
    	}
    	s+="]";
        return s;
    }
    



}
