package objects;

import graphics.Color;
import math.Ray;
import math.Vector3f;

public class Plane extends Entity {
	// normal which is perpendicular to the plane
	private Vector3f normal;
	// a point on the plane
	private Vector3f point;

	// for checker patters
	private boolean checkers;

	public Plane(Vector3f normal, Vector3f point, Color color) {
		super(color);
		this.normal = normal;
		this.point = point;
		checkers = false;
	}

	public Plane(Vector3f normal, Vector3f point, boolean checkers) {
		super(new Color(0, 0, 0, 0.0f));
		this.normal = normal;
		this.point = point;
		this.checkers = checkers;
	}

	public Vector3f getNormal(Vector3f point) {
		return normal;
	}

	// calculating the distance between the origin of ray to the intersection of ray with the plane
	public float intersect(Ray ray) {
		float dot = new Vector3f().dot(ray.direction, normal);

		// if the ray is perpendicular to the plane then the ray does not intersect the
		// plane
		if (dot == 0) {
			return -1;
		}

		return (new Vector3f().dot(point, normal) - new Vector3f().dot(ray.origin, normal)) / new Vector3f().dot(ray.direction, normal);
	}

	public boolean getCheckers() {
		return checkers;
	}
}
