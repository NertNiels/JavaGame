package gui;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

public class GuiTexture {

	private int texture;
	private Vector2f position;
	private Vector2f scale;
	private Vector4f color;
	private float opacity;
	
	public GuiTexture(int texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		color = new Vector4f(1, 1, 1, 1);
		opacity = 1;
	}
	
	public GuiTexture(int texture, Vector2f position, Vector2f scale, Vector4f color, float opacity) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		this.color = color;
		this.opacity = opacity;
	}
	
	public int getTexture() {
		return texture;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public Vector2f getScale() {
		return scale;
	}
	
	public Vector4f getColor() {
		return color;
	}
	
	public float getOpacity() {
		return opacity;
	}
	
	public void setColor(Vector4f color) {
		this.color = color;
	}
	
	public void setColor(float r, float g, float b, float a) {
		this.color = new Vector4f(r, g, b, a);
	}
	
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
	
	public Vector2f getTopLeft() {
		return Vector2f.sub(position, new Vector2f(scale.x/2, scale.y/2), null);
	}
	
	public Vector2f getBottomRight() {
		return Vector2f.add(position, new Vector2f(scale.x/2, scale.y/2), null);
	}
}
