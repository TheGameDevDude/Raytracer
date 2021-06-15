package window;

import java.util.ArrayList;
import java.util.List;

import graphics.Color;
import graphics.Light;
import graphics.PostProcessing;
import graphics.Renderer;
import math.Vector3f;
import objects.Camera;
import objects.Sphere;
import objects.Triangle;
import objects.Entity;
import objects.Plane;

public class Scene {
	private Renderer renderer;
	private PostProcessing postProcessing;
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Light> lights = new ArrayList<Light>();

	// all the objects in the scene
	public Camera camera = new Camera(new Vector3f(0.0f, 1.0f, -2.7f), new Vector3f(0.0f, 1.0f, 0.0f), 90);
	public Entity sphere_1 = new Sphere(new Vector3f(2.0f, 0.0f, 0.7f).scale(0.7f), 1, new Color(100, 100, 100, 0.3f));
	public Entity sphere_2 = new Sphere(new Vector3f(-2.0f, 0.0f, 0.7f).scale(0.7f), 1, new Color(100, 100, 100, 0.3f));
	public Entity plane_down = new Plane(new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(0.0f, -1.0f, 0.0f), true);
	public Entity plane_right = new Plane(new Vector3f(-1.0f, 0.0f, 0.0f), new Vector3f(4.0f, 0.0f, 0.0f), new Color(50, 50, 100));
	public Entity plane_left = new Plane(new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(-4.0f, 0.0f, 0.0f), new Color(100, 50, 50));
	public Entity plane_back = new Plane(new Vector3f(0.0f, 0.0f, -1.0f), new Vector3f(0.0f, 0.0f, 4.0f), new Color(100, 100, 100));
	public Entity plane_up = new Plane(new Vector3f(0.0f, -1.0f, 0.0f), new Vector3f(0.0f, 5.0f, 0.0f), new Color(100, 100, 50));
	public Entity plane_front = new Plane(new Vector3f(0.0f, 0.0f, 1.0f), new Vector3f(0.0f, 0.0f, -5.0f), new Color(0, 0, 0));
	public Entity triangle = new Triangle(new Vector3f(0.0f, 4.9f, 3.5f), new Vector3f(1.0f, 4.9f, 3.0f), new Vector3f(-1.0f, 4.9f, 3.0f), new Color(255, 255, 255));

	// all lights in the scene
	public Light whiteLight = new Light(new Vector3f(0.0f, 3.0f, 0.5f), new Color(255, 255, 255));

	public Scene(int width, int height) {
		renderer = new Renderer(width, height, 4);
		postProcessing = new PostProcessing(width, height);
		entities.add(plane_down);
		entities.add(plane_right);
		entities.add(plane_left);
		entities.add(plane_back);
		entities.add(plane_up);
		entities.add(plane_front);
		entities.add(triangle);
		entities.add(sphere_1);
		entities.add(sphere_2);
		lights.add(whiteLight);
	}

	// rendering the entire scene
	public void render(int[] screenPixels) {
		renderer.render(screenPixels, camera, entities, lights);
		postProcessing.bloom(screenPixels, 11);
	}
}
