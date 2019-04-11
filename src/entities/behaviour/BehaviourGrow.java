package entities.behaviour;

import java.util.ArrayList;

import entities.Entity;
import main.MainGameLoop;
import models.io.ModelLoader;
import renderEngine.Loader;

public class BehaviourGrow extends BehaviourBlueprint {

	private int growState = 0;
	private int maxGrowState = 0;
	private float grow = 0;
	private float growSpeed = 0;
	private boolean adult = false;
	
	private Integer[] models;
	
	public BehaviourGrow(Entity baseEntity, int maxGrowState, float growSpeed) {
		super(baseEntity, BehaviourType.Grow);
		this.maxGrowState = maxGrowState;
		this.growSpeed = growSpeed;
		baseEntity.setScale(0);
	}
	
	public BehaviourGrow(Entity baseEntity, String line, Loader loader) {
		super(baseEntity, BehaviourType.Grow);
		String[] data = line.split(" ");
//		GROW max_grow grow_speed model_index[]
		this.maxGrowState = Integer.parseInt(data[1]);
		this.growSpeed = Float.parseFloat(data[2]);
		ArrayList<Integer> modelData = new ArrayList<Integer>();
		for(int i = 3; i < this.maxGrowState + 3; i++) {
			modelData.add(Integer.parseInt(data[i]));
		}
		models = modelData.toArray(new Integer[modelData.size()]);
		baseEntity.getModel().setRawModel(ModelLoader.getModel(models[0], loader));
	}

	@Override
	public void update() {
		if(grow >= 1 && !adult) {
			growState++;
//			baseEntity.setScale((float)growState / (float)maxGrowState);
			if(growState >= maxGrowState) {
				growState = maxGrowState-1;
				adult = true;
			}
			grow = 0;
			baseEntity.getModel().setRawModel(ModelLoader.getModel(models[growState], MainGameLoop.loader));
		}
		grow += growSpeed;
	}
	
	public boolean isAdult() {
		return adult;
	}

}
