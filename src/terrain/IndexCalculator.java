package terrain;

import org.joml.Vector3f;

public class IndexCalculator {

	public static Index calculateIndexPositions(Index[] indices, int indexPointer, int i, int j, GridSquare gridSquare,
			int indexCount) {
		if (indexPointer % 2 == 0)
			return calculateIndexAtEvenPosition(indices, indexPointer, i, j, gridSquare, indexCount);
		return calculateIndexAtOddPosition(indices, indexPointer, i, j, gridSquare);
	}

	private static Index calculateIndexAtEvenPosition(Index[] indices, int indexPointer, int i, int j,
			GridSquare gridSquare, int indexCount) {
		Index currentIndex = indices[indexPointer];
		Vertex vertex1;
		Vertex vertex2;
		Vertex vertex3;
		if (j == indexCount - 1 && i != 0) {
			vertex1 = gridSquare.getVertexAtPosition(currentIndex.getVertex2());
			vertex2 = gridSquare.getVertexAtPosition(currentIndex.getVertex3());
			vertex3 = gridSquare.getVertexAtPosition(currentIndex.getVertex1());
		} else {
			vertex1 = gridSquare.getVertexAtPosition(currentIndex.getVertex1()).getVertexCopy();
			vertex2 = gridSquare.getVertexAtPosition(currentIndex.getVertex2());
			vertex3 = gridSquare.getVertexAtPosition(currentIndex.getVertex3());
		}
		if (i == 0)
			vertex1 = gridSquare.getVertexAtPosition(currentIndex.getVertex1());

		return new Index(vertex1.getCurrentIndex(), vertex2.getCurrentIndex(), vertex3.getCurrentIndex());
	}

	private static Index calculateIndexAtOddPosition(Index[] indices, int indexPointer, int i, int j,
			GridSquare gridSquare) {
		Index currentIndex = indices[indexPointer];
		Vertex vertex1 = gridSquare.getVertexAtPosition(currentIndex.getVertex1());
		Vertex vertex2 = gridSquare.getVertexAtPosition(currentIndex.getVertex2());
		Vertex vertex3 = gridSquare.getVertexAtPosition(currentIndex.getVertex3());

		return new Index(vertex1.getCurrentIndex(), vertex2.getCurrentIndex(), vertex3.getCurrentIndex());
	}

	public static Vector3f caluculateNormal(Index currentIndex, int indexPointer, int indexCount, GridSquare gridSquare,
			int i, int j) {
		if (indexPointer % 2 == 0)
			return calculateNormalAtEvenPosition(currentIndex, indexCount, gridSquare, i, j);
		return calculateNormalAtOddPosition(currentIndex, gridSquare, indexCount, i, j);
	}

	private static Vector3f calculateNormalAtEvenPosition(Index currentIndex, int indexCount, GridSquare gridSquare,
			int i, int j) {
		Vertex vertex1;
		Vertex vertex2;
		Vertex vertex3;
		if (j == indexCount - 1 && i != 0) {
			vertex1 = gridSquare.getVertexAtPosition(currentIndex.getVertex2());
			vertex2 = gridSquare.getVertexAtPosition(currentIndex.getVertex3());
			vertex3 = gridSquare.getVertexAtPosition(currentIndex.getVertex1());
		} else {
			vertex1 = gridSquare.getVertexAtPosition(currentIndex.getVertex1()).getVertexCopy();
			vertex2 = gridSquare.getVertexAtPosition(currentIndex.getVertex2());
			vertex3 = gridSquare.getVertexAtPosition(currentIndex.getVertex3());
		}
		if (i == 0)
			vertex1 = gridSquare.getVertexAtPosition(currentIndex.getVertex1());
		Vector3f U = new Vector3f();
		vertex2.getPosition().sub(vertex1.getPosition(), U);
		Vector3f V = new Vector3f();
		vertex3.getPosition().sub(vertex1.getPosition(), V);

		Vector3f normal = new Vector3f();
		U.cross(V, normal);
		vertex1.setNormal(normal);
		return normal;
	}

	private static Vector3f calculateNormalAtOddPosition(Index currentIndex, GridSquare gridSquare, int indexCount, int i, int j) {
		Vertex vertex1 = gridSquare.getVertexAtPosition(currentIndex.getVertex1());
		Vertex vertex2 = gridSquare.getVertexAtPosition(currentIndex.getVertex2());
		Vertex vertex3 = gridSquare.getVertexAtPosition(currentIndex.getVertex3());

		Vector3f U = new Vector3f();
		vertex2.getPosition().sub(vertex1.getPosition(), U);
		Vector3f V = new Vector3f();
		vertex3.getPosition().sub(vertex1.getPosition(), V);
		
		if(i == indexCount) System.out.println("i: " + i + ", j: " + j);

		Vector3f normal = new Vector3f();
		U.cross(V, normal);
		vertex1.setNormal(normal);
		return normal;
	}
}
