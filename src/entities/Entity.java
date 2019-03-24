package entities;

import org.joml.Vector2f;
import org.joml.Vector3f;

import main.Configs;
import models.Model;
import models.RawModel;
import models.TexturedModel;
import world.World;

public class Entity {

	private Model model;
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float scale;

	public Entity(Model model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	public Entity(Model model, World world, Vector2f position, float scale) {
		this.model = model;
		this.position = new Vector3f(position.x, 0, position.y);
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		
		float height = world.getHeightAt(position.x, position.y) * Configs.HEIGHT_SCALE_FACTOR;
		this.increasePosition(0, height, 0);
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}
	
	public void update() {
		
	}

	public Model getModel() {
		return model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotX() {
		return rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public float getScale() {
		return scale;
	}

	public RawModel getRawModel() {
		return model.getRawModel();
	}
	
	public TexturedModel getTexturedModel() {
		return model.getTexturedModel();
	}

}
