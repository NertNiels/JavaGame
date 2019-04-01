package world;

import java.util.ArrayList;

import main.Configs;
import renderEngine.Loader;

public class BiomeManager {

	private ArrayList<BiomeSpreader> spreaders = new ArrayList<BiomeSpreader>();
	private int texture;
	private boolean updated = true;
	
	public BiomeManager(Loader loader) {
		texture = loader.loadTexture();
	}
	
	public int getTexture(Loader loader) {
		if(!updated) return texture;
		float[] pixels = new float[Configs.VERTEX_COUNT * Configs.VERTEX_COUNT * 4];
		for(int i = 0; i < pixels.length; i+=4) {
			pixels[i  ] = (float)i/(float)pixels.length;
			pixels[i+1] = (float)i/(float)pixels.length;
			pixels[i+2] = (float)i/(float)pixels.length;
			pixels[i+3] = 1f;
		}
		updated = false;
		texture = loader.loadTexture(texture, pixels, Configs.VERTEX_COUNT, Configs.VERTEX_COUNT);
		return texture;
	}
}
