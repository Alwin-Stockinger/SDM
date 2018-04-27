package SDM.Spark;

import java.util.Arrays;
import java.util.Map;
import org.apache.spark.api.java.JavaRDD;

public class Print_Labels {
	
		//List the clustering labels (last column) and their distinct counts
		public static void print_labels(JavaRDD<String> rdd)
		{
	            JavaRDD<String> labels =  rdd.flatMap(l -> Arrays.asList(l.substring(l.lastIndexOf(",")+1 , l.length()-1)).iterator());
	            
	            Map<String, Long> sum = labels.countByValue();
	            
	            System.out.println("Label: " + sum.toString());	        	        
	    }

}
