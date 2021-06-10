package math;

public class Vector3f {
	public float x;
	public float y;
	public float z;
	
	public Vector3f() {
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float getMagnitude() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	public void normalize() {
		float magnitude = getMagnitude();
		x /= magnitude;
		y /= magnitude;
		z /= magnitude;
	}
	
	public void scale(float lamda) {
		x *= lamda;
		y *= lamda;
		z *= lamda;
	}
	
	public float dot(Vector3f a, Vector3f b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}
	
	public Vector3f cross(Vector3f a, Vector3f b) {
		Vector3f result = new Vector3f();
		result.x = a.y * b.z - a.z * b.y;
		result.y = a.z * b.x - a.x * b.z;
		result.z = a.x * b.y - a.y * b.x;
		return result;
	}
}
