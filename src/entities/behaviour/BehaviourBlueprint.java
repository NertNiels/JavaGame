package entities.behaviour;

import entities.Entity;

public abstract class BehaviourBlueprint {

	Entity baseEntity;
	
	public BehaviourBlueprint(Entity baseEntity) {
		this.baseEntity = baseEntity;
	}
	
	public abstract void update();
}
