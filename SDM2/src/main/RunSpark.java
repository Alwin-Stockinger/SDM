package main;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;
import org.apache.spark.SparkConf;
//import java.awt.List;
import java.util.Arrays;
import java.util.List;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

class RunSpark{

	public static void main(String [] args)
	{
		// on Google Cloud:
		//SparkConf conf = new SparkConf().setAppName("GRUPPEXX").setMaster("storage.googleapis.com");

		// local environment (laptop/PC)
		SparkConf conf = new SparkConf().setAppName("GRUPPEXX").setMaster("local[*]");
		
		
		JavaSparkContext sc = new JavaSparkContext(conf);
		sc.setLogLevel("ERROR");

				
		String AWS_ACCESS_KEY_ID = "";
		String AWS_SECRET_ACCESS_KEY = "";

		{	// count
			JavaRDD<String> textLoadRdd=sc.textFile("./src/main/RunSpark.java");
			System.out.println("Lines:"+textLoadRdd.count());
		}

		{	// map
			List data=(List) Arrays.asList(1,2,3,4,5);			System.out.println("data="+data);
			JavaRDD<Integer> distData=sc.parallelize((java.util.List<Integer>) data);
			JavaRDD<Integer> manipulated=distData.map(
					new Function<Integer,Integer>()  {
						public Integer call(Integer myInt) {
							myInt=myInt*2;
							//System.out.println(myInt);
							return myInt;
						}
					}
			);
			//JavaRDD<Integer> manipulated =distData.map(n ->new Integer(n) *2);
			List<Integer> tranformed = manipulated.collect();
			System.out.print("tranformed:"); for (Integer d : tranformed) System.out.print(d +" ");	System.out.println("");
		}
		
		


		// example accessing S3:

    //clusterMembers.saveAsTextFile("s3n://" + AWS_ACCESS_KEY_ID + ":" + AWS_SECRET_ACCESS_KEY + "@qltrail-lab-265-1488270472/result");

		sc.stop();

	}

}
