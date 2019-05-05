package entities;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.behaviour.BehaviourBlueprint;
import entities.behaviour.BehaviourType;
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
	private float distortionFactor = 0;
	public final EntityType type;
	private ArrayList<BehaviourBlueprint> behaviours = new ArrayList<BehaviourBlueprint>();

	public Entity(Model model, Vector3f position, float rotX, float rotY, float rotZ, float scale, EntityType type) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.type = type;
	}
	
	public Entity(Model model, World world, Vector2f position, float scale, EntityType type) {
		this.model = model;
		this.position = new Vector3f(position.x, 0, position.y);
		this.scale = scale;
		this.type = type;
		
		float height = world.getHeightAt(position.x, position.y) * Configs.HEIGHT_SCALE_FACTOR;
		this.increasePosition(0, height, 0);
	}
	
	public void addBehaviour(BehaviourBlueprint behaviour) {
		behaviours.add(behaviour);
	}
	
	public void addBehaviours(BehaviourBlueprint[] behaviourArray) {
		for(BehaviourBlueprint b : behaviourArray) {
			behaviours.add(b);
		}
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	
	public void increasePosition(Vector3f delta) {
		this.position.x += delta.x;
		this.position.y += delta.y;
		this.position.z += delta.z;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}
	
	public void update() {
		for(BehaviourBlueprint b : behaviours) {
			b.update();
		}
	}

	public Model getModel() {
		return model;
	}

	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
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
	
	public float getDistortionFactor() {
		return distortionFactor;
	}
	
	public void setDistortionFactor(float distortionFactor) {
		this.distortionFactor = distortionFactor;
	}
	
	public BehaviourBlueprint getBehaviour(BehaviourType type) {
		for(BehaviourBlueprint b:behaviours) {
			if(b.getType() == type) return b;
		}
		return null;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}

}
