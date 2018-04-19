package main;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class MainTasks {

	public static void main(String[] args) {
		
		System.out.println( "Starting the main program" );
        //System.setProperty("hadoop.home.dir", "C:\\winutils\\hadoop\\");	//HADOOP has to be present!!!
        SparkConf conf = new SparkConf().setAppName("GRUPPE02").setMaster("local[*]");	
		JavaSparkContext javaSparkContext = new JavaSparkContext(conf);
		
		System.out.println("\n\nStarting Task 1");
		System.out.println("\nList the clustering labels (last column) and their distinct counts.");
		System.out.println("\nFirst a short test for correctness is performed");
		
		String filePath = "kddTest.txt";
        JavaRDD<String> lines = javaSparkContext.textFile(filePath);

        JavaPairRDD<String, Integer> count =
                lines	.map(line -> {
                			String[] parts = line.split(",");
                			//javaSparkContext.close();
                			return parts[parts.length-1];
                		})
                        .mapToPair(w -> new Tuple2<String, Integer>(w, 1))
                        .reduceByKey((x, y) -> x + y);

        // this line produces a new directory and saves the file as part-00000
        // if the directory already exists, it has to be removed beforehand,
        // otherwise an exception is thrown
        count.saveAsTextFile("results");
        
		for(Tuple2<String, Integer> label : count.collect()) {
			System.out.println("Label: " + label._1 + " : " + label._2);
		}
		javaSparkContext.close();
	}
}
