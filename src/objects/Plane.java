package objects;

import graphics.Color;
import math.Ray;
import math.Vector3f;

public class Plane extends Entity {
	// normal which is perpendicular to the plane
	private Vector3f normal;
	// a point on the plane
	private Vector3f point;

	public Plane(Vector3f normal, Vector3f point, Color color) {
		super(color);
		this.normal = normal;
		this.point = point;
	}

	public Vector3f getNormal(Vector3f point) {
		return normal;
	}

	// calculating the distance between the origin of ray to the intersection of ray with the plane
	// detailed explanation given in images/Plane_Intersection.png
	public float intersect(Ray ray) {
		float dot = new Vector3f().dot(ray.direction, normal);

		// if the ray is perpendicular to the plane then the ray does not intersect the
		// plane
		if (dot == 0) {
			return -1;
		}

		return (new Vector3f().dot(point, normal) - new Vector3f().dot(ray.origin, normal)) / new Vector3f().dot(ray.direction, normal);
	}
}
