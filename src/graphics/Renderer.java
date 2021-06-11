package graphics;

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

				// find the first object's color that gets intersected, if minDistValue is negative then there is no object and the color is black
				List<Object> indexAndDistance = findFirstIndexOfObjectIntersectingTheRay(entities, ray);
				int index = (int) indexAndDistance.get(0);
				float distance = (float) indexAndDistance.get(1);

				Color color = new Color(0, 0, 0);
				if (index != -1) {
					color = getColorAtIntersection(ray, distance, index, entities, lights);
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
		Vector3f direction = new Vector3f(xDir, yDir, zDir);
		direction.normalize();

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

	private Color getColorAtIntersection(Ray ray, float distance, int index, List<Entity> entities, List<Light> lights) {
		return entities.get(index).getColor();
	}
}
