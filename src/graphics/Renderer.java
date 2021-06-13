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
	private int aaDepth;

	public Renderer(int width, int height, int aaDepth) {
		this.width = width;
		this.height = height;
		this.aaDepth = aaDepth;
	}

	// ray-tracing happens here
	public void render(int[] screenPixels, Camera camera, List<Entity> entities, List<Light> lights) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				// stores all colors for one pixel
				int[] red = new int[aaDepth * aaDepth];
				int[] green = new int[aaDepth * aaDepth];
				int[] blue = new int[aaDepth * aaDepth];

				// another loop for antialiasing
				for (int aay = 0; aay < aaDepth; aay++) {
					for (int aax = 0; aax < aaDepth; aax++) {
						// generate rays
						Ray ray = generateRays(x, y, camera, aaDepth, aax, aay);

						// find the first object's color that gets intersected
						List<Object> indexAndDistance = findFirstIndexOfObjectIntersectingTheRay(entities, ray);
						int index = (int) indexAndDistance.get(0);
						float minDistance = (float) indexAndDistance.get(1);

						// if index is negative then there is no object being intersected and the color is black
						Color color = new Color(0, 0, 0);
						if (index != -1) {
							color = getColorAtIntersection(ray, minDistance, index, camera, entities, lights);
						}

						red[aax + aay * aaDepth] = color.red;
						green[aax + aay * aaDepth] = color.green;
						blue[aax + aay * aaDepth] = color.blue;
					}
				}

				// getting the average of all the colors for one pixel (antialiasing)
				int finalRed = 0, finalGreen = 0, finalBlue = 0;

				for (int r = 0; r < red.length; r++) {
					finalRed += red[r];
				}

				for (int g = 0; g < green.length; g++) {
					finalGreen += green[g];
				}

				for (int b = 0; b < blue.length; b++) {
					finalBlue += blue[b];
				}

				finalRed /= red.length;
				finalGreen /= blue.length;
				finalBlue /= green.length;

				screenPixels[x + y * width] = finalRed << 16 | finalGreen << 8 | finalBlue;

			}
		}
	}

	private Ray generateRays(int x, int y, Camera camera, int aaDepth, int aax, int aay) {
		// if aaDepth is 1 then there is no antialiasing
		if (aaDepth == 1) {
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
		} else {
			float h = (float) Math.tan(camera.FOV / 2);
			float aspectRatio = (float) width / (float) height;
			float w = aspectRatio * h;

			// converting x and y to -1 to 1
			float xampt = (2.0f * ((float) x + ((float) aax / (float) (aaDepth - 1))) / (float) width) - 1.0f;
			float yampt = (2.0f * ((float) height - ((float) y + ((float) aay / (float) (aaDepth - 1)))) / (float) height) - 1.0f;

			// calculating the direction of rays
			float xDir = camera.direction.x + (h * yampt * camera.up.x) + (w * xampt * camera.right.x);
			float yDir = camera.direction.y + (h * yampt * camera.up.y) + (w * xampt * camera.right.y);
			float zDir = camera.direction.z + (h * yampt * camera.up.z) + (w * xampt * camera.right.z);
			Vector3f direction = new Vector3f(xDir, yDir, zDir).normalize();

			// final ray
			return new Ray(camera.position, direction);
		}

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
			if (distance > 0.0001f && distance < minDistance) {
				minDistance = distance;
				index = i;
			}
		}

		return Arrays.asList(index, minDistance);
	}

	private Color getColorAtIntersection(Ray ray, float minDistance, int index, Camera camera, List<Entity> entities, List<Light> lights) {
		// finding the point of intersection and the normal at the point of intersection
		Vector3f direction = ray.direction.scale(minDistance);
		Vector3f intersectingPoint = new Vector3f(ray.origin.x + direction.x, ray.origin.y + direction.y, ray.origin.z + direction.z);
		Vector3f normalAtIntersectingPoint = entities.get(index).getNormal(intersectingPoint);

		// get material information
		Color objectColor = getObjectColor(entities.get(index), intersectingPoint);
		float shininess = objectColor.shininess;

		// ambient light color of the object
		int R = objectColor.red / 5, G = objectColor.green / 5, B = objectColor.blue / 5;

		// reflections
		if (shininess > 0.0f) {
			// calculating reflection ray
			Vector3f reflectionDirection = calculateReflectedVector(ray.direction, normalAtIntersectingPoint);
			Ray reflectionRay = new Ray(intersectingPoint, reflectionDirection);

			// finding the firstObject color hit by the reflected ray
			List<Object> indexAndDistance = findFirstIndexOfObjectIntersectingTheRay(entities, reflectionRay);
			int reflectionIndex = (int) indexAndDistance.get(0);
			float reflectionMinDistance = (float) indexAndDistance.get(1);

			// if there is such object then recursively get the color at the intersection point of the reflected ray and mix the color with the reflection color
			if (reflectionIndex != -1) {
				Color reflectionColor = getColorAtIntersection(reflectionRay, reflectionMinDistance, reflectionIndex, camera, entities, lights);
				R += (int) ((float) reflectionColor.red * shininess);
				G += (int) ((float) reflectionColor.green * shininess);
				B += (int) ((float) reflectionColor.blue * shininess);
			}
		}

		for (Light light : lights) {
			Vector3f toLightVector = new Vector3f(light.position.x - intersectingPoint.x, light.position.y - intersectingPoint.y, light.position.z - intersectingPoint.z);
			Vector3f toLightDirection = toLightVector.normalize();

			// the light value at the point of intersection
			float lightValue = new Vector3f().dot(normalAtIntersectingPoint, toLightDirection);

			if (lightValue > 0.0f) {
				// to find whether there is a shadow at the point of intersection
				boolean shadowed = isShadow(toLightVector, intersectingPoint, entities);

				// for ambient lighting
				if (lightValue < 0.001f) {
					lightValue = 0.001f;
				}

				// if there is no shadow then we can mix light color and object color
				if (shadowed == false) {
					R += (int) ((float) objectColor.red * lightValue * (float) light.color.red / 255);
					G += (int) ((float) objectColor.green * lightValue * (float) light.color.green / 255);
					B += (int) ((float) objectColor.blue * lightValue * (float) light.color.blue / 255);

					if (shininess > 0.0f) {
						// calculating specularity
						Vector3f reflectedVector = calculateReflectedVector(new Vector3f(-toLightVector.x, -toLightVector.y, -toLightVector.z), normalAtIntersectingPoint);
						Vector3f toCameraDirection = new Vector3f(camera.position.x - intersectingPoint.x, camera.position.y - intersectingPoint.y, camera.position.z - intersectingPoint.z).normalize();
						float specularity = (float) Math.pow(Math.max(new Vector3f().dot(toCameraDirection, reflectedVector), 0.0f), 10);

						// mixing specularity and shininess with object color and light color
						R += (int) ((float) objectColor.red * specularity * (float) light.color.red * shininess / 255);
						G += (int) ((float) objectColor.green * specularity * (float) light.color.green * shininess / 255);
						B += (int) ((float) objectColor.blue * specularity * (float) light.color.blue * shininess / 255);
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

	// get object color of the winning object
	private Color getObjectColor(Entity entity, Vector3f intersectingPoint) {
		Color objectColor = entity.getColor();

		// to give the plane checkers pattern texture
		if (entity.getCheckers() == true) {
			int square = (int) Math.floor(intersectingPoint.x) + (int) Math.floor(intersectingPoint.z);
			if (square % 2 == 0) {
				objectColor.red = 50;
				objectColor.green = 50;
				objectColor.blue = 50;
			} else {
				objectColor.red = 200;
				objectColor.green = 200;
				objectColor.blue = 200;
			}
		}

		return objectColor;
	}

	// to find whether there is a shadow at the point of intersection
	private boolean isShadow(Vector3f toLightVector, Vector3f intersectingPoint, List<Entity> entities) {
		boolean shadowed = false;
		float distanceOfToLightVector = toLightVector.getMagnitude();
		Ray rayFromIntersectingPointToLight = new Ray(intersectingPoint, toLightVector.normalize());

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

		return shadowed;
	}

	// calculating the reflected vector
	private Vector3f calculateReflectedVector(Vector3f incident, Vector3f normal) {
		Vector3f incidentVector = new Vector3f(incident.x, incident.y, incident.z).normalize();
		float dot = new Vector3f().dot(incidentVector, normal) * 2.0f;
		Vector3f normalDirVector = normal.scale(dot);
		return new Vector3f(incidentVector.x - normalDirVector.x, incidentVector.y - normalDirVector.y, incidentVector.z - normalDirVector.z);
	}
}
