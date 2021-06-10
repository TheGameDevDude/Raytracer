package objects;

import graphics.Color;
import math.Vector3f;

public class Sphere {
	public Vector3f position;
	public float radius;
	public Color color;
	
	public Sphere(Vector3f position, float radius, Color color) {
		this.position = position;
		this.radius = radius;
		this.color = color;
	}
}
