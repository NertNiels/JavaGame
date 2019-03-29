package entities.behaviour;

import entities.Entity;

public class BehaviourTest extends BehaviourBlueprint {

	public BehaviourTest(Entity baseEntity) {
		super(baseEntity);
		baseEntity.setDistortionFactor(1);
	}

	@Override
	public void update() {
//		baseEntity.increaseRotation(0, 1, 0);
	}

}
