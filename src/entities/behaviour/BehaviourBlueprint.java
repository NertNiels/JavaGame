package entities.behaviour;

import entities.Entity;

public abstract class BehaviourBlueprint {

	Entity baseEntity;
	BehaviourType type;
	
	public BehaviourBlueprint(Entity baseEntity, BehaviourType type) {
		this.baseEntity = baseEntity;
		this.type = type;
	}
	
	public abstract void update();
	
	public BehaviourType getType() {
		return type;
	}
}
