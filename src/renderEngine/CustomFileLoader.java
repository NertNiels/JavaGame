package renderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;

public class CustomFileLoader {

	public static RawModel loadCustomModel(String fileName, Loader loader) throws IOException {
		FileReader fr = null;
		try {
			fr = new FileReader(new File("res/" + fileName + ".txt"));
		} catch (FileNotFoundException e) {
			System.err.println("Could not load file.");
			e.printStackTrace();
		}

		BufferedReader reader = new BufferedReader(fr);
		String line;
		ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
		ArrayList<Vector2f> textures = new ArrayList<Vector2f>();
		ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		ArrayList<Vector3f> colors = new ArrayList<Vector3f>();
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] textureArray = null;
		float[] colorsArray = null;
		int[] indicesArray = null;

		boolean hasTextures = false;
		boolean hasColors = false;

		try {
			while (true) {
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				if (line.startsWith("t ")) {
					if (currentLine[1].trim().contains("yes"))
						hasTextures = true;
				}
				if (line.startsWith("c ")) {
					if (currentLine[1].trim().contains("yes"))
						hasColors = true;
				}
				if (line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				} else if (line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					textures.add(texture);
				} else if (line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3]));
					normal.normalise();
					normals.add(normal);
				} else if (line.startsWith("vc ")) {
					Vector3f color = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3]));
					colors.add(color);
				} else if (line.startsWith("f ")) {
					if (hasTextures)
						textureArray = new float[vertices.size() * 2];
					if (hasColors)
						colorsArray = new float[vertices.size() * 3];
					normalsArray = new float[vertices.size() * 3];
					break;
				}
			}

			while (line != null) {
				if (!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}

				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");

				processVertex(vertex1, indices, textures, normals, colors, textureArray, normalsArray, colorsArray,
						hasTextures, hasColors);
				processVertex(vertex2, indices, textures, normals, colors, textureArray, normalsArray, colorsArray,
						hasTextures, hasColors);
				processVertex(vertex3, indices, textures, normals, colors, textureArray, normalsArray, colorsArray,
						hasTextures, hasColors);
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];

		int vertexPointer = 0;
		for (Vector3f vertex : vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}

		for (int i = 0; i < indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}

		if(hasTextures) return loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray);
		if(hasColors) return loader.loadToVAO(verticesArray, normalsArray, indicesArray, colorsArray);
		System.err.println("Could not load object. not colors or textures found. Please make sure t OR s is set to yes");
		System.exit(-1);
		return null;
	}

	private static void processVertex(String[] vertexData, ArrayList<Integer> indices, ArrayList<Vector2f> textures,
			ArrayList<Vector3f> normals, ArrayList<Vector3f> colors, float[] textureArray, float[] normalsArray,
			float[] colorsArray, boolean hasTextures, boolean hasColors) {

		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);
		if (hasTextures) {
			Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
			textureArray[currentVertexPointer * 2] = currentTex.x;
			textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
		}
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[currentVertexPointer * 3] = currentNorm.x;
		normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
		normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
		if (hasColors) {
			Vector3f currentColor = colors.get(Integer.parseInt(vertexData[3]) - 1);
			colorsArray[currentVertexPointer * 3] = currentColor.x;
			colorsArray[currentVertexPointer * 3+1] = currentColor.y;
			colorsArray[currentVertexPointer * 3+2] = currentColor.z;
			
		}
	}

}
