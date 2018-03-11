package visual;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class XYPlane extends ApplicationFrame{
	
	
	
	public XYPlane(double[][] data, int clusters) {
		super("Gaussian Distributed Clusters");
		
		final XYSeriesCollection collec=new XYSeriesCollection();
		final XYSeries[] series=new XYSeries[clusters];
		for(int j=0;j<clusters;j++) {
			final XYSeries ser=new XYSeries("Cluster"+(j+1));
			series[j]=ser;
			for(int i=0;i<data.length/clusters;++i) {
				series[j].add(data[j*data.length/clusters+i][0],data[j*data.length/clusters+i][1]);
			}
			collec.addSeries(series[j]);
		}
		
		final JFreeChart chart=ChartFactory.createScatterPlot("Gaussian Distributed Clusters", "x-Axis", "y-Axis", collec);
		
		final ChartPanel chartPanel= new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500,500));
		setContentPane(chartPanel);
		
	}
	
	
}
