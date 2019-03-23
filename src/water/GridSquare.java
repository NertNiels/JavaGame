package water;

import models.RawModel;
import renderEngine.Loader;

public class GridSquare {

	private float[] vertexArray;
	private float[] textureCoords;
	private int[] indexArray;
	
	public GridSquare(float[] vertexArray, float[] textureCoords, int[] indexArray) {
		this.vertexArray = vertexArray;
		this.textureCoords = textureCoords;
		this.indexArray = indexArray;
	}
	
	public static RawModel generatePlane(Loader loader, int vertexCount, int size){
		int count = vertexCount * vertexCount;
		float[] vertices = new float[count * 2];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6*(vertexCount-1)*(vertexCount-1)];
		int vertexPointer = 0;
		for(int i=0;i<vertexCount;i++){
			for(int j=0;j<vertexCount;j++){
				vertices[vertexPointer*2] = (float)j/((float)vertexCount - 1) * size;
				vertices[vertexPointer*2+1] = (float)i/((float)vertexCount - 1) * size;
				textureCoords[vertexPointer*2] = (float)i/((float)vertexCount - 1);
				textureCoords[vertexPointer*2+1] = (float)j/((float)vertexCount - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<vertexCount-1;gz++){
			for(int gx=0;gx<vertexCount-1;gx++){
				int topLeft = (gz*vertexCount)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*vertexCount)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		
		GridSquare gridSquare = new GridSquare(vertices, textureCoords, indices);
		return loader.loadToVAO(gridSquare);
	}
	
	public float[] getVertexArray() {
		return vertexArray;
	}
	
	public int[] getIndexArray() {
		return indexArray;
	}
	
	public float[] getTextureCoords() {
		return textureCoords;
	}
	
}
