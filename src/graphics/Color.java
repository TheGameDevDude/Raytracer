package graphics;

public class Color {
	public int red;
	public int green;
	public int blue;

	public Color(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public int getBrightness() {
		return (red + green + blue) / 3;
	}

	public float multiply(Color a, Color b) {
		return (a.red * b.red + a.green * b.green + a.blue * b.blue) / 255;
	}

	public Color average(Color a, Color b) {
		return new Color((a.red + b.red) / 2, (a.green + b.green) / 2, (a.blue + b.blue) / 2);
	}

}
