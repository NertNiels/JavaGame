package models.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.joml.Vector3f;

import models.RawModel;
import renderEngine.Loader;

public class ModelLoader {

	public static RawModel loadModel(String fileName, Loader loader) throws IOException {
		FileReader fr = new FileReader("res/" + fileName + ".txt");
		BufferedReader reader = new BufferedReader(fr);
		String line = "";
		
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
		ArrayList<Vector3f> colors = new ArrayList<Vector3f>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] colorsArray = null;
		int[] indicesArray = null;
		
		try {
			while(true) {
				line = reader.readLine();
				String[] data = line.split(" ");
				if(line.startsWith("v ")) {
					float x = Float.parseFloat(data[1]);
					float y = Float.parseFloat(data[2]);
					float z = Float.parseFloat(data[3]);
					int normal = Integer.parseInt(data[4]);
					int color = Integer.parseInt(data[5]);
					vertices.add(new Vertex(x, y, z, normal, color));
				} else if(line.startsWith("vn ")) {
					float x = Float.parseFloat(data[1]);
					float y = Float.parseFloat(data[2]);
					float z = Float.parseFloat(data[3]);
					normals.add(new Vector3f(x, y, z));
				} else if(line.startsWith("vc ")) {
					float x = Float.parseFloat(data[1]);
					float y = Float.parseFloat(data[2]);
					float z = Float.parseFloat(data[3]);
					colors.add(new Vector3f(x, y, z));
				} else if(line.startsWith("f ")) {
					break;
				}
			}
			
			colorsArray = new float[vertices.size() * 3];
			normalsArray = new float[vertices.size() * 3];
			verticesArray = new float[vertices.size() * 3];
			
			while(line != null) {
				String[] data = line.split(" ");
				String[] face = data[1].split("/");
				indices.add(Integer.parseInt(face[0]));
				indices.add(Integer.parseInt(face[1]));
				indices.add(Integer.parseInt(face[2]));
				line = reader.readLine();
			}
			indicesArray = new int[indices.size()];
			for(int i = 0; i < indices.size(); i++) {
				indicesArray[i] = indices.get(i);
			}
			
			for(int i = 0; i < vertices.size(); i++) {
				processVertex(vertices, normals, colors, verticesArray, normalsArray, colorsArray, i);
			}
		} catch (Exception e) {
			System.err.println("Error while parsing line: " + line + ".");
			e.printStackTrace();
		} finally {
			reader.close();
			fr.close();
		}
		
		return loader.loadToVAO(verticesArray, normalsArray, indicesArray, colorsArray);
	}
	
	private static void processVertex(ArrayList<Vertex> vertices, ArrayList<Vector3f> normals, ArrayList<Vector3f> colors, float[] verticesArray, float[] normalsArray, float[] colorsArray, int i) {
		int index = i * 3;
		Vertex currentVertex = vertices.get(i);
		verticesArray[index] = currentVertex.position.x;
		verticesArray[index + 1] = currentVertex.position.y;
		verticesArray[index + 2] = currentVertex.position.z;
		
		Vector3f currentNormal = normals.get(currentVertex.normal);
		normalsArray[index] = currentNormal.x;
		normalsArray[index + 1] = currentNormal.y;
		normalsArray[index + 2] = currentNormal.z;
		
		Vector3f currentColor = colors.get(currentVertex.color);
		colorsArray[index] = currentColor.x;
		colorsArray[index + 1] = currentColor.y;
		colorsArray[index + 2] = currentColor.z;
		
	}
	
}
