package entities.behaviour;

import entities.Entity;

public class BehaviourGrow extends BehaviourBlueprint {

	private int growState = 0;
	private int maxGrowState = 0;
	private float grow = 0;
	private float growSpeed = 0;
	private boolean adult = false;
	
	public BehaviourGrow(Entity baseEntity, int maxGrowState, float growSpeed) {
		super(baseEntity, BehaviourType.Grow);
		this.maxGrowState = maxGrowState;
		this.growSpeed = growSpeed;
		baseEntity.setScale(0);
	}

	@Override
	public void update() {
		if(grow >= 1 && !adult) {
			growState++;
			baseEntity.setScale((float)growState / (float)maxGrowState);
			if(growState >= maxGrowState) {
				growState = maxGrowState;
				adult = true;
			}
			grow = 0;
		}
		grow += growSpeed;
	}
	
	public boolean isAdult() {
		return adult;
	}

}
