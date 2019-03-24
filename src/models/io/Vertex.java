package models.io;

import org.joml.Vector3f;

public class Vertex {

	Vector3f position;
	int normal;
	int color;
	
	public Vertex(Vector3f position, int normal, int color) {
		this.position = position;
		this.normal = normal;
		this.color = color;
	}
	
	public Vertex(float x, float y, float z, int normal, int color) {
		this.position = new Vector3f(x, y, z);
		this.normal = normal;
		this.color = color;
	}
}
