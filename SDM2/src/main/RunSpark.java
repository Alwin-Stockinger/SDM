package main;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/*
import org.apache.calcite.linq4j.Linq4j;
import org.apache.calcite.linq4j.function.Function2;
import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;



//import java.awt.List;




import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;


import scala.Tuple2;import org.apache.spark.api.java.function.*;




import java.net.MalformedURLException;
import java.net.URL;*/

class RunSpark{

	public static void main(String [] args)
	{
		// on Google Cloud:
		//SparkConf conf = new SparkConf().setAppName("GRUPPEXX").setMaster("storage.googleapis.com");

		// local environment (laptop/PC)
		SparkConf conf = new SparkConf().setAppName("GRUPPE02").setMaster("local[*]");

		//scala.Tuple2<String,String>[] a=conf.getAll();
		//for(int i=0;i<a.length;i++)	System.out.println(a[i]._1 + "=" + a[i]._2);
			
		JavaSparkContext javaSparkContext = new JavaSparkContext(conf);
/*		sc.setLogLevel("ERROR");

		
				
		String AWS_ACCESS_KEY_ID = "";
		String AWS_SECRET_ACCESS_KEY = "";
*/
		/*{	// count
			JavaRDD<String> textLoadRdd=sc.textFile("./src/main/RunSpark.java");
			System.out.println("Lines:"+textLoadRdd.count());
		}*/

		/*{	// map
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
			//JavaRDD<Integer> manipulated =distData.map(n -javaSparkContext>new Integer(n) *2);
			List<Integer> tranformed = manipulated.collect();
			System.out.print("tranformed:"); for (Integer d : tranformed) System.out.print(d +" ");	System.out.println("");
		}*/
		
		//KddFeatures f=new KddFeatures();
		//for (int i=0;i<f.size();i++)	if(f.isNumeric(i)) System.out.println(f.Name(i)+"="+f.Type(i));
		
		// funktioniert noch nicht ganz
		// filtern soll dann mit f.isNumeric erfolgen
		JavaRDD<List<String>> rdd = javaSparkContext.textFile("./kddcup.data_10_percent")
			.filter(new Function<String, Boolean>() {
	            private static final long serialVersionUID = 1L;
	            @Override
	            public Boolean call(String v1) throws Exception {
	                String split[] = v1.split(",");
	                return split[1].equals("tcp") ;
	            }
	        })
			.mapToPair(new PairFunction<String, String, List<String>>() {
	            private static final long serialVersionUID = 1L;
	            @Override
	            public Tuple2<String, List<String>> call(String t) throws Exception {
	                String split[] = t.split(",");
	                List<String> list = new ArrayList<String>();
	                list.add(split[1].trim());
	                return new Tuple2<String, List<String>>(split[1].trim(), list);
	            }
	        })
			.reduceByKey(new Function2<List<String>, List<String>, List<String>>() {
	            private static final long serialVersionUID = 1L;
	            //@Override
	            public List<String> call(List<String> v1, List<String> v2) throws Exception {
	                List<String> list = new ArrayList<String>();
	                list.addAll(v1);
	                list.addAll(v2);
	                return list;
	            }
			}).values();
		

		List<List<String>> tranformed = rdd.collect();

		System.out.println("tranformed:");
		for (int i = 0; i < tranformed.size(); i++) {
			for (int j = 0; j < tranformed.get(i).size(); j++) {
				System.out.print(tranformed.get(i).get(j)+" ");
			}
			System.out.println("");
		}
		
		
		
		
		System.out.print("tranformed:"+tranformed);

		// example accessing S3:

    //clusterMembers.saveAsTextFile("s3n://" + AWS_ACCESS_KEY_ID + ":" + AWS_SECRET_ACCESS_KEY + "@qltrail-lab-265-1488270472/result");

//		sc.stop();

	}

}
