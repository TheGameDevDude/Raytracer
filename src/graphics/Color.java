package graphics;

public class Color {
	public int red;
	public int green;
	public int blue;
	public float shininess;
	
	public Color(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.shininess = 0.0f;
	}

	public Color(int red, int green, int blue,float shininess) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.shininess = shininess;
	}
}
