package main;

import data.DataPoint;
import data.DataSet;
import Hashfunktions.LSH;
import java.util.ArrayList;
import java.util.Collections;


public class Main {
	static final String TEST_SETTINGS_FILE= "find_settings.txt";
	
	public static void main(String[] args) {
		
		boolean run_find_settings=true;

        DataSet bigDataSet = new DataSet("LSH-nmi.csv");
        //sparkTest(bigDataSet);
        
		if (run_find_settings)	{
			TestSettings test=new TestSettings(TEST_SETTINGS_FILE);
			

	        LSH lsh = new LSH(test.getSettings(0), 15);
	        lsh.hash(bigDataSet.getDataPoints());
			
			for (int i=1;i<test.size();i++)	{
				System.out.println();
		        LSH otherLsh = new LSH(test.getSettings(i), 15);
		        otherLsh.hash(bigDataSet.getDataPoints());
				System.out.println("Line:" + i);
		        String operator=test.getOperator(i-1);
		        if(operator.equals("AND"))	{
			        lsh.combineHashOR(bigDataSet.getDataPoints(), otherLsh);
		        } else if(operator.equals("OR"))	{
			        lsh.combineHashOR(bigDataSet.getDataPoints(), otherLsh);
		        } else {
					System.out.println("operator error:" + operator);
		        }
		        double nmi=NMI(bigDataSet.getTruthCluster(),bigDataSet.getCluster());	
			}
		} else {
		
			TimeMeasurement time = new TimeMeasurement();
			time.Start();
			
			System.out.println("Starting LSH algorithm");
	
	
	        System.out.println("Test with 15 buckets");
	
	        double[] p = new double[10];
	
	        for (int i = 0; i < 10; ++i) {
	            p[i] = 1.0;
	        }
	
	        double[] p1 = new double[10];
	
	        for (int i = 0; i < 10; ++i) {
	            if (i % 2 == 0) {
	                p1[i] = 1.0;
	            } else {
	                p1[i] = -1.0;
	            }
	        }
	
	        LSH lsh = new LSH(p, 15);
	        LSH otherLsh = new LSH(p1, 15);
	        lsh.hash(bigDataSet.getDataPoints());
	        /*for (int i = 0; i < lsh.getBucketNumber(); ++i) {
	            double[] vals = zeroVals();
	            for (DataPoint dp : lsh.getBuckets()[i].getDataPoints()) {
	                for (int j = 0; j < vals.length; ++j) {
	                    vals[j] += dp.getVector()[j];
	                }
	            }
	            scalarDiv(vals, lsh.getBuckets()[i].getDataPoints().size());
	            for (int k = 0; k < vals.length; ++k) {
	               System.out.print((int) vals[k] + " , ");
	            }
	            System.out.println();
	        }*/
	        
	        System.out.println("\ncombine with or:\n");
	        otherLsh.hash(bigDataSet.getDataPoints());
	        lsh.combineHashOR(bigDataSet.getDataPoints(), otherLsh);
	
	        /*for (int i = 0; i < lsh.getBucketNumber(); ++i) {
	            double[] vals = zeroVals();
	            for (DataPoint dp : lsh.getBuckets()[i].getDataPoints()) {
	                for (int j = 0; j < vals.length; ++j) {
	                    vals[j] += dp.getVector()[j];
	                }
	            }
	            scalarDiv(vals, lsh.getBuckets()[i].getDataPoints().size());
	            for (int k = 0; k < vals.length; ++k) {
	                System.out.print((int) vals[k] + " , ");
	            }
	            System.out.println();
	        }*/
	        
	        //System.out.println(bigDataSet.getTruthCluster());
	        //System.out.println(bigDataSet.getCluster());
	        double nmi=NMI(bigDataSet.getTruthCluster(),bigDataSet.getCluster());
	        
	        time.Stop();
	        System.out.println("Time: "+time.get()+"ms");
		}
        
	}

	private static void scalarDiv(double[] vector, double scalar) {
	    for (int i = 0; i < vector.length; ++i) {
	        vector[i] /= scalar;
        }
    }

	private static double[] zeroVals() {

	    double[] vals = new double[10];
	    for (int i = 0; i < vals.length; ++i) {
	        vals[i] = 0.0;
        }
	    return vals;
    }
	/*
	private static void sparkTest(DataSet bigDataSet) {

        System.out.println("KMeans Classification using spark MLlib in Java . . .");

        // hadoop home dir [path to bin folder containing winutils.exe]
        System.setProperty("hadoop.home.dir", "C:\\winutils\\hadoop\\");

        SparkConf conf = new SparkConf().setAppName("JavaKMeansExample")
                .setMaster("local[2]")
                .set("spark.executor.memory", "3g")
                .set("spark.driver.memory", "3g");

        JavaSparkContext jsc = new JavaSparkContext(conf);

        JavaRDD<String> data = jsc.textFile("LSH-nmi.csv");
        JavaRDD parsedData = data.map(s -> {
            String[] sarray = s.split(",");
            double[] values = new double[sarray.length-1];
            for (int i = 0; i < sarray.length-1; i++) {
                values[i] = Double.parseDouble(sarray[i]);
            }
            return Vectors.dense(values);
        });
        parsedData.cache();

        // Cluster the data into three classes using KMeans
        int numClusters = 15;
        int numIterations = 50;
        KMeansModel clusters = KMeans.train(parsedData.rdd(), numClusters, numIterations);

        System.out.println("\n*****Training*****");
        int clusterNumber = 0;
        for (Vector center: clusters.clusterCenters()) {
            System.out.println("Cluster center for Cluster "+ (clusterNumber++) + " : " + center);
        }
        double cost = clusters.computeCost(parsedData.rdd());
        System.out.println("\nCost: " + cost);

        DataSet testDataSet = new DataSet("test.csv");
        for (DataPoint dataPoint : testDataSet.getDataPoints()) {
            System.out.println("predicted Cluster: " + clusters.predict(Vectors.dense(dataPoint.getVector())) + "   Ground truth: " + dataPoint.getTruth());
        }

        int[][] matrix = getZeroMatrix();
        for (DataPoint dataPoint : bigDataSet.getDataPoints()) {
            int i = clusters.predict(Vectors.dense(dataPoint.getVector()));
            int j = (int) Double.parseDouble(dataPoint.getTruth().replace("class", ""));
            matrix[i][j]++;
        }
        int sum = 0;
        for (int i = 0; i < 15; ++i) {
            for (int j = 0; j < 16; ++j) {
                System.out.print(matrix[i][j] + " ");
                sum += matrix[i][j];
            }
            System.out.print("\n");
        }
        System.out.println("Sum: " + sum);
    }
	 */
	private static int[][] getZeroMatrix() {
        int[][] matrix = new int[15][16];
        for (int i = 0; i < 15; ++i) {
            for (int j = 0; j < 16; ++j) {
                matrix[i][j] = 0;
            }
        }
        return matrix;
    }

	public static double NMI(ArrayList<Integer> one, ArrayList<Integer> two){
		if(one.size()!=two.size()){
			throw new IllegalArgumentException("Sizes don't match!");
		}
		int maxone = Collections.max(one);
		int maxtwo = Collections.max(two);

		double[][] count = new double[maxone+1][maxtwo+1];
		//System.out.println(count[1][2]);
		for(int i=0;i<one.size();i++){
			count[one.get(i)][two.get(i)]++;
		}
		//i<maxone=R
		//j<maxtwo=C
		double[] bj = new double[maxtwo+1];
		double[] ai = new double[maxone+1];

		for(int m=0;m<(maxtwo+1);m++){
			for(int l=0;l<(maxone+1);l++){
				bj[m]=bj[m]+count[l][m];
			}
		}
		for(int m=0;m<(maxone+1);m++){
			for(int l=0;l<(maxtwo+1);l++){
				ai[m]=ai[m]+count[m][l];
			}
		}

		double N=0;
		for(int i=0;i<ai.length;i++){
			N=N+ai[i];
		}
		double HU = 0;
		for(int l=0;l<ai.length;l++){
			double c=0;
			c=(ai[l]/N);
			if(c>0){
				HU=HU-c*Math.log(c);
			}
		}

		double HV = 0;
		for(int l=0;l<bj.length;l++){
			double c=0;
			c=(bj[l]/N);
			if(c>0){
				HV=HV-c*Math.log(c);
			}
		}
		double HUstrichV=0;
		for(int i=0;i<(maxone+1);i++){
			for(int j=0;j<(maxtwo+1);j++){
				if(count[i][j]>0){
					HUstrichV=HUstrichV-count[i][j]/N*Math.log(((count[i][j])/(bj[j])));
				}
			}
		}
		double IUV = HU-HUstrichV;
		double reto = IUV/(Math.max(HU, HV));

		System.out.println("NMI: "+reto);
		return reto;
	}


}
