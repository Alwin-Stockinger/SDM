package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class WebData {
	List<String> lines=new ArrayList<String>();
	
	public WebData(URL url)	{
		//lines=new ArrayList<String>();
		try {
			download(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void download(URL url) throws IOException {
        URLConnection con = url.openConnection();
        InputStream is =con.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = null;

        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
    }

	public int size()	{
		return lines.size();
	}
	public String line(int pos)	{
		return lines.get(pos);
	}

	
}
