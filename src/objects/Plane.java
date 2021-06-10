package objects;

import graphics.Color;
import math.Vector3f;

public class Plane {
	// normal which is perpendicular to the plane
	public Vector3f normal;
	// a point on the plane
	public Vector3f point; 
	public Color color;
	
	public Plane(Vector3f normal, Vector3f point, Color color) {
		this.normal = normal;
		this.point = point;
		this.color = color;
	}
}
