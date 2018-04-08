package main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KddFeatures {
	List<String> name=new ArrayList<String>();
	List<String> type=new ArrayList<String>();

	
	public KddFeatures()	{
		load();
	}
	
	private void load()	{
		try {
			WebData feature_set=new WebData(new URL("http://kdd.ics.uci.edu/databases/kddcup99/kddcup.names"));
			
			for (int i=1;i<feature_set.size();i++)	{	// erste line (0) alle typen (nur name) komma getrennt
				String[] parts = feature_set.line(i).split(": ");
				name.add(parts[0]);
				type.add(parts[1].replace(".",""));
				//System.out.println(parts[0] + "-" +parts[1]);
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int size()	{
		return name.size();
	}
	public String Name(int pos)	{
		return name.get(pos);
	}
	public String Type(int pos)	{
		return type.get(pos);
	}
	public Boolean isNumeric(int pos)	{
		return Type(pos).equals("continuous");
	}	
}
