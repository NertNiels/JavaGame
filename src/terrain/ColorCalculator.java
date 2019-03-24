package terrain;

import org.joml.Vector3f;
import main.Configs;
import toolbox.Maths;

public class ColorCalculator {

	public static Vector3f[][] generateColorMap(Vector3f color, int width, int height) {
		Vector3f[][] colors = new Vector3f[width][height];
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				colors[i][j] = new Vector3f(color);
			}
		}
		return colors;
	}
	
	public static Vector3f[][] generateColorMap(float[][] heightMap) {
		int width = heightMap.length;
		int height = heightMap[0].length;
		Vector3f[][] colors = new Vector3f[width][height];
		
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				for(int c = 0; c < Configs.BIOME_COLORS.length; c++) {
					if(heightMap[i][j] <= Configs.BIOME_HEIGHTS[c]) {
						colors[i][j] = getColorAtHeight(heightMap[i][j]);
						break;
					}
				}
			}
		}
		
		return colors;
	}
	
	private static Vector3f getColorAtHeight(float height) {
		for(int c = Configs.BIOME_COLORS.length - 1; c >= 0; c--) {
			if(Configs.BIOME_HEIGHTS[c] < height) {
				
				float blendFactor = (height - Configs.BIOME_HEIGHTS[c]) / (Configs.BIOME_HEIGHTS[c+1] - Configs.BIOME_HEIGHTS[c]);
				
				Vector3f color = Maths.interpolateColor(Configs.BIOME_COLORS[c], Configs.BIOME_COLORS[c+1], blendFactor);
				return color;
			}
		}
		return Configs.BIOME_COLORS[0];
	}
}
