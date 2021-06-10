package graphics;

public class Renderer {
	private int width;
	private int height;
	
	public Renderer(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	// raytracer will happen here soon
	public void render(int[] screenPixels) {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				screenPixels[x + y * width] = 0;
			}
		}
	}
}
