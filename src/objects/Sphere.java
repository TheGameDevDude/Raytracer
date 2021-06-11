package objects;

import graphics.Color;
import math.Ray;
import math.Vector3f;

public class Sphere extends Entity {
	// center of sphere
	private Vector3f position;
	public float radius;

	public Sphere(Vector3f position, float radius, Color color) {
		super(color);
		this.position = position;
		this.radius = radius;
	}

	// calculating the normal at a given point on the sphere
	public Vector3f getNormal(Vector3f point) {
		Vector3f normal = new Vector3f(point.x - position.x, point.y - position.y, point.z - position.z);
		normal.normalize();
		return normal;
	}
	
	// calculating the distance between the origin of ray and the point where ray intersects the sphere
	public float intersect(Ray ray) {
		Vector3f p = new Vector3f(ray.origin.x - position.x, ray.origin.y - position.y, ray.origin.z - position.z);
		float b = 2 * new Vector3f().dot(p, ray.direction);
		float c = new Vector3f().dot(p, p) - (radius * radius);
		
		float discriminant = b * b - 4 * c;
		
		// if the discriminant is greater than zero then it intersects the sphere
		if(discriminant > 0) {
			float root1 = (float) ((-b - Math.sqrt(discriminant)) / 2);
			
			if(root1 > 0) {
				return root1;
			}else {
				return (float) ((-b + Math.sqrt(discriminant)) / 2);
			}
			
		}else {
			return -1;
		}
	}

}
