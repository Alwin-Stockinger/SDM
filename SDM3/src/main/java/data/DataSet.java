package data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataSet {

	private List<DataPoint> dataPoints;
	
	public DataSet(String csvFile) {
		
		dataPoints = new ArrayList<>();
		try {
			parseData(csvFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void parseData(String csvFile) throws IOException {
		
		String line = "";
		String splitter = ",";
		BufferedReader reader = null;
		int line_count=0;
		try {
			
			reader = new BufferedReader(new FileReader(csvFile));
			while((line = reader.readLine()) != null) {
				line_count++;	//if (line_count==10) break;
				String[] data = line.split(splitter);
				double[] vectors = getVectors(data);
				String groundTruth = (String)data[10];
				dataPoints.add(new DataPoint(vectors, groundTruth));
			}
		} catch (FileNotFoundException e) {
			System.out.println("File was not found");
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private double[] getVectors(String[] data) {
		
		double[] vectors = new double[10];
		for(int i = 0; i < 10; ++i) {
			
			vectors[i] = Double.parseDouble(data[i]);
		}
		return vectors;
	}
	public ArrayList<Integer> getCluster() {
		ArrayList<Integer> c=new ArrayList();
		for (int i=0;i<dataPoints.size();i++)
			c.add((int) dataPoints.get(i).getCluster());
		return c;
	}
	public ArrayList<Integer> getTruthCluster() {
		ArrayList<Integer> c=new ArrayList();
		for (int i=0;i<dataPoints.size();i++)
			c.add((int) dataPoints.get(i).getTruthClusterNr());
		return c;
	}
	
	public List<DataPoint> getDataPoints() {
		return dataPoints;
	}
}
