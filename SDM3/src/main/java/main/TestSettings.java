package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import data.DataPoint;
 
public class TestSettings {
	static final String COLUMNS_SPLITTER= ",";
	static final String FILE_KOMMENTAR= "#";
	ArrayList<String>	data=new ArrayList<String>();
	ArrayList<String>	operator=new ArrayList<String>();

	public TestSettings(String filename) 	{
		try {
			pars_file(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	int size()	{
		return data.size();
	}
	double[] getSettings (int pos)	{
		return split(pos);
	}
	String getOperator (int pos)	{
		return operator.get(pos);
	}


	
	private double[] split(int pos) {
		String[] d = data.get(pos).split(COLUMNS_SPLITTER);
		double[] ret = new double[d.length];
		for(int i = 0; i < d.length; ++i) {
			ret[i] = Double.parseDouble(d[i]);
		}
		return ret;
	}
	
	
	private void pars_file(String filename)	throws IOException {
		String line = "";
		ArrayList<String>	tmp=new ArrayList<String>();
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
			while((line = reader.readLine()) != null) {
				if (!line.startsWith(FILE_KOMMENTAR))	tmp.add(line);
			}
			data.add(tmp.get(0));
			for(int i=2;i<tmp.size();i+=2)	{
				operator.add(tmp.get(i-1));
				data.add(tmp.get(i));
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
}





