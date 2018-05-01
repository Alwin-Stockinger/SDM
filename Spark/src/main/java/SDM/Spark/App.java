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
		EXIT 					,
		Print_Labels 			,
		KMeans 					,
		FindK 					,
		PerformanceMeasurement 	,
		SetK 					,
		SetMaxIterations		,
		SetRuns					,
		SetInitializationSteps	,
		SetEpsilon				;

		
/*		private String output_str;
		ProgrammOptions(String output_str) {
	        this.output_str = output_str;
	    }	 */   
		public static String OutputStr() {
			int i=0;
			String o="";
			o+=i+" --> Exit";
			i++; o+="\n"+i+" --> Print Labels in KDD Cup 1999 Data Set";
			i++; o+="\n"+i+" --> KMeans Algorithm";// with parameters k:"	+kmeans.getNum_clusters()+kmeans.getParameters();
//			i++; o+="\n"+i+" --> Choosing K with parameters"				+kmeans.getParameters();
			i++; o+="\n"+i+" --> Find best K";// for parameters";				+kmeans.getParameters();
			i++; o+="\n"+i+" --> Performance Measurement with different Threads";
			i++; o+="\n"+i+" --> Set K";
			i++; o+="\n"+i+" --> Set MaxIterations";
			i++; o+="\n"+i+" --> Set Runs";
			i++; o+="\n"+i+" --> Set InitializationSteps";
			//i++; o+="\n"+i+" --> Set Epsilon";
			return o;
		}
	}
	private static ProgrammOptions ShowMenu()	{
		int count=0;
		Scanner scan = new Scanner(System.in);
		do	{
	        System.out.println("\nParameter for calculation:\n"+kmeans.getParameters()+"\n");
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
//kmeans.setRuns(2);
        
        boolean exit=false;
        ProgrammOptions option;
        do	{
        	option=ShowMenu();
        	Scanner scan2=new Scanner(System.in);
        	Scanner scan3=new Scanner(System.in);
        	//int k;
        	//double distance=0.0;
	        switch (option)	{
		        case EXIT:
		        	break;
		        case Print_Labels:				
		        	data.print_labels();			
		        	break;
		        case KMeans:	   				
		        	System.out.println("distance="+kmeans.choosek());
		        	break;
		        	
		        case SetK:					
		           	System.out.println("Please enter K");
	                kmeans.setNum_clusters(scan2.nextInt());
		        	break;
		        case SetMaxIterations:
		           	System.out.println("Please enter MaxIterations");
	                kmeans.setMaxIterations(scan2.nextInt());
		        	break;
		        case SetRuns:					
		           	System.out.println("Please Runs");
	                kmeans.setRuns(scan2.nextInt());
		        	break;
		        case SetInitializationSteps:	
		           	System.out.println("Please enter InitializationSteps");
	                kmeans.setInitializationSteps(scan2.nextInt());
		        	break;
		        case SetEpsilon:				
		           	System.out.println("Please enter Epsilon");
	                kmeans.setEpsilon(scan2.nextDouble());
		        	break;
		        	
		        case FindK:
		        	int bestk=kmeans.findk2();
		        	kmeans.setNum_clusters(bestk);
		        	System.out.println("found k="+bestk);
		        	break;
		        case PerformanceMeasurement:
	            	System.out.println("Please enter a K");
	            	kmeans.setNum_clusters(scan2.nextInt());
	            	
	            	System.out.println("Please enter maximum number of maximal threads");
	            	int t=scan2.nextInt();
	            	
	            	//for(int i=1;i<=t;i++) {
		            for(int i=t;i>0;i--) {
	            		conf.setMaster("local["+t+"]");
	            		System.out.println("\nUsing "+i+" threads");
	            		
//	            		for(int j=5;j<100;j+=5) {
	            			long startTime=System.nanoTime();
//	                		K_Means.choosek(data.get_data(), k,j);
	            			kmeans.choosek();
	                		long endTime=System.nanoTime();
//	                		System.out.println("Execution with maxIter="+j+" , did need "+(endTime-startTime)+"ns\n");
	                		System.out.println("Threads="+i+" need "+(endTime-startTime)/1000000+"ms\n");
//	            		}
	            	}
            		conf.setMaster("local[*]");
		        	break;
	        	default: System.out.println("Input Error");
	        }
        }while(!exit);

        sc.close();
    }		
}
