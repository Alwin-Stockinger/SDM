package SDM.Spark;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

public class Parse_Data {
	
			// Remove all non numeric features
	 		public static JavaRDD<Vector>  parse_data(JavaRDD<String> rdd) 
	 	    {
	 	        return rdd.map(
	 	                (Function<String, Vector>) s -> { 
	 	                	
	 	                	String[] splitted = s.split(",");
	 					
	 	                    double[] values = new double[splitted.length-4];
	 						
	 	                    int j = 0;
	 						
	 	                    for (int i = 0; i < splitted.length; ++i) 
	 						{
	 	                    	// Eliminate row number 1,2,4 and 41
	 	                        if(i!=1 && i!=2 && i!=3 &&i!=41)
	 							{
	 	                          // kddcup.data_10_percent contains "x" values
	 	                          try 
	 	                          {
	 	                        		values[j] = Double.parseDouble(splitted[i]);
	 	 	                            j++;
	 	                          }
	 	                          catch (NumberFormatException e)
	 	                          {
	 	                              System.out.println("ERROR with:" + splitted[i]); 
	 	                          }   
	 	                        }
	 	                    }
	 	                    return Vectors.dense(values);
	 	                }
	 	        );
	 	    }

}
