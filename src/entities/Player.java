package entities;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.behaviour.BehaviourRigidBody;
import entities.camera.Camera;
import main.Configs;
import world.World;

public class Player extends Entity {
	
	private Camera camera;

	public Player(World world, Vector2f position, Camera camera) {
		super(null, world, position, 1, EntityType.Player);
		world.addEntity(this);
		this.camera = camera;
		camera.setPosition(new Vector3f(position.x, getPosition().y, position.y));
		super.addBehaviour(new BehaviourRigidBody(this, world));
	}
	
	@Override
	public void setPosition(Vector3f position) {
		super.setPosition(position);
		if(camera == null) return;
		camera.setPosition(new Vector3f(position.x, position.y + Configs.PLAYER_HEIGHT, position.z));
	}
	
	@Override
	public void increasePosition(Vector3f position) {
		super.increasePosition(position);
		if(camera == null) return;
		camera.inceasePosition(new Vector3f(position.x, position.y, position.z));
	}

}
