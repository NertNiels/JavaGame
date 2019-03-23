package terrain;

import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import main.Configs;
import models.RawModel;
import renderEngine.Loader;
import toolbox.Maths;
import world.HeightGenerator;

public class Terrain {

	private float x;
	private float z;
	private int offsetX;
	private int offsetZ;
	private RawModel model;

	private float[][] heightMap;

	private int gridX, gridZ;

	public Terrain(int gridX, int gridZ, Loader loader, HeightGenerator heightGenerator) {
		this.x = (gridX * Configs.SIZE);
		this.z = (gridZ * Configs.SIZE);

		this.gridX = gridX;
		this.gridZ = gridZ;

		this.offsetX = (gridX * Configs.VERTEX_COUNT);
		this.offsetZ = (gridZ * Configs.VERTEX_COUNT);

		this.model = generateTerrain(loader, heightGenerator);
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getRawModel() {
		return model;
	}

	public int getGridX() {
		return gridX;
	}

	public int getGridZ() {
		return gridZ;
	}

	public void cleanUp() {
		GL30.glDeleteVertexArrays(getRawModel().getVaoID());
	}

	private RawModel generateTerrain(Loader loader, HeightGenerator heightGenerator) {

		GridSquare gridSquare = GridSquare.generateSquare(Configs.SIZE, Configs.VERTEX_COUNT + 1);

		heightMap = heightGenerator.getTerrainMap(Configs.VERTEX_COUNT + 1, Configs.VERTEX_COUNT + 1, offsetX, offsetZ);
		gridSquare.applyHeightMap(heightMap, Configs.HEIGHT_SCALE_FACTOR);
		gridSquare.calculateNormals();
		Vector3f[][] colorMap = ColorCalculator.generateColorMap(heightMap);
		gridSquare.applyColorMap(colorMap);

		return loader.loadToVAO(gridSquare);
	}

	public Vector3f getNormalAt(float x, float z) {
		float squareX = x / ((float) Configs.SIZE / ((float) Configs.VERTEX_COUNT - 1));
		float squareZ = z / ((float) Configs.SIZE / ((float) Configs.VERTEX_COUNT - 1));

		float inX = squareX % 1;
		float inZ = squareZ % 1;

		if (inX + inZ <= 1) {
			float vertex1 = heightMap[(int) squareX][(int) squareZ];
			float vertex2 = heightMap[(int) squareX][(int) squareZ + 1];
			float vertex3 = heightMap[(int) squareX + 1][(int) squareZ];

			Vector3f U = new Vector3f(0, vertex2 - vertex1, 1);
			Vector3f V = new Vector3f(1, vertex3 - vertex1, 0);

			return Vector3f.cross(U, V, null);
		}

		float vertex1 = heightMap[(int) squareX + 1][(int) squareZ];
		float vertex2 = heightMap[(int) squareX][(int) squareZ];
		float vertex3 = heightMap[(int) squareX][(int) squareZ + 1];

		Vector3f U = new Vector3f(-1, vertex2 - vertex1, 1);
		Vector3f V = new Vector3f(0, vertex3 - vertex1, 1);

		return Vector3f.cross(U, V, null);
	}

	public float getHeightAt(float x, float z) {

		float squareSize = (float)Configs.SIZE / ((float)Configs.VERTEX_COUNT);
		int squareX = (int)(x / squareSize);
		int squareZ = (int)(z / squareSize);

		float inX = (x % squareSize)/squareSize;
		float inZ = (z % squareSize)/squareSize;
		float answer;
		if (inX + inZ <= 1) {
			answer = Maths.barryCentric(new Vector3f(0, heightMap[(int)squareX][(int)squareZ], 0),
					new Vector3f(1, heightMap[(int)squareX + 1][(int)squareZ], 0),
					new Vector3f(0, heightMap[(int)squareX][(int)squareZ + 1], 1), new Vector2f(inX, inZ));
		} else {
			answer = Maths.barryCentric(new Vector3f(1, heightMap[(int)squareX + 1][(int)squareZ], 0),
					new Vector3f(1, heightMap[(int)squareX + 1][(int)squareZ + 1], 1),
					new Vector3f(0, heightMap[(int)squareX][(int)squareZ + 1], 1), new Vector2f(inX, inZ));
		}
		return answer;
	}

}
