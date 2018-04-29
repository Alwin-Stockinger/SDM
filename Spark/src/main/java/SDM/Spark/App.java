package SDM.Spark;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

import SDM.Spark.*;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;


public class App {
	
	final static String FILE_PATH = "kddcup.data_10_percent";
	final static Level LOG_LEVEL=Level.ERROR;
	
	static void set_logger(Level level)	{
//		ALL:	The ALL has the lowest possible rank and is intended to turn on all logging.
//		DEBUG:	The DEBUG Level designates fine-grained informational events that are most useful to debug an application.
//		ERROR:	The ERROR level designates error events that might still allow the application to continue running.
//		FATAL:	The FATAL level designates very severe error events that will presumably lead the application to abort.
//		INFO:	The INFO level designates informational messages that highlight the progress of the application at coarse-grained level.
//		OFF:	The OFF has the highest possible rank and is intended to turn off logging.
//		TRACE:	The TRACE Level designates finer-grained informational events than the DEBUG
//		TRACE_INT:	TRACE level integer value.
//		WARN:	The WARN level designates potentially harmful situations.			
		Logger.getLogger("org").setLevel(level);
	}
	
	
    public static void main( String[] args )	    {
    	
        set_logger(LOG_LEVEL);
        
        SparkConf conf = new SparkConf().setAppName("GRUPPE02").setMaster("local[*]");
        
        JavaSparkContext sc = new JavaSparkContext(conf);
        
        JavaRDD<String> rdd = sc.textFile(FILE_PATH);
		
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
        		K_Means.choosek(parsedData, a,30);
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
            		System.out.println("\nUsing "+i+" threads");
            		
            		for(int j=5;j<100;j+=5) {
            			long startTime=System.nanoTime();
                		K_Means.choosek(parsedData, k,j);
                		long endTime=System.nanoTime();
                		
                		System.out.println("Execution with maxIter="+j+" , did need "+(endTime-startTime)+"ns\n");
            		}
            		
            	}
            	
            	
            	
            }
            
            if(count == 0) {sc.close(); System.exit(0);}
            
            
        } while (count != 0); 	

    }		
}
