package at.ac.univie.spark;

import org.apache.spark.api.java.JavaSparkContext;

import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.rdd.RDD;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.JavaPairRDD;


import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.spark.SparkConf;


import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

   
class RunSpark{
    
	
	// Remove all non numeric features
	private static JavaRDD<Vector>  parse(JavaRDD<String> rdd) 
    {
        return rdd.map(
                (Function<String, Vector>) s -> { String[] splitted = s.split(",");
				
                    double[] values = new double[splitted.length-4];
					
                    int j = 0;
					
                    for (int i = 0; i < splitted.length; i++) 
					{
                        if(i!=1 && i!=2 && i!=3 &&i!=41)
						{
                            values[j] = Double.parseDouble(splitted[i]);
                            j++;
                        }

                    }
                    return Vectors.dense(values);
                }
        );
    }
	
	//List the clustering labels (last column) and their distinct counts
	private static void countLabels(JavaRDD<String> rdd){
        try{
            PrintWriter writer = new PrintWriter("labels"+".txt", "UTF-8");
            
            JavaRDD<String> words =  rdd.flatMap(l -> Arrays.asList(l.substring(l.lastIndexOf(",")+1 , l.length()-1)).iterator());
            
            Map<String, Long> wCount2= words.countByValue();
            
            writer.println(wCount2.toString());

            writer.close();
        } 
        catch (IOException e) 
        {
            System.out.println("ERROR: "+e.getMessage());
        }
    }
    
	// Define a Euclidean distance function, and a function that returns the distance from a data point 
	// to its nearest cluster’s centroid.
	private static double euclidian(KMeansModel clusters, JavaRDD<Vector> parsedrdd)
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
	
	// Kmeans Algorithm of Spark
	private static void Kmeans(JavaRDD<Vector> parsedrdd) 
	{         
           // Kmeans Algorithm of apache spark
           KMeans kmeans = new KMeans();
           
           // A clustering model for K-means of apache spark
           KMeansModel clusters = kmeans.run(parsedrdd.rdd());
          
           // Implemented Distance function --> pass clusters and parsedrdd to method
           Double euDist = euclidian(clusters,parsedrdd);
           
           // Return the number of elements in the RDD
           long Elements_rdd = parsedrdd.rdd().count();
           
           // using the overall average distance as our quality score for the clustering result
           long average_distance = (long) (euDist / Elements_rdd);
           
           System.out.println("Average Distance: " + average_distance);                
   } 
	 
    
	public static void main(String [] args)
	{


		// local environment (laptop/PC)
		SparkConf conf = new SparkConf().setAppName("GRUPPE02").setMaster("local[*]");

		JavaSparkContext sc = new JavaSparkContext(conf);
				
		JavaRDD<String> rdd = sc.textFile("/data/kddcup.data_10_percent");
				
		// Check first line of the data
		System.out.println(rdd.first());
			
		// Remove all non numeric features, to prepare rdd for Kmeans
		JavaRDD<Vector> parsedData = parse(rdd);
		
		//List the clustering labels (last column) and their distinct counts
		countLabels(rdd);
		
		// Send the parsed data to Kmeans method
		Kmeans(parsedData);

        
	    sc.stop();
    }
}
