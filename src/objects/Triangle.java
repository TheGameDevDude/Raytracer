package objects;

import graphics.Color;
import math.Ray;
import math.Vector3f;

public class Triangle extends Entity {
	// all three points of a triangle
	private Vector3f A;
	private Vector3f B;
	private Vector3f C;

	public Triangle(Vector3f A, Vector3f B, Vector3f C, Color color) {
		super(color);
		this.A = A;
		this.B = B;
		this.C = C;
	}

	public Vector3f getNormal(Vector3f point) {
		return getNormal();
	}

	// calculating the distance between the origin of ray to the intersection of ray with the plane of the triangle
	public float intersect(Ray ray) {
		Vector3f normal = getNormal();
		float dot = new Vector3f().dot(ray.direction, normal);

		// if the ray is perpendicular to the plane then the ray does not intersect the plane
		if (dot == 0) {
			return -1;
		}

		float distance = (new Vector3f().dot(A, normal) - new Vector3f().dot(ray.origin, normal)) / new Vector3f().dot(ray.direction, normal);
		Vector3f Q = ray.direction.scale(distance);

		// to check whether the point of intersection is on the triangle 
		float a = new Vector3f().dot(new Vector3f().cross(new Vector3f(A.x - B.x, A.y - B.y, A.z - B.z), new Vector3f(Q.x - B.x, Q.y - B.y, Q.z - B.z)), normal);
		float b = new Vector3f().dot(new Vector3f().cross(new Vector3f(C.x - A.x, C.y - A.y, C.z - A.z), new Vector3f(Q.x - A.x, Q.y - A.y, Q.z - A.z)), normal);
		float c = new Vector3f().dot(new Vector3f().cross(new Vector3f(B.x - C.x, B.y - C.y, B.z - C.z), new Vector3f(Q.x - C.x, Q.y - C.y, Q.z - C.z)), normal);

		if (a > 0.0f && b > 0.0f && c > 0.0f) {
			return distance;
		} else {
			return -1;
		}
	}

	// get the normal of the triangle
	private Vector3f getNormal() {
		return new Vector3f().cross(new Vector3f(A.x - B.x, A.y - B.y, A.z - B.z), new Vector3f(C.x - B.x, C.y - B.y, C.z - B.z)).normalize();
	}
}
