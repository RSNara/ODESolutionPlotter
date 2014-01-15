import java.applet.Applet;
import java.awt.*;
import java.util.ArrayList;

public class DerivativeGraph extends Applet{
	
	private ArrayList listOfPoints;
	private Point origin;
	private Point initialPoint;
	final private double scalingFactor = 40;						// how much do you want to zoom into the function? You can only zoom out a limited amount with functions with infinite discontinuities
	final private double stepSize = 1;
	
	private double f(double x, double y) {									// Enter in the function that you wish to have graphed
		return Math.pow(3*Math.sin(x-Math.PI/4)*Math.cos(2*x+Math.PI/2),2);
	}
	
	public void paint(Graphics g) {
		// update
		this.origin =  new Point(this.getWidth()/2,this.getHeight()/2); // enter in the x and y coordinates of the origin (currently, it's set to the center of the applet)
		this.initialPoint = new Point(0, 1);
		this.listOfPoints = getPoints();
		
		// draw axis
		g.setColor(Color.gray);
		g.drawLine((int)origin.getX(),0, (int)origin.getX(),this.getHeight() - 1);		// draw vertical axis
		g.drawLine(0,(int)origin.getY(), this.getWidth() - 1, (int)origin.getY());		// draw horizontal axis
		
		// label x-axis
		for (int i = (int)this.origin.getX(); i < this.getWidth();i++) {
			if (((i - (int)this.origin.getX()) % scalingFactor) == 0)
				g.drawLine(i, (int)this.origin.getY() - 5, i, (int)this.origin.getY() + 5);
		}
		
		for (int i = (int)this.origin.getX(); i > 0;i--) {
			if (((i - (int)this.origin.getX()) % scalingFactor) == 0)
				g.drawLine(i, (int)this.origin.getY() - 5, i, (int)this.origin.getY() + 5);
		}
		
		// label y-axis
		for (int i = (int)this.origin.getY(); i < this.getHeight();i++) {
			if (((i - (int)this.origin.getY()) % scalingFactor) == 0)
				g.drawLine((int)this.origin.getX() - 5, i, (int)this.origin.getX() + 5,i);
		}
		
		for (int i = (int)this.origin.getY(); i > 0;i--) {
			if (((i - (int)this.origin.getY()) % scalingFactor) == 0)
				g.drawLine((int)this.origin.getX() - 5, i, (int)this.origin.getX() + 5,i);
		}
		
		
		// drawing function
		for (int i = 0; i < listOfPoints.size() - 1; i++) {	// If I have 3 points, i have 2 lines connecting them, // so I have to draw n-1 lines to graph a function with n points
					
			Point initial = (Point)listOfPoints.get(i);
			Point finalP = (Point)listOfPoints.get(i+1);
			
			if (    (((Double)initial.getY()).isInfinite()) || 					// if you have discontinuities, don't graph them (doesn't work for floor/ceiling discontinuities though)
					(((Double)finalP.getY()).isInfinite())  ||
					(((Double)initial.getY()).isNaN())      ||
					(((Double)finalP.getY()).isNaN())       
				)
				
				continue;
			
			g.setColor(Color.red);
			g.drawLine((int)(this.origin.getX() + scalingFactor*initial.getX()), (int)(this.origin.getY()  - scalingFactor*initial.getY()), (int)(this.origin.getX()  + scalingFactor*finalP.getX()), (int)(this.origin.getY()  - scalingFactor*finalP.getY()) );
					
		}
				
		g.setColor(Color.gray);							
	}
	
	// returns a list of all the points that are going to be graphed
	public ArrayList getPoints() {
		
		ArrayList listOfPoints = new ArrayList();
		double slope = f(this.initialPoint.getX(),this.initialPoint.getY());
		Point newPoint = new Point(initialPoint.getX(), initialPoint.getY());
		Point oldPoint;
		listOfPoints.add(newPoint);
		
		for (int i = (int)(this.initialPoint.getX() * scalingFactor); i < (int)(this.getWidth() - this.initialPoint.getX() * scalingFactor);i++) {
			oldPoint = newPoint;
			newPoint = new Point((i + stepSize)/scalingFactor, slope*(this.stepSize/scalingFactor) + oldPoint.getY());
			slope = f(newPoint.getX(),newPoint.getY());
			listOfPoints.add(newPoint);
		}
		
		ArrayList listOfPoints2 = new ArrayList();
		slope = f(this.initialPoint.getX(),this.initialPoint.getY());
		newPoint = new Point(initialPoint.getX(), initialPoint.getY());
		listOfPoints2.add(newPoint);
		
		for (int i = (int)(this.initialPoint.getX() * scalingFactor); i > -this.origin.getX();i--) {
			oldPoint = newPoint;
			newPoint = new Point((i - stepSize)/scalingFactor, -slope*(this.stepSize/scalingFactor) + oldPoint.getY());
			slope = f(newPoint.getX(),newPoint.getY());
			listOfPoints2.add(newPoint);
		}
		
		ArrayList listOfPoints3 = new ArrayList();
		
		for (int i = listOfPoints2.size()- 1; i >= 0; i--) {
			listOfPoints3.add(listOfPoints2.get(i));
		}
		
		for (int i = 0; i < listOfPoints.size();i++) {
			listOfPoints3.add(listOfPoints.get(i));
		}
		
		Graphics g = getGraphics();
		g.drawString(((Integer)(listOfPoints3.size())).toString(), 10,10);
		g.dispose();
		
		return listOfPoints;
	}
	
	// returns a list of all the discontinuities, this is has no use for the time being
	public ArrayList getDiscontinuities() {
		ArrayList exceptions = new ArrayList();
		for (int i = 0; i < this.listOfPoints.size();i++) {
			if (((Double)((Point)this.listOfPoints.get(i)).getY()).isInfinite()) {
				exceptions.add(this.listOfPoints.get(i));
			}
		}
		
		return exceptions;
	}
}
