package graphics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import math.Ray;
import math.Vector3f;
import objects.Camera;
import objects.Entity;

public class Renderer {
	private int width;
	private int height;

	public Renderer(int width, int height) {
		this.width = width;
		this.height = height;
	}

	// producing bunch of rays from the camera to the target
	public void render(int[] screenPixels, Camera camera, List<Entity> entities, List<Light> lights) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				// generate rays
				Ray ray = generateRays(x, y, camera);

				// find the first object's color that gets intersected
				List<Object> indexAndDistance = findFirstIndexOfObjectIntersectingTheRay(entities, ray);
				int index = (int) indexAndDistance.get(0);
				float minDistance = (float) indexAndDistance.get(1);

				// if index is negative then there is no object being intersected and the color is black
				Color color = new Color(0, 0, 0);
				if (index != -1) {
					color = getColorAtIntersection(ray, minDistance, index, camera, entities, lights);
				}

				// plotting the pixel
				screenPixels[x + y * width] = color.red << 16 | color.green << 8 | color.blue;
			}
		}
	}

	private Ray generateRays(int x, int y, Camera camera) {
		float h = (float) Math.tan(camera.FOV / 2);
		float aspectRatio = (float) width / (float) height;
		float w = aspectRatio * h;

		// converting x and y to -1 to 1
		float xampt = (2.0f * (float) x / (float) width) - 1.0f;
		float yampt = (2.0f * ((float) height - (float) y) / (float) height) - 1.0f;

		// calculating the direction of rays
		float xDir = camera.direction.x + (h * yampt * camera.up.x) + (w * xampt * camera.right.x);
		float yDir = camera.direction.y + (h * yampt * camera.up.y) + (w * xampt * camera.right.y);
		float zDir = camera.direction.z + (h * yampt * camera.up.z) + (w * xampt * camera.right.z);
		Vector3f direction = new Vector3f(xDir, yDir, zDir).normalize();

		// final ray
		return new Ray(camera.position, direction);
	}

	private List<Object> findFirstIndexOfObjectIntersectingTheRay(List<Entity> entities, Ray ray) {
		int index = -1;
		if (entities.size() == 0)
			return Arrays.asList(index, Float.MAX_VALUE);

		float minDistance = Float.MAX_VALUE;

		// finding the minimum distance of intersection
		for (int i = 0; i < entities.size(); i++) {
			float distance = entities.get(i).intersect(ray);
			if (distance < 0)
				continue;
			if (distance < minDistance) {
				minDistance = distance;
				index = i;
			}
		}

		return Arrays.asList(index, minDistance);
	}

	private Color getColorAtIntersection(Ray ray, float minDistance, int index, Camera camera, List<Entity> entities, List<Light> lights) {
		// finding the point of intersection
		Vector3f direction = ray.direction.scale(minDistance);
		Vector3f intersectingPoint = new Vector3f(ray.origin.x + direction.x, ray.origin.y + direction.y, ray.origin.z + direction.z);
		// finding the normal at the point of intersection
		Vector3f normalAtIntersectingPoint = entities.get(index).getNormal(intersectingPoint);
		Color objectColor = entities.get(index).getColor();

		float shininess = objectColor.shininess;

		// ambient light color of the object
		int R = objectColor.red / 5, G = objectColor.green / 5, B = objectColor.blue / 5;

		for (Light light : lights) {
			Vector3f toLightVector = new Vector3f(light.position.x - intersectingPoint.x, light.position.y - intersectingPoint.y, light.position.z - intersectingPoint.z);
			Vector3f toLightDirection = toLightVector.normalize();

			// the light value at the point of intersection
			float lightValue = new Vector3f().dot(normalAtIntersectingPoint, toLightDirection);

			if (lightValue > 0.0f) {
				boolean shadowed = false;
				float distanceOfToLightVector = toLightVector.getMagnitude();
				Ray rayFromIntersectingPointToLight = new Ray(intersectingPoint, toLightDirection);

				// find the secondary intersections from the ray at the intersection point
				List<Float> intersections = new ArrayList<Float>();
				for (Entity entity : entities) {
					float distance = entity.intersect(rayFromIntersectingPointToLight);
					if (distance > 0.0001f) {
						intersections.add(distance);
					}
				}

				// if any of the distance of intersections is lesser than the distance of toLightVector then there is a shadow
				for (int i = 0; i < intersections.size(); i++) {
					if (intersections.get(i) <= distanceOfToLightVector) {
						shadowed = true;
						break;
					}
				}

				// for ambient lighting
				if (lightValue < 0.1f) {
					lightValue = 0.1f;
				}

				// if there is no shadow then we can mix light color and object color
				if (shadowed == false) {
					R += (int) ((float) objectColor.red * lightValue * (float) light.color.red * lightValue / 255);
					G += (int) ((float) objectColor.green * lightValue * (float) light.color.green * lightValue / 255);
					B += (int) ((float) objectColor.blue * lightValue * (float) light.color.blue * lightValue / 255);

					if (shininess > 0.0f) {
						// calculating the reflected ray
						Vector3f incidentRay = new Vector3f(-toLightVector.x, -toLightVector.y, -toLightVector.z).normalize();
						float dot = new Vector3f().dot(incidentRay, normalAtIntersectingPoint) * 2.0f;
						Vector3f normalDirVector = normalAtIntersectingPoint.scale(dot);
						Vector3f reflectedRay = new Vector3f(incidentRay.x - normalDirVector.x, incidentRay.y - normalDirVector.y, incidentRay.z - normalDirVector.z);
						Vector3f toCameraDirection = new Vector3f(camera.position.x - intersectingPoint.x, camera.position.y - intersectingPoint.y, camera.position.z - intersectingPoint.z).normalize();
						// calculating specularity
						float specularity = (float) Math.pow(Math.max(new Vector3f().dot(toCameraDirection, reflectedRay), 0.0f), shininess);
						
						// mixing specularity with object color and light color
						R += (int) ((float) objectColor.red * specularity * (float) light.color.red * specularity / 255);
						G += (int) ((float) objectColor.green * specularity * (float) light.color.green * specularity / 255);
						B += (int) ((float) objectColor.blue * specularity * (float) light.color.blue * specularity / 255);
					}
				}
			}
		}

		// clamping R, G, B values
		if (R >= 255)
			R = 255;
		if (G >= 255)
			G = 255;
		if (B >= 255)
			B = 255;

		return new Color(R, G, B);
	}
}
