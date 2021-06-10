package window;

import graphics.Color;
import graphics.Renderer;
import math.Vector3f;
import objects.Camera;
import objects.Sphere;
import objects.Entity;
import objects.Plane;

public class Scene {
	private Renderer renderer;
	
	// all the objects in the scene
	public Camera camera = new Camera(new Vector3f(4.0f, 4.0f, -4.0f), new Vector3f(), 60);
	public Entity sphere = new Sphere(new Vector3f(), 1, new Color(200, 50, 20));
	public Entity plane = new Plane(new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(0.0f, -1.0f, 0.0f), new Color(50, 20, 200));
	
	public Scene(int width, int height) {
		renderer = new Renderer(width, height);
	}
	
	// rendering the entire scene
	public void render(int[] screenPixels) {
		renderer.render(screenPixels);
	}
}
