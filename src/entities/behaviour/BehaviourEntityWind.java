package entities.behaviour;

import entities.Entity;

public class BehaviourEntityWind extends BehaviourBlueprint {

	public BehaviourEntityWind(Entity baseEntity) {
		super(baseEntity);
		baseEntity.setDistortionFactor(1);
	}

	@Override
	public void update() {
//		baseEntity.increaseRotation(0, 1, 0);
	}

}
