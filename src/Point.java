
public class Point {
	
	private double x;
	private double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Point() {
		this.x = 0;
		this.y = 0;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void setXY(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public double getDistanceFrom(double x, double y) {
		return Math.sqrt(Math.pow(this.x - x,2) + Math.pow(this.y - y, 2));
	}
	
	public double getDistanceFrom(Point p){
		
		return Math.sqrt(Math.pow(this.x - p.x,2) + Math.pow(this.y - p.y, 2));
		
	}
	

}