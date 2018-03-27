package visual;

import dataGenerator.*;
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

import java.io.BufferedWriter;
import java.io.FileWriter; 				// JAVA says this is conflicting...why?
import java.io.IOException;
import kmeans.Cluster;

public class GnuPlot {//https://github.com/mothlight/SUEWS-Graphs/blob/master/SUEWS_graphs/src/au/edu/monash/ges/suews/ProcessSUEWSRun.java

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
	public void generateDataFileFromPoints(	ArrayList<Point<Double>> points, 
			String outputDirectory, String filename,
			String xLabel, String yLabel, String dataFileName) throws IOException	{
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
	    
		StringBuffer outputStr = new StringBuffer();

		//outputStr.append(xLabel + " " + yLabel + '\n');

		for (int i = 0;i<points.size();i++)	{
			outputStr.append(points.get(i).get(0) + " " + points.get(i).get(1) + '\n');	// mehr dimensionen missachten (keine darstellung möglich)
		}
		//System.out.print("String: "+outputStr);

//		writeFile(outputStr.toString(), dataFileName);
	    writer.write(outputStr.toString());
	     
	    writer.close();

		//String outputFile = outputDirectory + File.separator + filename;
	}		

	public void plotData(String dataFileName, String outputBild, String yLabel, String outputDirectory,int cluster_count) {
		Plot.setGnuplotExecutable("gnuplot");
		Plot.setPlotDirectory(outputDirectory);
		Plot aPlot = new Plot();

		aPlot.setTitle("Plot for " + yLabel);
		aPlot.setGrid();
		aPlot.setKey("right box");
		aPlot.unsetParametric();
		aPlot.unsetPolar();
		aPlot.setOutput(Terminal.PNG, outputBild, " 1024,600  enhanced font Vera 14 ");
		aPlot.unsetLogscale();
		aPlot.setYTics("nomirror");
		aPlot.addExtra("set style line 1 linecolor rgbcolor \"#FF0000\" lw 2 pt 1 ps 1 pi 20"); //$NON-NLS-1$
//		aPlot.addExtra("set style line 2 linecolor rgbcolor \"#990000\" lw 2 pt 2 ps 1 pi 20"); //$NON-NLS-1$
//		aPlot.addExtra("set style line 3 linecolor rgbcolor \"#52015b\" lw 2 pt 3 ps 1 pi 20"); //$NON-NLS-1$
//		aPlot.addExtra("set style line 4 linecolor rgbcolor \"#988f03\" lw 2 pt 4 ps 1 pi 20"); //$NON-NLS-1$
//		aPlot.addExtra("set style line 5 linecolor rgbcolor \"#be7400\" lw 2 pt 5 ps 1 pi 20"); //$NON-NLS-1$
//		aPlot.addExtra("set style line 6 linecolor rgbcolor \"#00AA00\" lw 2 pt 6 ps 1 pi 20"); //$NON-NLS-1$
//		aPlot.addExtra("set style line 7 linecolor rgbcolor \"#00b7be\" lw 2 pt 7 ps 1 pi 20"); //$NON-NLS-1$
//		aPlot.addExtra("set style line 8 linecolor rgbcolor \"#808080\" lw 2 pt 8 ps 1 pi 20"); //$NON-NLS-1$
//		aPlot.addExtra("set style line 9 linecolor rgbcolor \"#d26584\" lw 2 pt 9 ps 1 pi 20"); //$NON-NLS-1$
//		aPlot.addExtra("set style line 10 linecolor rgbcolor \"#000000\" lw 2 pt 10 ps 1 pi 20"); //$NON-NLS-1$
//		aPlot.addExtra("set style line 11 linecolor rgbcolor \"#AA0000\" lw 2 pt 11 ps 1 pi 20"); //$NON-NLS-1$

		aPlot.addExtra("set tmargin 1");
		aPlot.addExtra("set bmargin 2");
		aPlot.addExtra("set lmargin 6");
		aPlot.addExtra("set rmargin 1");

		aPlot.addExtra("set dgrid3d 10,10,1");
		aPlot.addExtra("set ylabel \" " + yLabel + "\"");
		String xLabel = "Time";
		aPlot.addExtra("set xlabel \" " + xLabel + "\"");

		aPlot.setDataFileName(dataFileName);
		
		//http://jgnuplot.sourceforge.net/api/src-html/org/jgnuplot/Graph.html
		//Graph(String theDataFileName, String theUsing, String theDataModifiers, int theAxes, String theName, int theStyle, int theLineType, int thePointType)
		//Graph(String theDataFileName, String theUsing,                          int theAxes, String theName, int theStyle, int theLineType, int thePointType)
		//Graph(String theDataFileName, String theUsing,                          int theAxes,                 int theStyle, int theLineType, int thePointType)
		//Graph(String theDataFileName, String theUsing,                          int theAxes, String theName, int theStyle)		
		
		
		for(int i=0;i<cluster_count;i++)	{
			String r=(i*2+1)+":"+(i*2+2);
			aPlot.pushGraph(new Graph(dataFileName, r, Axes.X1Y1, yLabel, Style.POINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
		}

//		aPlot.addExtra ( "set style line 1 lc rgbcolor 'red' lt 1 lw 2 pt 1");
		
		//https://stackoverflow.com/questions/5315044/how-do-i-select-a-color-for-every-point-in-gnuplot-data-file
		//plot "file.dat" using 1:2:3 with points color rgb($4,$5,$6)
		
		try	{
			aPlot.plot();
			//aPlot.popGraph();
			//String[] commands = new String[]{Messages.getString("ProcessSUEWSRun.GNUPLOT_SH"),  outputDirectory };
			//Process aProcess = Runtime.getRuntime().exec(commands);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}	
	
	private ArrayList<Point<Double>> cluster_points(ArrayList<ArrayList<Double>> points, int dimension)	{
		ArrayList<Point<Double>> p=new ArrayList<Point<Double>>(); 
		for(int i=0 ; i<points.size(); i++)	{
			Point<Double> pn=new Point<Double>(dimension); 
			for(int j=0;j<dimension;j++)	pn.set(j, points.get(i).get(j));
			p.add(pn);
		}
		return p;
	}
		    
    public GnuPlot(ArrayList<Cluster> clusters,int dim) throws IOException {
    	
    	
    	String dataFileName="filename";
    	String outputBild="./bild.png";
    	String filename="filename";
    	String xLabel="";
    	String yLabel="";
    	String outputDirectory=".";
			
    	generateDataFileFromCluster(clusters,outputDirectory,filename);
    	
    	plotData(dataFileName, outputBild, yLabel, outputDirectory,clusters.size());
    	
    	
    }
 /*   public void GnuPlot(ArrayList<XYPoint> points) throws IOException {
    	
    	String dataFileName="filename";
    	String outputBild="./bild.png";
    	String filename="filename";
    	String xLabel="";
    	String yLabel="";
    	String outputDirectory=".";
			
    	generateDataFileFromPoints(points, outputDirectory, filename, xLabel, yLabel, dataFileName); 
    	generateDataFileFromCluster(clusters,outputDirectory,filename,"","");
    	plotData(dataFileName, outputBild, yLabel, outputDirectory);
    	
//        Plot.setGnuplotExecutable("gnuplot");
//        Plot.setPlotDirectory(".");
//
//        Plot aPlot = new Plot();
//        
//        
//        aPlot.setXRange("0", "13");
//        aPlot.setKey("box");
//        aPlot.setSamples("50");
//        aPlot.setDummy("t");
//        aPlot.addExtra("damp(t) = exp(-s*wn*t)/sqrt(1.0-s*s)");
//        aPlot.addExtra("per(t) = sin(wn*sqrt(1.0-s**2)*t - atan(-sqrt(1.0-s**2)/s))");
//        aPlot.addExtra("c(t) = 1-damp(t)*per(t)");
//        aPlot.addExtra("# wn is undamped natural frequency");
//        aPlot.addExtra("# s is damping factor");
//        aPlot.addExtra("wn = 1.0");
//        aPlot.pushGraph(new Graph("s=.1,c(t)", "1:2", Axes.X1Y1, "Name", Style.DOTS, LineType.NOT_SPECIFIED, PointType.POSTSCRIPT_DOT));
//
//        
//        aPlot.pushGraph(new Graph("s=.3,c(t)"));
////        aPlot.pushGraph(new Graph("s=.5,c(t)"));
////        aPlot.pushGraph(new Graph("s=.7,c(t)"));
////        aPlot.pushGraph(new Graph("s=.9,c(t)"));
////        aPlot.pushGraph(new Graph("s=1.0,c(t)"));
////        aPlot.pushGraph(new Graph("s=1.5,c(t)"));
////        aPlot.pushGraph(new Graph("s=2.0,c(t)"));
//        aPlot.addExtra("# plot c(t) for several different damping factors s");
//        aPlot.setOutput(Terminal.PNG, "test/out/controls-01.png");
//        try {
//           aPlot.plot();
//           
//        }
//        catch (Exception e) {
//           System.err.println(e);
//           System.exit(1);
//        }
     }
	*/	     
	
/*	public static void main(String[] args) throws IOException {
		SimpleDem s=new SimpleDem();
		
		ArrayList<XYPoint> points=new ArrayList();
		for (int i = 0;i<1500;i++)	{
			points.add(new XYPoint(i%4));
		}
		
		s.gnuplot(points);
	}
*/
}
