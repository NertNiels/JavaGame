package water;

import org.joml.Vector2f;

import main.Configs;
import renderEngine.Loader;

public class WaterTile {

	private float height;
	private float x, z;
	private int gridX, gridZ;
	
	public WaterTile(int gridX, int gridZ, float height, Loader loader) {
		this.x = gridX * Configs.SIZE;
		this.z = gridZ * Configs.SIZE;
		this.gridX = gridX;
		this.gridZ = gridZ;
		this.height = height;
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}
	
	public int getGridX() {
		return gridX;
	}
	
	public int getGridZ() {
		return gridZ;
	}
	public Vector2f getGridCoords() {
		return new Vector2f(gridX, gridZ);
	}
}
