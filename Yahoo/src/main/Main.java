package main;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.linalg.Vector;

import scala.Tuple2;





public class Main {

	public static void main(String[] args) {
		
		String filePath = "trainIdx2.txt";
        
        SparkConf conf = new SparkConf().setAppName("GRUPPE02").setMaster("local[*]");
        
        JavaSparkContext sc = new JavaSparkContext(conf);
        
        JavaRDD<String> rdd = sc.textFile(filePath);
        
        //remove Users
        JavaRDD<String> rdds=rdd.filter(s->!s.contains("|"));
        
        
        /*rdds.foreach(set->{
        	System.out.println("Set="+set);
        });*/
        
        JavaPairRDD<String,Double> pairRdd=rdds.mapToPair(s->{
        	String[] split =s.split("\t");
        	//System.out.println(x);
        	return new Tuple2<String, Double>(split[0],Double.parseDouble(split[1]));
        });
        
        
        // adds 1 to ever pair, for dividing later
        JavaPairRDD<String, Tuple2<Double, Double>> valueRdd = pairRdd.mapValues(d -> new Tuple2<Double, Double>(d,1.));
        
        //sums everthing up
        JavaPairRDD<String, Tuple2<Double, Double>> reducedRdd = valueRdd.reduceByKey((tuple1,tuple2) ->  new Tuple2<Double, Double>(tuple1._1 + tuple2._1, tuple1._2 + tuple2._2));
        
        //calculates the averages
        JavaPairRDD<String, Double> averageRdd = reducedRdd.mapToPair(getAverageByKey);
        
        averageRdd.foreach(set->{
        	System.out.println("Album="+set._1()+" Avg_Rating="+set._2());
        });
        
        sc.stop();
        sc.close();
        
	}
	
	private static PairFunction<Tuple2<String, Tuple2<Double, Double>>,String,Double> getAverageByKey = (tuple) -> {
	     Tuple2<Double, Double> val = tuple._2;
	     double total = val._1;
	     double count = val._2;
	     Tuple2<String, Double> averagePair = new Tuple2<String, Double>(tuple._1, total / count);
	     return averagePair;
	  };
	

}
