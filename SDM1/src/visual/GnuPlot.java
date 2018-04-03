package visual;

import kmeans.Cluster;

import java.io.BufferedWriter;
import java.util.ArrayList;

// http://jgnuplot.sourceforge.net/api/index.html
import org.jgnuplot.Axes;
import org.jgnuplot.Graph;
import org.jgnuplot.LineType;
import org.jgnuplot.Plot;
import org.jgnuplot.PointType;
import org.jgnuplot.Style;
import org.jgnuplot.Terminal;


import java.io.FileWriter; 				
import java.io.IOException;


public class GnuPlot {
	final String filename_png="./K-means.png";
	final String data_file="data_file";
	final String xLabel="x-Value";
	final String yLabel="y-Value";
	final String outputDirectory=".";

    public GnuPlot(ArrayList<Cluster> clusters,int dim, String imageLabel) throws IOException {
    	generateDataFileFromCluster(clusters,outputDirectory,data_file);
    	plotData(clusters.size(),imageLabel);
    }

	public void generateDataFileFromCluster(	ArrayList<Cluster> clusters, 
												String outputDirectory, String filename) throws IOException	{
		generateDataFileFromCluster(clusters,outputDirectory,filename,"","");
	}
	public void generateDataFileFromCluster(	ArrayList<Cluster> clusters, 
												String outputDirectory, String filename,
												String xLabel, String yLabel) throws IOException	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		//outputStr.append(xLabel + " " + yLabel + '\n');
		int clusters_aktiv=clusters.size();	
		int i=0;
		while (clusters_aktiv>0){
			StringBuffer outputStr = new StringBuffer();
			for (int c = 0;c<clusters.size();c++)	{ 
				if(i<clusters.get(c).size())	{	// punkt vorhanden
		    		ArrayList<Double> P=clusters.get(c).getPoint(i);
	    			outputStr.append(P.get(0) + " " + P.get(1)+" " );	// mehr dimensionen missachten (keine darstellung möglich)
				} else {
	    			outputStr.append("- - ");
					if (clusters.get(c).size()==i)	clusters_aktiv--;
				}
			}
			outputStr.append('\n');
    	    writer.write(outputStr.toString());
    	    i++;
		}
	    writer.close();
	}

	public void plotData(int cluster_count, String titel) {

		Plot.setGnuplotExecutable("gnuplot");
		Plot.setPlotDirectory(outputDirectory);
		Plot aPlot = new Plot();

		aPlot.setTitle(titel);
		aPlot.setGrid();
		//aPlot.setKey("right box");
//		aPlot.unsetParametric();
//		aPlot.unsetPolar();
		aPlot.setOutput(Terminal.PNG, filename_png, " 1024,600  enhanced font Vera 14 ");
//		aPlot.unsetLogscale();
//		aPlot.setYTics("nomirror");

//		aPlot.addExtra("set tmargin 1");
//		aPlot.addExtra("set bmargin 2");
//		aPlot.addExtra("set lmargin 6");
//		aPlot.addExtra("set rmargin 1");

		aPlot.addExtra("set dgrid3d 10,10,1");
		aPlot.addExtra("set ylabel \" " + yLabel + "\"");
		aPlot.addExtra("set xlabel \" " + xLabel + "\"");

		aPlot.setDataFileName(data_file);
		
		for(int i=0;i<cluster_count;i++)	{
			String r=(i*2+1)+":"+(i*2+2);
			aPlot.pushGraph(new Graph(data_file, r, Axes.X1Y1, yLabel, Style.POINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
		}
		
		try	{
			aPlot.plot();
		} catch (Exception e)	{
			e.printStackTrace();
		}
	}	
	




}
