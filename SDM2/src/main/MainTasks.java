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
		
		listLabelsAndCounts(javaSparkContext, "kddTest.txt");
		
		System.out.println("Remove non numeric features");
		JavaRDD<String> lines = removeNonNumericFeatures(javaSparkContext, "kddTest.txt");
		printRDD(lines);
		
		System.out.println("Starting K - Means test");
		simpleKmeans(javaSparkContext, "kddTest.txt");
		
		javaSparkContext.close();
	}
	
	public static void simpleKmeans(JavaSparkContext javaSparkContext, String filePath) {
    	
    	JavaRDD<String> lines = removeNonNumericFeatures(javaSparkContext, filePath);
    	//JavaRDD<String> lines = javaSparkContext.textFile(filePath);
    	JavaRDD<Vector> numbers = lines.map(line -> {
    		String[] parts = line.split(",");
    		double[] values = new double[parts.length];
    		int i = 0;
    		for(String part : parts) {
    			values[i++] = Double.parseDouble(part);
    		}
    		return Vectors.dense(values);
    	});
    	numbers.cache();
    	
    	KMeans kmeans = new KMeans();
    	// k = 2 and maxIterations = 20 are the default parameters
    	KMeansModel model = KMeans.train(numbers.rdd(), 2, 20);
    	
    	System.out.println("Training completed");
    	int clusterNumber = 0;
    	for(Vector center : model.clusterCenters()) {
    		System.out.println("Cluster " + ++clusterNumber + " : " + center);
    	}
    	double cost = model.computeCost(numbers.rdd());
    	System.out.println("Cost: " + cost);
    }
	
	public static void listLabelsAndCounts(JavaSparkContext javaSparkContext, String filePath) {
    	
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
        
        //System.out.println(count.collect().toString());
		for(Tuple2<String, Integer> label : count.collect()) {
			System.out.println("Label: " + label._1 + " : " + label._2);
		}
    }
	
public static void printRDD(JavaRDD<String> lines) {
    	
    	for(String line : lines.collect()) {
    		System.out.println(line);
    	}
    }
    
    public static JavaRDD<String> removeNonNumericFeatures(JavaSparkContext javaSparkContext, String filePath) {
    	
    	JavaRDD<String> lines = javaSparkContext.textFile(filePath);
    	lines = lines.map(line -> {
    		String[] parts = line.split(",");
    		String newLine = buildLineWithoutNumericFeatures(parts);
    		return newLine;
    	});
    	return lines;
    }
    
    public static String buildLineWithoutNumericFeatures(String[] parts) {
    	
    	String newLine = "";
    	// it is assumed that the lines always have the same format, therefore
    	// this part can be build hardcoded
    	newLine += parts[0];
    	int length = parts.length - 2;
    	for(int i = 4; i < length; ++i) {
    		newLine += ',' + parts[i];
    	}
    	return newLine;
    }
}
