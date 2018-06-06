package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import data.DataPoint;
import data.DataSet;
import kMeans.Cluster;
import kMeans.KMeans;

public class KMain {

	public static void main(String[] args) {
		TimeMeasurement time=new TimeMeasurement();
		DataSet dataSet=new DataSet("LSH-nmi.csv");
		List<DataPoint> data=dataSet.getDataPoints();
		ArrayList<Double> nmi=new ArrayList<Double>();
		KMeans kmeans;

		int ands=2;
		int ors=1;
		int bucketSize=2000;
		int iterations=10;
		int tries=1;
		int startCount=args.length;
				
		switch (startCount) {
			case 6: bucketSize=Integer.parseInt(args[5]);
			case 5:	ors=Integer.parseInt(args[4]);
			case 4: ands=Integer.parseInt(args[3]);
			case 3:	iterations=Integer.parseInt(args[2]);
			case 2: tries=Integer.parseInt(args[1]);
			case 1:	if(args[0].equals("normal")) kmeans=new KMeans(true);	//without hash
					else kmeans=new KMeans(false);
			default: break;
		}
		
		if(args.length>0&&args[0].equals("normal")) kmeans=new KMeans(true);	//without hash
		else kmeans=new KMeans(false);		//with hash
		
		for(int f=0;f<tries;f++) {
			time.Start();
			ArrayList<DataPoint> startPoint=getRandomPoints(15,10,data);
			ArrayList<Cluster> clusters=kmeans.lshLloyed(startPoint, data, ands, ors ,bucketSize, iterations);	//ANDs,ORs,buckets,iterations
			time.Stop();
	
			//assign the points their cluster, just for NMI calculations, actual KMeans did already finish;
			for(int i=0;i<clusters.size();i++) {
				for(DataPoint point: clusters.get(i).getDataPoints()) {
					point.setCluster(i);
				}
			}
			nmi.add(NMI(dataSet.getTruthCluster(), dataSet.getCluster()));
			time.Snap();	
			System.out.println("Time for KMeans was " + time.get());
		}
		
		if (tries>1)	{
			System.out.println("After "+tries+" runs the average nmi is "+ avg(nmi)
								+ " and the average time was "+time.AvgTime());
			System.out.println("NMI values:");
			for(double n:nmi) {
				System.out.println(n);
			}
			System.out.println("Time values:"+time.Snaps());
		}

	}
	
	static <T extends Number> double avg(ArrayList<T> vec) {
		double avg = 0;
		for(T x:vec)	avg+=x.doubleValue();
		return avg/=vec.size();
	}
	
	private static ArrayList<DataPoint> getRandomStartPoints(int i, int dim) {
		ArrayList<DataPoint> ret=new ArrayList<DataPoint>();
		
		Random random=new Random();
		
		for(int j=0;j<i;j++) {
			double[] vec=new double[dim];
			for(int k=0;k<dim;k++) {
				vec[k]=random.nextDouble()*100;
			}
			ret.add(new DataPoint(vec, null));
		}
		return ret;
	}
	
	private static ArrayList<DataPoint> getRandomPoints(int i,int dim, List<DataPoint> data){
		ArrayList<DataPoint> ret=new ArrayList<DataPoint>();
		Random random=new Random();
		
		ArrayList<Integer> used=new ArrayList<Integer>();
		
		for(int j=0;j<i;j++) {
			int rand=random.nextInt(data.size());
			
			while(used.contains(rand)) {
				rand=random.nextInt(data.size());
			}
			
			ret.add(new DataPoint(data.get(rand).getVector(),null));
			used.add(rand);
		}
		
		
		return ret;
	}


	public static double NMI(ArrayList<Integer> one, ArrayList<Integer> two){
		if(one.size()!=two.size()){
			throw new IllegalArgumentException("Sizes don't match!");
		}
		int maxone = Collections.max(one);
		int maxtwo = Collections.max(two);

		double[][] count = new double[maxone+1][maxtwo+1];
		//System.out.println(count[1][2]);
		for(int i=0;i<one.size();i++){
			count[one.get(i)][two.get(i)]++;
		}
		//i<maxone=R
		//j<maxtwo=C
		double[] bj = new double[maxtwo+1];
		double[] ai = new double[maxone+1];

		for(int m=0;m<(maxtwo+1);m++){
			for(int l=0;l<(maxone+1);l++){
				bj[m]=bj[m]+count[l][m];
			}
		}
		for(int m=0;m<(maxone+1);m++){
			for(int l=0;l<(maxtwo+1);l++){
				ai[m]=ai[m]+count[m][l];
			}
		}

		double N=0;
		for(int i=0;i<ai.length;i++){
			N=N+ai[i];
		}
		double HU = 0;
		for(int l=0;l<ai.length;l++){
			double c=0;
			c=(ai[l]/N);
			if(c>0){
				HU=HU-c*Math.log(c);
			}
		}

		double HV = 0;
		for(int l=0;l<bj.length;l++){
			double c=0;
			c=(bj[l]/N);
			if(c>0){
				HV=HV-c*Math.log(c);
			}
		}
		double HUstrichV=0;
		for(int i=0;i<(maxone+1);i++){
			for(int j=0;j<(maxtwo+1);j++){
				if(count[i][j]>0){
					HUstrichV=HUstrichV-count[i][j]/N*Math.log(((count[i][j])/(bj[j])));
				}
			}
		}
		double IUV = HU-HUstrichV;
		double reto = IUV/(Math.max(HU, HV));

		System.out.println("NMI: "+reto);
		return reto;
	}

}
