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
        } catch (IOException e) {
            System.out.println("ERROR: "+e.getMessage());
        }
    }
     
    
	public static void main(String [] args)
	{


		// local environment (laptop/PC)
		SparkConf conf = new SparkConf().setAppName("GRUPPE02").setMaster("local[*]");

		JavaSparkContext sc = new JavaSparkContext(conf);
				
		JavaRDD<String> rdd = sc.textFile("/data/kddcup.data_10_percent");
				
		//Check first line of the data
		System.out.println(rdd.first());
			
		// Remove all non numeric features, to prepare rdd for Kmeans
		JavaRDD<Vector> parsedData = parse(rdd);
		
		//List the clustering labels (last column) and their distinct counts
		countLabels(rdd);

        
	    sc.stop();
    }
}
