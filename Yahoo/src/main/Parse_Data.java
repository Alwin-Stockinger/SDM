package main;

import java.util.ArrayList;

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
	 	                	
	 	                	String[] splitted = s.split(" ");
	 					
	 	                	ArrayList<Double> values=new ArrayList<Double>(); //double[] values = new double[splitted.length - 4];
	 						
	 	                    
	 						
	 	                    for (int i = 0; i < splitted.length; ++i) 
	 						{
	 	                    	if(!splitted[i].contains("|")) {
	 	                    		values.add(Double.parseDouble(splitted[i]));
	 	                    		
	 	                    	}
	 	                    }
	 	                    
	 	                    double[] val=new double[values.size()];
	 	                    for(int i=0; i<val.length;i++) {
	 	                    	val[i]=values.get(i);
	 	                    }
	 	                    
	 	                    
	 	                    return Vectors.dense(val);
	 	                }
	 	        );
	 	    }

}
