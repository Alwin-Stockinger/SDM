package SDM.Spark;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;

public class Distance {

	
	// Define a Euclidean distance function, and a function that returns the distance from a data point 
	 		// to its nearest clusterâ€™s centroid.
	 		public static double euclidian(KMeansModel clusters, JavaRDD<Vector> parsedrdd)
	 		{
	 	        double euclidian_distance;
	 	        
	 	        // Type must be Vector[]
	 	        Vector[] center_of_cluster = clusters.clusterCenters();
	 	        
	 	        // function for computing euclidian distance
	 	        euclidian_distance = parsedrdd.map((Function<Vector, Double>) a -> 
	 	        {
	 	        	double sum = 0;
	 	        
	 	        	// Returns the cluster index that a given point belongs to (Type Integer)
	 	            int q = clusters.predict(a);            
	 	            
	 	            	for (int i = 0; i < a.size(); i++)
	 	            	{
	 	            		//summe = (q - p)^2 = (q - p) * (q - p)
	 	            		// apply(i) --> Gets the value of the ith element (Type double)
	 	            		sum += (center_of_cluster[q].apply(i) - a.toArray()[i]) * (center_of_cluster[q].apply(i) - a.toArray()[i]);
	 	            	}
	 	        
	 	        // return square root of summe
	 	        return Math.sqrt(sum);
	 	        	
	 	        }
	 	        ).reduce((a, b) -> a + b);

	 	        return euclidian_distance;
	 	    }
}
