package SDM.Spark;

import java.util.Arrays;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

public class Data {
		private JavaRDD<Vector>	parsed_date;
		private static JavaSparkContext sc;
		private static String file;
		
		public Data(JavaSparkContext sc, String file_path)	{
			this.sc=sc;
			file=file_path;
			parsed_date=parse_data();
			System.out.println(parsed_date.count() + " Data parsed");
		}
		
		public JavaRDD<Vector> get_data()	{
			return parsed_date;
		}
		public static void print_labels()	{	//List the clustering labels (last column) and their distinct counts
            JavaRDD<String> labels=  sc.textFile(file).flatMap(l -> Arrays.asList(l.substring(l.lastIndexOf(",")+1 , l.length()-1)).iterator());
            Map<String, Long>    sum = labels.countByValue();
            //System.out.println("");		// warnungen,... erzeugen kein \n
            System.out.println("Label: " + sum.toString().trim());	        	        
	    }		
		
 		private static JavaRDD<Vector>  parse_data() {	// Remove all non numeric features
 			JavaRDD<String> rdd=sc.textFile(file);
 			return rdd.map( (Function<String, Vector>) s -> 
 	        		{  	String[] splitted = s.split(",");
 	                    double[] values = new double[splitted.length - 4];
 	                    int j = 0;
 	                    for (int i = 0; i < splitted.length; ++i) 	{
 	                        if(i!=1 && i!=2 && i!=3 &&i!=41)	{	// Eliminate row number 1,2,4 and 41
 	                          // kddcup.data_10_percent contains "x" values
 	                          try {
 	                        		values[j] = Double.parseDouble(splitted[i]);
 	 	                            j++;
 	                          }
 	                          catch (NumberFormatException e)	{
 	//                              System.out.println("Cannot parse value: " + splitted[i]); 
 	                          }   
 	                        }
 	                    }
 	                    return Vectors.dense(values);
 	                });
 	    }
}	
