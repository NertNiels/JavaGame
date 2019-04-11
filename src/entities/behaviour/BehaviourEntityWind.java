package entities.behaviour;

import entities.Entity;

public class BehaviourEntityWind extends BehaviourBlueprint {

	public BehaviourEntityWind(Entity baseEntity) {
		super(baseEntity, BehaviourType.EntityWind);
		baseEntity.setDistortionFactor(1);
	}
	
	public BehaviourEntityWind(Entity baseEntity, String line) {
		super(baseEntity, BehaviourType.EntityWind);
		baseEntity.setDistortionFactor(Float.parseFloat(line.split(" ")[1]));
	}

	@Override
	public void update() {
		
	}

}
