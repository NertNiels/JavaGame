package entities.behaviour;

import entities.Entity;

public class BehaviourGrow extends BehaviourBlueprint {

	private int growState = 0;
	private int maxGrowState = 0;
	private float grow = 0;
	private float growSpeed = 0;
	
	public BehaviourGrow(Entity baseEntity, int maxGrowState, float growSpeed) {
		super(baseEntity);
		this.maxGrowState = maxGrowState;
		this.growSpeed = growSpeed;
	}

	@Override
	public void update() {
		if(growSpeed >= 1) {
			growState++;
			if(growState > maxGrowState) {
				growState = maxGrowState;
			}
			grow = 0;
		}
		grow += growSpeed;
	}

}
