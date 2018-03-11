package main;

import org.jfree.ui.RefineryUtilities;

import dataGenerator.ClusterGenerator;
import visual.XYPlane;

public class Main {

	public static void main(String[] args) {
		/*int clusters=Integer.parseInt(args[0]);
		int points=Integer.parseInt(args[1]);
		int dim=Integer.parseInt(args[2]);
		*/
		int clusters=5;
		int points=100;
		int dim=2;
		
		ClusterGenerator gen=new ClusterGenerator();
		double data[][]=gen.generate(clusters, points, dim);
		
		for(int i=0;i<points;i++) {
			for(int j=0;j<dim;j++) {
				System.out.print(data[i][j]+"   ");
			}
			System.out.print("\n");
		}
		if(dim==2) {
			final XYPlane window=new XYPlane(data,clusters);
			window.pack();
			RefineryUtilities.centerFrameOnScreen(window);
			window.setVisible(true);
		}
	}

}
