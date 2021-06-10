package math;

public class Ray {
	public Vector3f origin;
	public Vector3f direction;
	
	public Ray(Vector3f origin, Vector3f direction) {
		this.origin = origin;
		this.direction = direction;
	}
}
