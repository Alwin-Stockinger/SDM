package visual;


import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import kmeans.Cluster;

public class XYPlane extends ApplicationFrame{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XYPlane(ArrayList<Cluster> clusters) {
		super("Gaussian Distributed Clusters");
		
		
		
		
		final XYSeriesCollection collec=new XYSeriesCollection();
		final XYSeries[] series=new XYSeries[clusters.size()];
		for(int j=0;j<clusters.size();j++) {
			final XYSeries ser=new XYSeries("Cluster"+(j+1));
			series[j]=ser;
			for(int i=0;i<clusters.get(j).getPoints().size();++i) {
				series[j].add(clusters.get(j).getPoints().get(i).get(0),clusters.get(j).getPoints().get(i).get(1));
			}
			collec.addSeries(series[j]);
		}
		
		final JFreeChart chart=ChartFactory.createScatterPlot("Gaussian Distributed Clusters", "x-Axis", "y-Axis", collec);
		
		final ChartPanel chartPanel= new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500,500));
		setContentPane(chartPanel);
		
	}
	
	
}
