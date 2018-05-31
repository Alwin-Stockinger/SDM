package data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataSet {

	List<DataPoint> dataPoints;
	
	public DataSet(String csvFile) {
		
		dataPoints = new ArrayList<>();
		try {
			parseData(csvFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void parseData(String csvFile) throws IOException {
		
		String line = "";
		String splitter = ",";
		BufferedReader reader = null;
		
		try {
			
			reader = new BufferedReader(new FileReader(csvFile));
			while((line = reader.readLine()) != null) {
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
	public ArrayList<Integer> getClusterNr() {
		ArrayList<Integer> c=new ArrayList();
		for (int i=0;i<dataPoints.size();i++)
			c.add((int) dataPoints.get(i).getClusterNr());
		return c;
	}
	public ArrayList<Integer> getTruthClusterNr() {
		ArrayList<Integer> c=new ArrayList();
		for (int i=0;i<dataPoints.size();i++)
			c.add((int) dataPoints.get(i).getTruthClusterNr());
		return c;
	}
	
	public List<DataPoint> getDataPoints() {
		return dataPoints;
	}
}
