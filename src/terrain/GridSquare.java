package terrain;

import org.lwjgl.util.vector.Vector3f;

public class GridSquare {

	private int vertexCount;

	private Vertex[][] vertices;
	private Index[] indices;

	private float[] vertexArray;
	private float[] normalArray;
	private float[] colorArray;
	private int[] indexArray;

	public GridSquare(int vertexCount, Vertex[][] vertices, Index[] indices) {
		this.vertexCount = vertexCount;
		this.vertices = vertices;
		this.indices = indices;
	}

	public void generateArrays() {
		int totalCount = calculateNumberOfVertices(vertexCount);
		vertexArray = new float[totalCount * 3];
		normalArray = new float[totalCount * 3];
		colorArray = new float[totalCount * 3];
		indexArray = new int[6 * (vertexCount - 1) * (vertexCount - 1)];

		int vertexPointer = 0;
		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < vertexCount; j++) {

				vertexArray[vertexPointer * 3] = vertices[i][j].getPosition().x;
				vertexArray[vertexPointer * 3 + 1] = vertices[i][j].getPosition().y;
				vertexArray[vertexPointer * 3 + 2] = vertices[i][j].getPosition().z;

				normalArray[vertexPointer * 3] = vertices[i][j].getNormal().x;
				normalArray[vertexPointer * 3 + 1] = vertices[i][j].getNormal().y;
				normalArray[vertexPointer * 3 + 2] = vertices[i][j].getNormal().z;

				colorArray[vertexPointer * 3] = vertices[i][j].getColor().x;
				colorArray[vertexPointer * 3 + 1] = vertices[i][j].getColor().y;
				colorArray[vertexPointer * 3 + 2] = vertices[i][j].getColor().z;

				vertices[i][j].setCurrentIndex(vertexPointer);

				vertexPointer++;
			}
		}

		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < vertexCount; j++) {
				if (vertices[i][j].getVertexCopy() != null) {
					vertexArray[vertexPointer * 3] = vertices[i][j].getVertexCopy().getPosition().x;
					vertexArray[vertexPointer * 3 + 1] = vertices[i][j].getVertexCopy().getPosition().y;
					vertexArray[vertexPointer * 3 + 2] = vertices[i][j].getVertexCopy().getPosition().z;

					normalArray[vertexPointer * 3] = vertices[i][j].getVertexCopy().getNormal().x;
					normalArray[vertexPointer * 3 + 1] = vertices[i][j].getVertexCopy().getNormal().y;
					normalArray[vertexPointer * 3 + 2] = vertices[i][j].getVertexCopy().getNormal().z;

					colorArray[vertexPointer * 3] = vertices[i][j].getVertexCopy().getColor().x;
					colorArray[vertexPointer * 3 + 1] = vertices[i][j].getVertexCopy().getColor().y;
					colorArray[vertexPointer * 3 + 2] = vertices[i][j].getVertexCopy().getColor().z;

					vertices[i][j].getVertexCopy().setCurrentIndex(vertexPointer);

					vertexPointer++;
				}
			}
		}

		int indexPointer = 0;
		int indexCount = vertexCount - 1;
		for (int i = 0; i < indexCount; i++) {
			for (int j = 0; j < indexCount; j++) {

				Index index = IndexCalculator.calculateIndexPositions(indices, indexPointer, i, j, this, indexCount);
				indexArray[indexPointer * 3] = index.getVertex1();
				indexArray[indexPointer * 3 + 1] = index.getVertex2();
				indexArray[indexPointer * 3 + 2] = index.getVertex3();
				indexPointer++;

				index = IndexCalculator.calculateIndexPositions(indices, indexPointer, i, j, this, indexCount);
				indexArray[indexPointer * 3] = index.getVertex1();
				indexArray[indexPointer * 3 + 1] = index.getVertex2();
				indexArray[indexPointer * 3 + 2] = index.getVertex3();
				indexPointer++;

			}
		}
	}

	/**
	 * @param size Total size over over x and z;
	 * @param vertexCount The width and height, for a square map.
	 * @return
	 */
	public static GridSquare generateSquare(float size, int vertexCount) {

		Vertex[][] vertices = new Vertex[vertexCount][vertexCount];
		Index[] indices = new Index[2 * (vertexCount - 1) * (vertexCount - 1)];

		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < vertexCount; j++) {
				Vector3f vertex = new Vector3f((float) i / ((float) vertexCount - 1) * size, 0,
						(float) j / ((float) vertexCount - 1) * size);

				Vector3f normal = new Vector3f(0, 1, 1);
				normal.normalise();

				Vector3f color = new Vector3f((float)i / (float)vertexCount, 1, (float)j / (float)vertexCount);

				if (i != 0 && i != vertexCount - 1 && !(j >= vertexCount - 2)) {
					Vector3f normal2 = new Vector3f(0, 1, 1);
					normal2.normalise();
					
					vertices[i][j] = new Vertex(vertex, normal, normal2, color);
				} else {
					vertices[i][j] = new Vertex(vertex, normal, color);
				}
			}
		}

		int indexPointer = 0;
		for (int i = 0; i < vertexCount - 1; i++) {
			for (int j = 0; j < vertexCount - 1; j++) {
				int topLeft = (j * vertexCount) + i;
				int topRight = topLeft + 1;
				int bottomLeft = ((j + 1) * vertexCount) + i;
				int bottomRight = bottomLeft + 1;

				Index index1 = new Index(topLeft, bottomLeft, topRight);
				Index index2 = new Index(topRight, bottomLeft, bottomRight);

				indices[indexPointer++] = index1;
				indices[indexPointer++] = index2;
			}
		}

		return new GridSquare(vertexCount, vertices, indices);
	}

	public void calculateNormals() {
		int indexPointer = 0;
		for (int i = 0; i < vertexCount - 1; i++) {
			for (int j = 0; j < vertexCount - 1; j++) {
				Index index = indices[indexPointer];

				IndexCalculator.caluculateNormal(index, indexPointer, vertexCount - 1, this, i, j);
				indexPointer++;

				index = indices[indexPointer];
				IndexCalculator.caluculateNormal(index, indexPointer, vertexCount - 1, this, i, j);
				indexPointer++;
			}
		}
	}

	public void applyHeightMap(float[][] heightMap) {
		if (heightMap.length != vertexCount || heightMap[0].length != vertexCount) {
			System.err.println("heightMap and vertexCount don't correspond.");
			return;
		}
		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < vertexCount; j++) {
				vertices[i][j].getPosition().y = heightMap[i][j];
			}
		}
	}
	
	public void applyHeightMap(float[][] heightMap, float scaleFactor) {
		if (heightMap.length != vertexCount || heightMap[0].length != vertexCount) {
			System.err.println("heightMap and vertexCount don't correspond.");
			return;
		}
		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < vertexCount; j++) {
				vertices[i][j].getPosition().y = heightMap[i][j] * scaleFactor;
			}
		}
	}
	
	public void applyColorMap(Vector3f[][] colors) {
		if (colors.length != vertexCount || colors[0].length != vertexCount) {
			System.err.println("colors and vertexCount don't correspond.");
			return;
		}
		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < vertexCount; j++) {
				vertices[i][j].setColor(colors[i][j]);
			}
		}
	}

	public float[] getVertexArray() {
		return vertexArray;
	}

	public float[] getNormalArray() {
		return normalArray;
	}

	public float[] getColorArray() {
		return colorArray;
	}

	public int[] getIndexArray() {
		return indexArray;
	}

	private static int calculateNumberOfVertices(int n) {
		return (n - 2) * (n * 2 - 2) + 2 * n;
	}

	public Vertex getVertexAtPosition(int position) {
		int j = (int) ((float) position / (float) vertexCount);
		int i = position - (vertexCount * j);
		return vertices[i][j];
	}

}
