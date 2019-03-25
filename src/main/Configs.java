package main;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Configs {

	public static final Vector3f[] BIOME_COLORS = new Vector3f[] {
			new Vector3f(240f/255f, 244f/255f, 119f/255f),
			new Vector3f(240f/255f, 244f/255f, 119f/255f),
			new Vector3f(2f/255f, 137/255f, 0/255f),
			new Vector3f(229f/255f, 149f/255f, 11f/255f),
			new Vector3f(1, 218f/255f, 155f/255f)
	};
	
	public static final float[] BIOME_HEIGHTS = new float[] {
			0f,
			0.2f,
			0.4f,
			0.95f,
			1f,
	};
	
	public static int SCREEN_WIDTH = 1;
	public static int SCREEN_HEIGHT = 1;
	public static final boolean FULL_SCREEN = true;
	public static final int FPS_CAP = 120;
	
	
	public static final float FOV = 80;
	public static final float FAR_PLANE = 1500;
	public static final float NEAR_PLANE = 0.1f;
	
	public static final Vector3f SKY_COLOR_TOP = new Vector3f(0.52734375f, 0.8046875f, 0.91796875f);
	public static final Vector3f SKY_COLOR_BOTTOM = new Vector3f(0.91764705882f, 0.90196078431f, 0.41960784313f);
	public static float FOG_DENSITY = 0.0015f;
	public static final float MIN_FOG_DENSITY = 0.0015f;
	public static final float MAX_FOG_DENSITY = 0.001f;
	
	public static final int GRID_WORLD_WIDTH = 1;
	public static final int GRID_WORLD_HEIGHT = 1;
	public static final int VERTEX_COUNT = 100;			// DEFAULT: 100
	public static final int SIZE = 800;
	
	public static final float MIN_ZOOM = 100;
	public static final float MAX_ZOOM = 400;
	
	public static final float SCALE = 20;				// DEFAULT: 20
	public static long SEED = 0;
	public static final int OCTAVES = 5;
	public static final float PERSISTANCE = 0.5f;
	public static final float LACUNARITY = 2f;
	public static final Vector2f OFFSET = new Vector2f(0, 0);
	public static final float HEIGHT_SCALE_FACTOR = 75f;	// DEFAULT: 75
	
	public static final float FALLOFF_A = 3;
	public static final float FALLOFF_B = 4;
	
	public static final float WATER_HEIGHT = 15f;		// DEFAULT: 15
	
	static {
		if(BIOME_COLORS.length != BIOME_HEIGHTS.length) {
			throw new IllegalArgumentException("BIOME_COLORS and BIOME_HEIGHTS aren't the same length.");
		}
		if(SEED == 0) SEED = (long)(Math.random() * 2000000);
	}
}
