package at.ac.univie.spark;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.SparkConf;


class RunSpark{

	public static void main(String [] args)
	{
		// local environment (laptop/PC)
		SparkConf conf = new SparkConf().setAppName("GRUPPE02").setMaster("local[*]");

		JavaSparkContext sc = new JavaSparkContext(conf);
		String AWS_ACCESS_KEY_ID = "";
		String AWS_SECRET_ACCESS_KEY = "";

		System.out.println(">>>>>>>>>>>>>>>>>> Hello from Spark! <<<<<<<<<<<<<<<<<<<<<<<<<");

//  ...

		// example accessing S3:

/*
    clusterMembers.saveAsTextFile("s3n://" + AWS_ACCESS_KEY_ID + ":" + AWS_SECRET_ACCESS_KEY + "@qltrail-lab-265-1488270472/result");
*/
		sc.stop();
	}

}
