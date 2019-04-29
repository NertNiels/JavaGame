package main;

import java.util.Random;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import toolbox.Maths;
import world.sky.SkyColor;

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
	
	public static final Vector3f BIOME_COLOR_DEFAULT = new Vector3f(0.47058823529f,0.28235294117f,0);
	public static final Vector3f BIOME_COLOR_GRASSLAND = new Vector3f(0.33333333333f,0.6f,0);
	public static final Vector3f BIOME_COLOR_FOREST = new Vector3f(0.13333333333f, 0.54509803921f, 0.13333333333f);
	public static final Vector3f BIOME_COLOR_BEACH = new Vector3f(0.95686274509f, 0.88235294117f, 0.70980392156f);
	

	public static final float BIOME_MAX_RANGE = 30f;

	
	public static int SCREEN_WIDTH = 1280;
	public static int SCREEN_HEIGHT = 720;
	public static final boolean FULL_SCREEN = false;
	public static final int FPS_CAP = 120;
	
	
	public static final float FOV = 80;
	public static final float FAR_PLANE = 1500;
	public static final float NEAR_PLANE = 0.1f;
	
	
	public static final SkyColor[] SKY_COLORS = new SkyColor[] {
			new SkyColor(Maths.hex("#101010"), Maths.hex("#101010"), 3),
			new SkyColor(Maths.hex("#0099ff"), Maths.hex("#ffff66"), 5),
			new SkyColor(Maths.hex("#33ccff"), Maths.hex("#ccf2ff"), 8),
			new SkyColor(Maths.hex("#33ccff"), Maths.hex("#ccf2ff"), 17),
			new SkyColor(Maths.hex("#33ccff"), Maths.hex("#ffc266"), 21),
			new SkyColor(Maths.hex("#20657d"), Maths.hex("#664d28"), 22),
			new SkyColor(Maths.hex("#101010"), Maths.hex("#101010"), 23),
			
	};
	
	public static float FOG_DENSITY = 0.0015f;
	public static final float MIN_FOG_DENSITY = 0.0015f;
	public static final float MAX_FOG_DENSITY = 0.001f;
	
	public static final int GRID_WORLD_WIDTH = 1;
	public static final int GRID_WORLD_HEIGHT = 1;
	public static final int VERTEX_COUNT = 100;			// DEFAULT: 100
	public static final int SIZE = 800;
	
	public static final float MIN_ZOOM = 5;
	public static final float MAX_ZOOM = 300;
	
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
	
	public static Random RANDOM;
	
	public static final float MIN_TIME_SCALE = 0;
	public static final float[] TIME_SCALE_TABLE = new float[] {
			0,
			36f/600f,
			72f/600f,
			144f/600f,
			288f/600f,
			1728/600f,
			3456/600f,
			6912/600f,
	};
	public static final String[] TIME_NAME_TABLE = new String[] {
			"paused",
			"1 hour/minute",
			"2 hours/minute",
			"4 hours/minute",
			"8 hours/minute",
			"1 day/minute",
			"2 days/minute",
			"4 days/minute"
	};
	
	static {
		if(BIOME_COLORS.length != BIOME_HEIGHTS.length) {
			throw new IllegalArgumentException("BIOME_COLORS and BIOME_HEIGHTS aren't the same length.");
		}
		if(SEED == 0) SEED = (long)(Math.random() * 2000000);
		RANDOM = new Random(SEED);
	}
}
