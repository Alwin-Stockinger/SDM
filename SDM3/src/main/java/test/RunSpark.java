package test;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

public class RunSpark {

    public static void main( String[] args ) {
        System.out.println("KMeans Classification using spark MLlib in Java . . .");

        // hadoop home dir [path to bin folder containing winutils.exe]
        System.setProperty("hadoop.home.dir", "C:\\winutils\\hadoop\\");

        SparkConf conf = new SparkConf().setAppName("JavaKMeansExample")
                .setMaster("local[2]")
                .set("spark.executor.memory", "3g")
                .set("spark.driver.memory", "3g");

        JavaSparkContext jsc = new JavaSparkContext(conf);

        // Load and parse data
        String path = "training.txt";
        JavaRDD<String> data = jsc.textFile(path);
        JavaRDD parsedData = data.map(s -> {
            String[] sarray = s.split(" ");
            double[] values = new double[sarray.length];
            for (int i = 0; i < sarray.length; i++) {
                values[i] = Double.parseDouble(sarray[i]);
            }
            return Vectors.dense(values);
        });
        parsedData.cache();

        // Cluster the data into three classes using KMeans
        int numClusters = 3;
        int numIterations = 20;
        KMeansModel clusters = KMeans.train(parsedData.rdd(), numClusters, numIterations);

        System.out.println("\n*****Training*****");
        int clusterNumber = 0;
        for (Vector center: clusters.clusterCenters()) {
            System.out.println("Cluster center for Cluster "+ (clusterNumber++) + " : " + center);
        }
        double cost = clusters.computeCost(parsedData.rdd());
        System.out.println("\nCost: " + cost);

        // Evaluate clustering by computing Within Set Sum of Squared Errors
        double WSSSE = clusters.computeCost(parsedData.rdd());
        System.out.println("Within Set Sum of Squared Errors = " + WSSSE);

        jsc.stop();
    }
}
