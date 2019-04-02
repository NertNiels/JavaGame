package world;

import org.lwjgl.util.vector.Vector2f;

public class BiomeSpreader {

	public float x;
	public float y;
	public BiomeType type;
	public float strength;
	
	public BiomeSpreader(float x, float y, BiomeType type, float strength) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.strength = strength;
	}
	
	public Vector2f getPosition() {
		return new Vector2f(x, y);
	}
	
}
