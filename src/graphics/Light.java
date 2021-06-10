package graphics;

import math.Vector3f;

public class Light {
	public Vector3f position;
	public Color color;
	
	public Light(Vector3f position, Color color) {
		this.position = position;
		this.color = color;
	}
}
