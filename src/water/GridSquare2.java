package water;

import org.lwjgl.util.vector.Vector2f;

import models.RawModel;
import renderEngine.Loader;

public class GridSquare2 {

	public static final int VERTICES_PER_SQUARE = 3 * 2;
	private static int i = 0;
	
	public static RawModel generatePlane(Loader loader, int gridCount, int size) {
		i = 0;
		int totalVertices = gridCount * gridCount * VERTICES_PER_SQUARE;
		float gridSquareSize = (float)size/(float)gridCount;
		float[] vertices = new float[totalVertices * 2];
		byte[] indicators = new byte[totalVertices * 4];
		
		for(int row = 0; row < gridCount; row++) {
			for(int col = 0; col < gridCount; col++) {
				storeGridSquare(col, row, vertices, indicators, gridSquareSize);
			}
		}
		
		return loader.loadToVAO(vertices, indicators);
	}
	
	private static void storeGridSquare(int col, int row, float[] vertices, byte[] indicators, float gridSquareSize) {
		Vector2f[] corners = getCornerPositions(col, row, gridSquareSize);
		storeTriangleLeft(corners, vertices, indicators);
		storeTriangleRight(corners, vertices, indicators);
	}
	
	private static void storeTriangleLeft(Vector2f[] corners, float[] vertices, byte[] indicators) {
		vertices[i*2] = corners[0].x;
		vertices[i*2+1] = corners[0].y;
		indicators[i*4] = (byte)(corners[1].x - corners[0].x);
		indicators[i*4+1] = (byte)(corners[1].y - corners[0].y);
		indicators[i*4+2] = (byte)(corners[2].x - corners[0].x);
		indicators[i*4+3] = (byte)(corners[2].y - corners[0].y);
		i++;
		vertices[i*2] = corners[1].x;
		vertices[i*2+1] = corners[1].y;
		i++;
		vertices[i*2] = corners[2].x;
		vertices[i*2+1] = corners[2].y;
		i++;
	}
	
	private static void storeTriangleRight(Vector2f[] corners, float[] vertices, byte[] indicators) {
		vertices[i*2] = corners[3].x;
		vertices[i*2+1] = corners[3].y;
		indicators[i*4] = (byte)(corners[2].x - corners[3].x);
		indicators[i*4+1] = (byte)(corners[2].y - corners[3].y);
		indicators[i*4+2] = (byte)(corners[1].x - corners[3].x);
		indicators[i*4+3] = (byte)(corners[1].y - corners[3].y);
		i++;
		vertices[i*2] = corners[2].x;
		vertices[i*2+1] = corners[2].y;
		i++;
		vertices[i*2] = corners[1].x;
		vertices[i*2+1] = corners[1].y;
		i++;
	}
	
	private static Vector2f[] getCornerPositions(int col, int row, float gridSquareSize) {
		Vector2f[] corners = new Vector2f[4];
		corners[0] = new Vector2f(col * gridSquareSize, row * gridSquareSize);
		corners[1] = new Vector2f(col * gridSquareSize, (row + 1) * gridSquareSize);
		corners[2] = new Vector2f((col + 1) * gridSquareSize, row * gridSquareSize);
		corners[3] = new Vector2f((col + 1) * gridSquareSize, (row + 1) * gridSquareSize);
		return corners;
	}
	
}
