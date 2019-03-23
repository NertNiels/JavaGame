package terrain;

import org.lwjgl.util.vector.Vector3f;

public class Vertex {

	private Vector3f position;
	private Vector3f normal;
	private Vector3f color;
	private Vertex vertexCopy = null;
	private int currentIndex;
	
	public Vertex(Vector3f vertex, Vector3f normal, Vector3f secondNormal, Vector3f color) {
		this.position = vertex;
		this.normal = normal;
		this.color = color;
		
		if(secondNormal != null) vertexCopy = new Vertex(vertex, secondNormal, this, color);
	}
	
	public Vertex(Vector3f vertex, Vector3f normal, Vector3f color) {
		this.position = vertex;
		this.normal = normal;
		this.color = color;
	}
	
	public Vertex(Vector3f vertex, Vector3f normal, Vertex vertexCopy, Vector3f color) {
		this.position = vertex;
		this.normal = normal;
		this.color = color;
		this.vertexCopy = vertexCopy;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
		if(vertexCopy != null) vertexCopy.position = position;
	}
	public Vector3f getNormal() {
		return normal;
	}
	public void setNormal(Vector3f normal) {
		this.normal = normal;
		this.normal.normalise();
	}
	public Vector3f getColor() {
		return color;
	}
	public void setColor(Vector3f color) {
		this.color = color;
		if(vertexCopy != null) vertexCopy.color = color;
	}
	
	public Vertex getVertexCopy() {
		return vertexCopy;
	}
	
	public void setCurrentIndex(int index) {
		this.currentIndex = index;
	}
	
	public int getCurrentIndex() {
		return currentIndex;
	}
	
	public void setNormalCopy(Vector3f normal) {
		vertexCopy.setNormal(normal);
	}
	
}
