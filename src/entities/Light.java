package entities;

import org.joml.Vector3f;

public class Light {

	private Vector3f postition;
	private Vector3f color;
	
	public Light(Vector3f postition, Vector3f color) {
		this.postition = postition;
		this.color = color;
	}

	public Vector3f getPostition() {
		return postition;
	}

	public void setPostition(Vector3f postition) {
		this.postition = postition;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	
	
}
