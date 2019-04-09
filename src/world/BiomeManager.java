package world;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import main.Configs;
import renderEngine.Loader;
import toolbox.Maths;

public class BiomeManager {

	private ArrayList<BiomeSpreader> spreaders = new ArrayList<BiomeSpreader>();
	private BiomeType[][] biomeMap;
	private int texture;
	private boolean updated = true;

	public BiomeManager(Loader loader) {
		texture = loader.loadTexture();
	}

	public int getTexture(Loader loader) {
		if (!updated)
			return texture;
		float[] pixels = new float[Configs.VERTEX_COUNT * Configs.VERTEX_COUNT * 4];
		int i = 0;
		for (int y = 0; y < Configs.VERTEX_COUNT; y++) {
			for (int x = 0; x < Configs.VERTEX_COUNT; x++) {
				pixels[i + 3] = 1f;
				
				ArrayList<SpreaderStrength> colors = new ArrayList<SpreaderStrength>();
				float totalStrength = 1;
				for (BiomeSpreader spreader : spreaders) {
					float distance = new Vector2f(spreader.x - (x+1) * ((float)Configs.SIZE/(float)Configs.VERTEX_COUNT), spreader.y - (y+1) * ((float)Configs.SIZE/(float)Configs.VERTEX_COUNT)).length();
					if (distance <= Configs.BIOME_MAX_RANGE) {
						float strength = spreader.strength * (1-(distance / Configs.BIOME_MAX_RANGE));
						Vector3f newColor = Configs.BIOME_COLOR_DEFAULT;
						if(spreader.type == BiomeType.Grassland) newColor = Configs.BIOME_COLOR_GRASSLAND;
						if(spreader.type == BiomeType.Forest) newColor = Configs.BIOME_COLOR_FOREST;
						if(spreader.type == BiomeType.Beach) newColor = Configs.BIOME_COLOR_BEACH;

						
						colors.add(new SpreaderStrength(newColor, strength));
						totalStrength += strength;
					}
				}
				Vector3f finalColor = Configs.BIOME_COLOR_DEFAULT;
				for(SpreaderStrength spreader:colors) {
					finalColor = spreader.interpolate(finalColor, totalStrength);
				}
				pixels[i] = finalColor.x;
				pixels[i + 1] = finalColor.y;
				pixels[i + 2] = finalColor.z;
				
				i += 4;
			}
		}
		updated = false;
		texture = loader.loadTexture(texture, pixels, Configs.VERTEX_COUNT, Configs.VERTEX_COUNT);
		return texture;
	}

	public void addSpreader(BiomeSpreader spreader) {
		spreaders.add(spreader);
		updated = true;
	}
	
	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
}

class SpreaderStrength {
	public Vector3f color;
	public float strength;
	
	public SpreaderStrength(Vector3f color, float strength) {
		this.color = color;
		this.strength = strength;
	}
	
	public Vector3f interpolate(Vector3f color, float totalStrength) {
		return Maths.interpolateColor(color, this.color, strength/totalStrength);
	}
}
