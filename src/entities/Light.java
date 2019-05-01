package entities;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Light {

	private Vector3f postition;
	private Vector3f color;
	private Vector2f bias = new Vector2f(0.3f, 0.8f);
	
	public Light(Vector3f postition, Vector3f color) {
		this.postition = postition;
		this.color = color;
	}
	
	public Light(Vector3f postition, Vector3f color, Vector2f bias) {
		this.postition = postition;
		this.color = color;
		this.bias = bias;
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
	
	public Vector2f getBias() {
		return bias;
	}
	
	public void setBias(Vector2f bias) {
		this.bias = bias;
	}
	
	
	
}
