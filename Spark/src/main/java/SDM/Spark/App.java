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
	
	final static String FILE_PATH = "./kddcup.data_10_percent";
	final static Level LOG_LEVEL=Level.ERROR;
	private static K_Means kmeans;
	
	private enum ProgrammOptions {
		EXIT 					,//("0 --> Exit"),
		Print_Labels 			,//("1 --> Print Labels in KDD Cup 1999 Data Set"),
		KMeans 					,//("2 --> KMeans Algorithm with parameters k:"	+kmeans.getNum_clusters()+kmeans.getParameters()),
		ChoosingK 				,//("3 --> Choosing K with parameters"				+kmeans.getParameters()),
		FindK 					,//("4 --> Find best K for parameters"				+kmeans.getParameters()),
		PerformanceMeasurement 	;//("5 --> Performance Measurement with different Threads");

/*		private String output_str;
		ProgrammOptions(String output_str) {
	        this.output_str = output_str;
	    }	 */   
		public static String OutputStr() { 
			String o="";
			o+="0 --> Exit";
			o+="\n1 --> Print Labels in KDD Cup 1999 Data Set";
			o+="\n2 --> KMeans Algorithm with parameters k:"	+kmeans.getNum_clusters()+kmeans.getParameters();
//			o+="\n3 --> Choosing K with parameters"				+kmeans.getParameters();
			o+="\n3 --> Set K";
			o+="\n4 --> Find best K for parameters"				+kmeans.getParameters();
			o+="\n5 --> Performance Measurement with different Threads";
			return o;
		}
	}
	private static ProgrammOptions ShowMenu()	{
		int count=0;
		Scanner scan = new Scanner(System.in);
		do	{
	        System.out.println("Please enter a number to execute a function");
	        System.out.println(ProgrammOptions.OutputStr());
        
	        count= scan.nextInt();
		} while (count>=ProgrammOptions.values().length);	// >= wegen 0 --> Exit
		return ProgrammOptions.values()[count]; 
	}
	
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
 
        SparkConf 		 conf = new SparkConf().setAppName("GRUPPE02")
        										.setMaster("local[*]")
   												.set("spark.driverEnv.SPARK_LOCAL_IP", "127.0.0.1")
   												.set("spark.driver.bindAddress", "127.0.0.1")
   												.set("spark.ui.showConsoleProgress", "false")
   							                    .set("spark.executorEnv.SPARK_LOCAL_IP", "127.0.0.1");
        JavaSparkContext sc   = new JavaSparkContext(conf);

        System.out.println("Parsing Data");
        Data data=new Data(sc, FILE_PATH);
        kmeans=new K_Means(data.get_data());	
        
        boolean exit=false;
        ProgrammOptions option;
        do	{
        	option=ShowMenu();
        	Scanner scan2=new Scanner(System.in);
        	Scanner scan3=new Scanner(System.in);
        	int k;
        	double distance=0.0;
	        switch (option)	{
		        case EXIT:
		        	break;
		        case Print_Labels:				
		        	data.print_labels();			
		        	break;
		        case KMeans:	   				
		        	//K_Means.Kmeans(data.get_data());
		        	System.out.println("distance="+kmeans.choosek());
		        	break;
		        case ChoosingK:					
		           	System.out.println("Please enter K");
		           	//Scanner scan2 = new Scanner(System.in);
	                kmeans.setNum_clusters(scan2.nextInt());
		        	break;
		        case FindK:
	            	/*System.out.println("Please enter a start K");
	            	//Scanner scan2=new Scanner(System.in);
	            	k=scan2.nextInt();
	            	
	            	System.out.println("Please enter a start stair size");
	            	//Scanner scan3=new Scanner(System.in);
	            	int stair=scan3.nextInt();
	            	
	            	K_Means.findk(data.get_data(), k, stair);*/
		        	int bestk=kmeans.findk2();
		        	kmeans.setNum_clusters(bestk);
		        	System.out.println("found k="+bestk);
		        	break;
		        case PerformanceMeasurement:
	            	System.out.println("Please enter a K");
	            	//Scanner scan2=new Scanner(System.in);
	            	k=scan2.nextInt();
	            	
	            	System.out.println("Please enter maximum number of threads");
	            	//Scanner scan3=new Scanner(System.in);
	            	int t=scan2.nextInt();
	            	
	            	//for(int i=1;i<=t;i++) {
		            for(int i=t;i>0;i--) {
	            		conf.setMaster("local["+t+"]");
	            		System.out.println("\nUsing "+i+" threads");
	            		
	            		for(int j=5;j<100;j+=5) {
	            			long startTime=System.nanoTime();
//	                		K_Means.choosek(data.get_data(), k,j);
	                		long endTime=System.nanoTime();
	                		
	                		System.out.println("Execution with maxIter="+j+" , did need "+(endTime-startTime)+"ns\n");
	            		}
	            	}
		        	break;
	        	default: System.out.println("Input Error");
	        }
        }while(!exit);

        sc.close();
        
        /*
        int count=0;
        do  {
            System.out.println("Please enter a number to execute a function");
            System.out.println("1 --> Print Labels in KDD Cup 1999 Data Set");
            
            System.out.print("2 --> KMeans Algorithm with ");
            if(!K_Means.IsOptimised())	System.out.print("default");
            else						System.out.print("optimised");
            System.out.println(	" parameters k: "+K_Means.getNum_clusters()+K_Means.getParameters());
            
            System.out.println("3 --> Choosing K with parameters"+K_Means.getParameters());
            System.out.println("4 --> Find best K for parameters"+K_Means.getParameters());
            System.out.println("5 --> Performance Measurement with different Threads");
            System.out.println("0 --> Exit");
            
            Scanner scan = new Scanner(System.in);
            count = scan.nextInt();

            if(count == 1)    {
            	//List the clustering labels (last column) and their distinct counts
        		//Print_Labels.print_labels(rdd);
        		data.print_labels();
        	}
            if(count == 2)  {    	// Send the parsed data to Kmeans method
        		K_Means.Kmeans(data.get_data());
            }
            if(count == 3)    {
            	System.out.println("Please enter K");
            	 Scanner scan2 = new Scanner(System.in);
                 int a = scan2.nextInt();
                
            	//Choose K
        		K_Means.choosek(data.get_data(), a,30);
            }
            if(count == 4) {
            	System.out.println("Please enter a start K");
            	Scanner scan2=new Scanner(System.in);
            	int k=scan2.nextInt();
            	
            	System.out.println("Please enter a start stair size");
            	Scanner scan3=new Scanner(System.in);
            	int stair=scan3.nextInt();
            	
            	K_Means.findk(data.get_data(), k, stair);
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
                		K_Means.choosek(data.get_data(), k,j);
                		long endTime=System.nanoTime();
                		
                		System.out.println("Execution with maxIter="+j+" , did need "+(endTime-startTime)+"ns\n");
            		}
            		
            	}
            }
//            if(count == 0) {sc.close(); break;//System.exit(0);}
        } while (count != 0); 
        
        sc.close();*/
    }		
}
