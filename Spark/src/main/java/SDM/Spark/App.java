package SDM.Spark;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
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
        System.out.println( "Hello World!" );
        
        String filePath = "kddcup.data_10_percent";
        
        SparkConf conf = new SparkConf().setAppName("GRUPPE02").setMaster("local[*]");
        
        JavaSparkContext sc = new JavaSparkContext(conf);
        
        JavaRDD<String> rdd = sc.textFile(filePath);
		
		// Remove all non numeric features, to prepare rdd for Kmeans
		JavaRDD<Vector> parsedData = Parse_Data.parse_data(rdd);
		
		//List the clustering labels (last column) and their distinct counts
		Print_Labels.print_labels(rdd);
		
		// Send the parsed data to Kmeans method
		K_Means.Kmeans(parsedData);
		
		sc.close();
    }		
}
