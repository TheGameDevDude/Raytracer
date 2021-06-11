package window;

import java.util.ArrayList;
import java.util.List;

import graphics.Color;
import graphics.Light;
import graphics.Renderer;
import math.Vector3f;
import objects.Camera;
import objects.Sphere;
import objects.Entity;
import objects.Plane;

public class Scene {
	private Renderer renderer;
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Light> lights = new ArrayList<Light>();

	// all the objects in the scene
	public Camera camera = new Camera(new Vector3f(1.5f, 1.5f, -1.0f), new Vector3f(0.0f, 0.0f, 0.0f), 90);
	public Entity sphere = new Sphere(new Vector3f(), 1, new Color(200, 50, 20));
	public Entity plane = new Plane(new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(0.0f, -1.0f, 0.0f), new Color(50, 20, 200));
	
	// all lights in the scene
	public Light whiteLight = new Light(new Vector3f(0, 2.0f, -1.0f), new Color(255, 255, 255));

	public Scene(int width, int height) {
		renderer = new Renderer(width, height);
		entities.add(plane);
		entities.add(sphere);
		lights.add(whiteLight);
	}

	// rendering the entire scene
	public void render(int[] screenPixels) {
		renderer.render(screenPixels, camera, entities, lights);
	}
}
