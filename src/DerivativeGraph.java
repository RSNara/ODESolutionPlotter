import java.applet.Applet;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class DerivativeGraph extends Applet{
	
	private ArrayList<Point> listOfPoints;
	private Point origin;
	
	/********************************************************************************************************
	 *                               THINGS THAT THE USER NEEDS TO ENTER IN:                                *
	 *======================================================================================================*
	 *                                                                                                      *
	 *     1) A point that lies on the DE solution. (Relevant for DE solution graphing only)                *
	 *     2) Domain of both functions                                                                      *
	 *     3) scalingFactor (Zoom level; the higher it is the more zoom you have)                           *
	 *     4) graphDerivative                                                                               *
	 *                                                                                                      *
	 *                                                                                                      *
	 ********************************************************************************************************/
	
	         // Only relevant for Differential Equations solutions
	/**1*/   final private Point initialPoint = new Point(0,0);						
	
	         // Some DE solutions require a readjustments of the domain (DE: Differential Equation; F: Function)
	/**2*/   final private double[] domainDE = new double[] {Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY};
	/**2.5*/ final private double[] domainF = new double[] {Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY};
	
	         // Make lower to zoom out, and larger to zoom in
	/**3*/   final private double scalingFactor = 100;						
	
	         // true: graph derivative(double x, double y); false: graph function(double x)
	/**4*/   final private boolean graphDerivative = true;					
	
	
	// Constants that should never be changed:
	final private static double epsilon = 0.0000000001;				
	final private double stepSize = 1;								
	

	/********************************************************************************************************
	 *                                  FUNCTIONS THAT WILL BE PLOTTED                                      *
	 *======================================================================================================*
	 *                                                                                                      *
	 *     1) A differential equation in the form of y' = f(x,y)                                            *
	 *     2) A function that will be plotted as is                                                         *
	 *                                                                                                      *
	 *                                                                                                      *                                                                                                      *
	 ********************************************************************************************************/
	
	/**1*/  private double derivative(double x, double y) {									
		    	return function(function(x));
			}
	
	/**2*/ 	private double function(double x) {
				return Math.sin(x);
			}
	
	
	public void paint(Graphics g) {
		// update the origin to adjust for resizing of the applet and get a new list of points
		this.origin =  new Point(this.getWidth()/2,this.getHeight()/2); 	// enter in the x and y coordinates of the origin (currently, it's set to the center of the applet)
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
		
		// indicate which function is being plotted
		if (graphDerivative)
			g.drawString("Differential Equation Soultion: ", 10,20);
		else
			g.drawString("Function Plot: ", 10,20);
		
		// label the number of points
		g.drawString("# of Points: " + this.listOfPoints.size(), 10,40);
		
		// drawing derivative
		for (int i = 0; i < listOfPoints.size() - 1; i++) {	// If I have 3 points, i have 2 lines connecting them, // so I have to draw n-1 lines to graph a derivative with n points
					
			Point initial = (Point)listOfPoints.get(i);
			Point finalP = (Point)listOfPoints.get(i+1);
			
			if (    (((Double)initial.getY()).isInfinite()) || 					// if you have discontinuities, don't graph them (doesn't work for floor/ceiling discontinuities though)
					(((Double)finalP.getY()).isInfinite())  ||
					(((Double)initial.getY()).isNaN())      ||
					(((Double)finalP.getY()).isNaN())       
				)
				
				continue;
			
			if (Math.abs(initial.getY() - finalP.getY()) >= scalingFactor) {
				continue;
			}
			
			// change the color of the graph depending on what you're graphing (blue = function, red = solution to DE)
			if (!graphDerivative)
				g.setColor(Color.blue);
			else
				g.setColor(Color.red);
			
			g.drawLine((int)(this.origin.getX() + scalingFactor*initial.getX()), (int)(this.origin.getY()  - scalingFactor*initial.getY()), (int)(this.origin.getX()  + scalingFactor*finalP.getX()), (int)(this.origin.getY()  - scalingFactor*finalP.getY()) );
					
		}
		
		this.writePoints();
		g.setColor(Color.gray);							
	}
	
	// returns a list of all the points that are going to be graphed
	public ArrayList<Point> getPoints() {
		
		// Collect all the points in correct order in a third ArrayList which will be handed back to the Paint routine
		ArrayList<Point> listOfPoints = new ArrayList();
		
		// If you want to graph the derivative, run the following block of code 
		
		if (graphDerivative) {
			
			// Do Euler's algorithm for points to the right of the specified point
			ArrayList<Point> listOfPointsRight = new ArrayList();
			double slope = derivative(this.initialPoint.getX(),this.initialPoint.getY());
			Point newPoint = new Point(initialPoint.getX(), initialPoint.getY());
			Point oldPoint;
			listOfPointsRight.add(newPoint);
			
			for (int i = (int)(this.initialPoint.getX() * scalingFactor); i < (int)(this.getWidth() - (this.initialPoint.getX() * scalingFactor + this.origin.getX()));i++) {
				oldPoint = newPoint;
				newPoint = new Point((i + stepSize)/scalingFactor, slope*(this.stepSize/scalingFactor) + oldPoint.getY());
				slope = derivative(newPoint.getX(),newPoint.getY());
				
				listOfPointsRight.add(newPoint);
				
				if (Math.abs(newPoint.getX() - this.domainDE[0]) < epsilon || Math.abs(newPoint.getX() - this.domainDE[1]) < epsilon || Double.isInfinite(slope) || Double.isNaN(slope))
					break;
				
			}
			
			// Do Euler's algorithm for points to the left of the specified point
			ArrayList<Point> listOfPointsLeft = new ArrayList();
			slope = derivative(this.initialPoint.getX(),this.initialPoint.getY());
			newPoint = new Point(initialPoint.getX(), initialPoint.getY());
			listOfPointsLeft.add(newPoint);
			
			for (int i = (int)(this.initialPoint.getX() * scalingFactor); i > -this.origin.getX();i--) {
				oldPoint = newPoint;
				newPoint = new Point((i - stepSize)/scalingFactor, -slope*(this.stepSize/scalingFactor) + oldPoint.getY());
				slope = derivative(newPoint.getX(),newPoint.getY());
				
				listOfPointsLeft.add(newPoint);
				
				if (Math.abs(newPoint.getX() - this.domainDE[0]) < epsilon || Math.abs(newPoint.getX() - this.domainDE[1]) < epsilon || Double.isInfinite(slope) || Double.isNaN(slope))
					break;
				
			}
			
			// input all the elements into listOfPoints in the correct order
			for (int i = listOfPointsLeft.size()- 1; i >= 0; i--) {
				listOfPoints.add(listOfPointsLeft.get(i));
			}
			
			for (int i = 0; i < listOfPointsRight.size();i++) {
				listOfPoints.add(listOfPointsRight.get(i));
			}
		
		}
		
		// If you want to graph the derivative as it is, use this block of code
		
		else {

			for (int i = -(int)this.origin.getX(); i < (int)(this.getWidth() - this.origin.getX());i++) {
				
				// add domain functionality. 
				
				// If you haven't gotten to the correct i value yet, just skip the current iteration of the loop.
				if ((domainF[0] * scalingFactor) > i)
					continue;
				
				// If you've gone past, break out of the loop
				if ((domainF[1] * scalingFactor ) < i)
					break;
				
				Point newPoint = new Point(i/scalingFactor, function(i/scalingFactor));
				
				// fixing asymptotic functions such as -Math.log(Math.cos(x)) but it makes other functions act weird
				
				if (Double.isInfinite(function(i/scalingFactor)));
				else if (Double.isNaN(function(i/scalingFactor)) && !Double.isNaN(function((i-1)/scalingFactor)))
					newPoint.setY(function((i-1)/scalingFactor)/Math.abs(function((i-1)/scalingFactor)) * scalingFactor);
				else if (Double.isNaN(function(i/scalingFactor)) && !Double.isNaN(function((i+1)/scalingFactor)))
					newPoint.setY(function((i+1)/scalingFactor)/Math.abs(function((i+1)/scalingFactor)) *  scalingFactor);
				

				listOfPoints.add(newPoint);
			}
		}
		
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

	// write all the points to a file
	public void writePoints() {
		
		for (int i = 0 ; i< this.listOfPoints.size();i++){
			System.out.println("(" + this.listOfPoints.get(i).getX() + ", " + this.listOfPoints.get(i).getY() + ", " + derivative(this.listOfPoints.get(i).getX(),this.listOfPoints.get(i).getY()) +  ")");
		}
	}	
}