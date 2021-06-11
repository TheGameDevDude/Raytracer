package graphics;

public class Color {
	public int red;
	public int green;
	public int blue;
	public int shininess;
	
	public Color(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.shininess = 0;
	}

	public Color(int red, int green, int blue, int shininess) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.shininess = shininess;
	}

	public int getBrightness() {
		return (red + green + blue) / 3;
	}
}
