package objects;

import graphics.Color;
import math.Ray;
import math.Vector3f;

public class Entity {
	// color of the object
	private Color color;

	public Entity(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	public Vector3f getNormal(Vector3f point) {
		return new Vector3f();
	}

	public float intersect(Ray ray) {
		return -1;
	}
}
