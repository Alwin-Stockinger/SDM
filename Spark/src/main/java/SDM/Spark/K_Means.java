package SDM.Spark;

import java.util.ArrayList;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;


public class K_Means {
	private final int MAX_CLUSTER_FOR_SUCHE=8;
	private int nr_threads=MAX_CLUSTER_FOR_SUCHE;
	
	private static JavaRDD<Vector> parsedData;

	private int num_clusters=2;	// als default, auch wenn nicht optimised
	private int maxIterations=20;
	private int runs=10;
	private int initializationSteps=5;
	private double epsilon=1.0e-6;
	
	//--------------------------------------------------------------------------------------------------
	
	public int setThreads	(int nr) {
		return nr_threads=nr;
	}
	public int getNum_clusters	() {
		return num_clusters;
	}
	public void setNum_clusters(int num_clusters) {
		this.num_clusters = num_clusters;
	}

	public int getMaxIterations() {
		return maxIterations;
	}
	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public int getRuns() {
		return runs;
	}
	public void setRuns(int runs) {
		this.runs = runs;
	}

	public int getInitializationSteps() {
		return initializationSteps;
	}
	public void setInitializationSteps(int initializationSteps) {
		this.initializationSteps = initializationSteps;
	}

	public double getEpsilon() {
		return epsilon;
	}
	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

	//--------------------------------------------------------------------------------------------------

	public K_Means(JavaRDD<Vector> parsedData){
		this.parsedData=parsedData;
	}
	
	public String getParameters()	{
		return " Clusters(k): "+num_clusters
				+"\n maxIterations: "+maxIterations
				+"\n runs: "+runs
				+"\n initializationSteps: "+initializationSteps
				+"\n epsilon: "+epsilon;   	
	}
	
	public void Kmeans(JavaRDD<Vector> parsedData)	{   	// Kmeans Algorithm of Spark      
           KMeans kmeans = new KMeans();
        
           // A clustering model for K-means of apache spark
           //KMeansModel clusters = kmeans.run(parsedData.rdd());
           KMeansModel clusters = KMeans.train(parsedData.rdd(), num_clusters, maxIterations);
           
           // Implemented Distance function --> pass clusters and parsedrdd to method			   
           Double euclidian_distance =Distance.euclidian(clusters,parsedData);
           //Double  euclidian_distance =Math.sqrt(clusters.computeCost(parsedData.rdd()));
           //public double computeCost(Dataset<?> dataset)
          //Return the K-means cost (sum of squared distances of points to their nearest center) for this model on the given data.
           
           // Return the number of elements in the RDD
           long Elements_rdd = parsedData.rdd().count();
           
           // using the overall average distance as our quality score for the clustering result
           //long average_distance = (long) (euclidian_distance / Elements_rdd);
           System.out.println("k: "+num_clusters+getParameters());
           System.out.println("Average Distance: " + (long) (euclidian_distance / Elements_rdd));                
	   }

	public double choosek()	{
		CalculateClusters c=new CalculateClusters(parsedData, num_clusters, maxIterations, runs, initializationSteps, epsilon);
		System.out.println(c);
		c.run();
		return c.Distance();

	}
/*	public static double choosek(JavaRDD<Vector> parsedrdd, int k, int maxIterations)	{	            											                     
		
		long Elements_rdd = parsedrdd.rdd().count();
        
        double sum_distance = 0.0;
        double sum_cost = 0.0;
        
        //int maxIterations = 30;
            
        // Run 10 times to get average result
    	//int runs=10;
        for(int j = 1 ; j<=runs ; j++)	 {
        	
           KMeans kmeans = new KMeans();
           
           kmeans.setEpsilon(epsilon);
           //kmeans.setRuns(10); does not do anything since spark 2
        	
           // Trains a k-means model using specified parameters, k - Number of clusters to create.
           // maxIterations - Maximum number of iterations allowed.
           
           KMeansModel clusters = kmeans.train(parsedrdd.rdd(), k, maxIterations);

           // Implemented Distance function --> pass clusters and parsedrdd to method			   
           double euclidian_distance = Distance.euclidian(clusters,parsedrdd);	                		                    	                    	                
            
           double average_distance = euclidian_distance / Elements_rdd;           
    
           sum_distance += average_distance;
           
           //Return the K-means cost (sum of squared distances of points to their nearest center) for this model on the given data.
           double centroid_distance = clusters.computeCost(parsedrdd.rdd());
           
           sum_cost += centroid_distance;
           
           System.out.println("run: "+j + "/"+runs+" sum_cost=" + sum_cost);
        }                	                             	                       
        
        sum_distance/=runs;
        sum_cost/=runs;
            
        System.out.println("Average Euclidian Distance achieved with k = " + k + ", value is " + (sum_distance));
        System.out.println("Distance from a data point to its nearest cluster’s centroid with k = " + k + ", value is " + (sum_cost ));
        return sum_distance;
    }*/

/*	public static void findk(JavaRDD<Vector> parsedrdd,int k, int stair) {
		double current=0;
		boolean best=false;
		while(!best) {
			current=choosek(parsedrdd,k,maxIterations);
			System.out.println("\n\n\n Current K is "+k+" with distance "+current);
			
			double up=choosek(parsedrdd,k+stair,maxIterations);
			double down=choosek(parsedrdd,k-stair,maxIterations);
			
			if(current<up&&current<down) {
				if(stair==1) best=true;
				stair/=2;
				
				//to get rid of lucky inits
			}
			else if(up>down) {
				current=down;
				k-=stair;
			}
			else {
				current=up;
				k+=stair;
			}
		}
		System.out.println("\nBest k is "+k+", with Average Euclidian Distance "+current+"\n");
		num_clusters=k;
		optimised=true;
	}*/
		
	public int findk2() {
		// knick (rechenzeit/distanz) wo verhältniss der letzten änderungen am geringsten
			
		ArrayList<Thread> threads = new ArrayList<>();
		ArrayList<CalculateClusters> calculate_cluster = new ArrayList<>();
		
		
		for(int cluster_count=2; cluster_count<=MAX_CLUSTER_FOR_SUCHE ;cluster_count++)	{
			CalculateClusters c=new CalculateClusters(parsedData, cluster_count, maxIterations, runs, initializationSteps, epsilon);
			calculate_cluster.add(c);
			Thread h=new Thread(c);
			h.start();
			threads.add(h);
		}
		for(int c=0; c<threads.size();c++)	{
			try {
				threads.get(c).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		double dif1=0, dif2=0;
		double rel1=0, rel2=0;
		double d1=0,d2=0,d3=0;
		
		int c;
		for(c=0; c<calculate_cluster.size();c++)	{
			rel1=rel2;
			d1=d2;
			d2=d3;
			d3=calculate_cluster.get(c).Distance();	//System.out.println("d="+d1+" "+d2+" "+d3);
			if (c>=3)	{
				dif1=d1-d2;
				dif2=d2-d3;
				rel2=dif1/dif2;	
				System.out.println(dif1+" "+dif2+ " "+rel1 + " "+rel2);
				if (rel2<rel1) break;
			}
		}
		c-=2;	// zb: 7-6/6-5 jetzt schlechter als 6-5/5-4 also ->
				// bei abbruch bei k=7, beste k=5 	
		//num_clusters=6;
		//optimised=true;
		return c;
	/*	
		int k=4;
		do	{
			rel1=rel2;
			d1=d2;
			d2=d3;
			d3=choosek(parsedrdd,k,maxIterations);
			dif1=d1-d2;
			dif2=d2-d3;
			rel2=dif1/dif2;		//System.out.println(dif1+" "+dif2+ " "+rel2 + " "+rel1);
			if (k>20) break; 	// zur sicherheit
			k++;
		} while(rel2<rel1 || k<=5);
		k-=2;	// zb: 7-6/6-5 jetzt schlechter als 6-5/5-4 also ->
				// bei abbruch bei k=7, beste k=5 
		num_clusters=k;
		optimised=true;
		*/		
	}
		
}
