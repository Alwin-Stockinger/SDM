package main.java.SDM.Spark;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

import SDM.Spark.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	
    	String filePath = "kddcup.data_10_percent";
        
        SparkConf conf = new SparkConf().setAppName("GRUPPE02").setMaster("local[*]");
        
        JavaSparkContext sc = new JavaSparkContext(conf);
        
        //turn this to Info or Debug to get all the logging back
        sc.setLogLevel("WARN");
        
        JavaRDD<String> rdd = sc.textFile(filePath);
		
		// Remove all non numeric features, to prepare rdd for Kmeans
		JavaRDD<Vector> parsedData = Parse_Data.parse_data(rdd).cache();

		int count = 100;
        do 
        {
            System.out.println("Please enter a number to execute a function");
            System.out.println("1 --> Print Labels in KDD Cup 1999 Data Set");
            System.out.println("2 --> KMeans Algorithm with default parameters k: 2, maxIterations: 20, runs: 1, initializationSteps: 5, epsilon: 1e-4");
            System.out.println("3 --> Choosing K with parameters maxIterations: 30, runs: 10, initializationSteps: 5, epsilon: 1.0e-6");
            System.out.println("4 --> Find best K for parameters maxIterations: 30, runs: 10, initializationSteps: 5, epsilon: 1.0e-6");
            System.out.println("5 --> Performance Measurement with different Threads");
            System.out.println("0 --> Exit");
            
            Scanner scan = new Scanner(System.in);
            count = scan.nextInt();
            
            
            if(count == 1) 
            {
            	//List the clustering labels (last column) and their distinct counts
        		Print_Labels.print_labels(rdd);
            }
            if(count == 2) 
            {
            	// Send the parsed data to Kmeans method
        		K_Means.Kmeans(parsedData);
            }
            if(count == 3) 
            {
            	System.out.println("Please enter K");
            	 Scanner scan2 = new Scanner(System.in);
                 int a = scan2.nextInt();
                 
            	
            	//Choose K
        		K_Means.choosek(parsedData, a);
            }
            if(count == 4) {
            	System.out.println("Please enter a start K");
            	Scanner scan2=new Scanner(System.in);
            	int k=scan2.nextInt();
            	
            	System.out.println("Please enter a start stair size");
            	Scanner scan3=new Scanner(System.in);
            	int stair=scan3.nextInt();
            	
            	
            	K_Means.findk(parsedData, k, stair);
            }
            if(count==5) {
            	
            	System.out.println("Please enter a K");
            	Scanner scan2=new Scanner(System.in);
            	int k=scan2.nextInt();
            	
            	System.out.println("Please enter maximum number of threads");
            	Scanner scan3=new Scanner(System.in);
            	int t=scan2.nextInt();
            	
            	for(int i=1;i<=t;i++) {
            		conf.setMaster("local["+t+"]");
            		long startTime=System.nanoTime();
            		K_Means.choosek(parsedData, k);
            		long endTime=System.nanoTime();
            		
            		System.out.println("Execution with "+t+" threads did need "+(endTime-startTime)+"ns\n");
            	}
            	
            	
            	
            }
            
            if(count == 0) {sc.close(); System.exit(0);}
            
            
        } while (count != 0); 	

    }		
}
