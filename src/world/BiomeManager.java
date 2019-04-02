package world;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import main.Configs;
import renderEngine.Loader;
import toolbox.Maths;

public class BiomeManager {

	private ArrayList<BiomeSpreader> spreaders = new ArrayList<BiomeSpreader>();
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
				pixels[i] = Configs.BIOME_COLOR_DEFAULT.x;
				pixels[i + 1] = Configs.BIOME_COLOR_DEFAULT.y;
				pixels[i + 2] = Configs.BIOME_COLOR_DEFAULT.z;
				pixels[i + 3] = 1f;
				for (BiomeSpreader spreader : spreaders) {
					float distance = new Vector2f(spreader.x - (x+1) * ((float)Configs.SIZE/(float)Configs.VERTEX_COUNT), spreader.y - (y+1) * ((float)Configs.SIZE/(float)Configs.VERTEX_COUNT)).length();
					if (distance <= Configs.BIOME_MAX_RANGE) {
						float strength = spreader.strength * (1-(distance / Configs.BIOME_MAX_RANGE));
						Vector3f color =  Maths.interpolateColor(new Vector3f(pixels[i], pixels[i+1], pixels[i+2]), Configs.BIOME_COLOR_GRASSLAND, strength);
						pixels[i] = color.x;
						pixels[i + 1] = color.y;
						pixels[i + 2] = color.z;
					}
				}
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
