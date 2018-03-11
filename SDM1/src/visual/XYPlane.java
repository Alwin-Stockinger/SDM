package visual;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class XYPlane extends ApplicationFrame{
	
	
	
	public XYPlane(double[][] data) {
		super("Gaussian Distributed Clusters");
		
		final XYSeries series=new XYSeries("Data Points");
		
		for(int i=0;i<data.length;++i) {
			series.add(data[i][0],data[i][1]);
		}
		final XYSeriesCollection collec=new XYSeriesCollection(series);
		final JFreeChart chart=ChartFactory.createScatterPlot("Gaussian Distributed Clusters", "x-Axis", "y-Axis", collec);
		
		final ChartPanel chartPanel= new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500,500));
		setContentPane(chartPanel);
		
	}
	
	
}
