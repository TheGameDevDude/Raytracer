package graphics;

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
	public void render(int[] screenPixels, Camera camera, Entity entity) {

		float h = (float) Math.tan(camera.FOV / 2);
		float aspectRatio = (float) width / (float) height;
		float w = aspectRatio * h;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				// converting x and y to -1 to 1
				float xampt = (2.0f * (float) x / (float) width) - 1.0f;
				float yampt = (2.0f * ((float) height - (float) y) / (float) height) - 1.0f;

				// calculating the direction of rays
				float xDir = camera.direction.x + (h * yampt * camera.up.x) + (w * xampt * camera.right.x);
				float yDir = camera.direction.y + (h * yampt * camera.up.y) + (w * xampt * camera.right.y);
				float zDir = camera.direction.z + (h * yampt * camera.up.z) + (w * xampt * camera.right.z);

				Vector3f direction = new Vector3f(xDir, yDir, zDir);
				direction.normalize();

				Ray ray = new Ray(camera.position, direction);

				// if the ray intersects then distance will be positive
				float dist = entity.intersect(ray);

				if (dist > 0) {
					screenPixels[x + y * width] = 0xff00ff;
				}

			}
		}
	}
}
