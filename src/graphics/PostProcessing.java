package graphics;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class PostProcessing {
	private final int width;
	private final int height;

	public PostProcessing(int width, int height) {
		this.width = width;
		this.height = height;
	}

	// blurring all the bright parts then mixing with the original screenPixels
	public void bloom(int[] screenPixels, int radius) {
		// filtering all the bright parts
		int[] brightScreenPixels = new int[width * height];
		for (int i = 0; i < brightScreenPixels.length; i++) {
			brightScreenPixels[i] = brightFilter(screenPixels[i]);
		}

		// blurring brightScreenPixels
		gaussianBlur(brightScreenPixels, radius);

		// adding the blurred bright pixels with the original screen pixel
		for (int i = 0; i < screenPixels.length; i++) {
			int screenRed = (screenPixels[i] >> 16) & 0xff;
			int screenGreen = (screenPixels[i] >> 8) & 0xff;
			int screenBlue = (screenPixels[i]) & 0xff;

			int brightScreenRed = (brightScreenPixels[i] >> 16) & 0xff;
			int brightScreenGreen = (brightScreenPixels[i] >> 8) & 0xff;
			int brightScreenBlue = (brightScreenPixels[i]) & 0xff;

			int finalRed = screenRed + brightScreenRed;
			int finalGreen = screenGreen + brightScreenGreen;
			int finalBlue = screenBlue + brightScreenBlue;

			if (finalRed >= 255)
				finalRed = 255;
			if (finalGreen >= 255)
				finalGreen = 255;
			if (finalBlue >= 255)
				finalBlue = 255;

			screenPixels[i] = finalRed << 16 | finalGreen << 8 | finalBlue;
		}
	}

	// gaussian blur
	public void gaussianBlur(int[] screenPixels, int radius) {
		List<Object> kernelAndSum = generateKernel(radius);
		int[] kernel = (int[]) kernelAndSum.get(0);
		int sum = (int) kernelAndSum.get(1);

		int[] processedPixels = new int[width * height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				processedPixels[x + y * width] = blur(kernel, screenPixels, sum, x, y);
			}
		}

		for (int i = 0; i < processedPixels.length; i++) {
			screenPixels[i] = processedPixels[i];
		}
	}

	// using the kernel matrix to modify pixels
	private int blur(int[] kernel, int[] screenPixels, int sum, int xScreenPixel, int yScreenPixel) {
		int radius = (int) Math.sqrt(kernel.length);
		int shift = radius / 2;
		int red = 0, green = 0, blue = 0;

		for (int y = 0; y < radius; y++) {
			for (int x = 0; x < radius; x++) {
				int xp = xScreenPixel - shift + x;
				int yp = yScreenPixel - shift + y;

				int screenColor = getScreenPixelColor(xp, yp, screenPixels);
				int kernelValue = kernel[x + y * radius];

				int screenRed = (screenColor >> 16) & 0xff;
				int screenGreen = (screenColor >> 8) & 0xff;
				int screenBlue = (screenColor) & 0xff;

				red += screenRed * kernelValue;
				green += screenGreen * kernelValue;
				blue += screenBlue * kernelValue;
			}
		}

		red /= sum;
		green /= sum;
		blue /= sum;

		return red << 16 | green << 8 | blue;
	}

	// to filter the bright parts of the image
	private int brightFilter(int screenPixel) {
		int R = (screenPixel >> 16) & 0xff;
		int G = (screenPixel >> 8) & 0xff;
		int B = (screenPixel) & 0xff;

		float red = (float) R * 0.004f;
		float green = (float) G * 0.004f;
		float blue = (float) B * 0.004f;

		float brightness = red * 0.2126f + green * 0.7152f + blue * 0.0722f;

		red *= brightness * 255;
		green *= brightness * 255;
		blue *= brightness * 255;

		if (red >= 255)
			red = 255;
		if (green >= 255)
			green = 255;
		if (blue >= 255)
			blue = 255;

		R = (int) red;
		G = (int) green;
		B = (int) blue;

		return (R << 16) | (G << 8) | (B);
	}

	// generates a kernel matrix for gaussian blur
	private List<Object> generateKernel(int radius) {
		int[] kernel = new int[radius * radius];
		List<Integer> finalPascalSequence = generatePascalsTriangleFinalRow(radius);
		int sum = 0;

		for (int y = 0; y < radius; y++) {
			for (int x = 0; x < radius; x++) {
				kernel[x + y * radius] = finalPascalSequence.get(x) * finalPascalSequence.get(y);
				sum += kernel[x + y * radius];
			}
		}

		return Arrays.asList(kernel, sum);
	}

	// generate pascal's triangle and get the final row
	private List<Integer> generatePascalsTriangleFinalRow(int row) {
		List<Integer> finalRow = new ArrayList<Integer>();
		finalRow.add(1);

		for (int i = 1; i < row; i++) {
			List<Integer> prevRow = finalRow;
			List<Integer> currentRow = new ArrayList<Integer>();

			currentRow.add(1);
			for (int j = 1; j < i; j++) {
				currentRow.add(prevRow.get(j - 1) + prevRow.get(j));
			}
			currentRow.add(1);

			finalRow = currentRow;
		}

		return finalRow;
	}

	private int getScreenPixelColor(int x, int y, int[] screenPixels) {
		if (x < 0 || y < 0 || x >= width || y >= height)
			return 0;
		return screenPixels[x + y * width];
	}
}
