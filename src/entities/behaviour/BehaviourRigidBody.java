package entities.behaviour;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import main.Configs;
import world.World;

public class BehaviourRigidBody extends BehaviourBlueprint {

	World world;
	
	public BehaviourRigidBody(Entity baseEntity, World world) {
		super(baseEntity, BehaviourType.RigidBody);
		this.world = world;
	}

	@Override
	public void update() {
		Vector3f pos = baseEntity.getPosition();
		float height = Configs.HEIGHT_SCALE_FACTOR * world.getHeightAt(pos.x, pos.z);
		if(pos.y < height) {
			baseEntity.setPosition(new Vector3f(pos.x, height, pos.z));
		}
	}

}
