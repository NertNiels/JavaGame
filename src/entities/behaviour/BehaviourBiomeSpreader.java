package entities.behaviour;

import entities.Entity;
import renderEngine.DisplayManager;
import world.BiomeManager;
import world.BiomeSpreader;
import world.BiomeType;

public class BehaviourBiomeSpreader extends BehaviourBlueprint {

	BiomeManager biomeManager;
	BiomeSpreader spreader;
	float strength;
	float fade;
	boolean fading = true;
	
	public BehaviourBiomeSpreader(Entity baseEntity, BiomeManager biomeManager, float strength, float fade, BiomeType type) {
		super(baseEntity, BehaviourType.BiomeSpreader);
		spreader = new BiomeSpreader(baseEntity.getPosition().x, baseEntity.getPosition().z, type, 0);
		this.strength = strength;
		this.fade = fade;
		this.biomeManager = biomeManager;
		biomeManager.addSpreader(spreader);
	}

	@Override
	public void update() {
		if(!fading)return;
		spreader.strength += fade * DisplayManager.getFrameTimeSeconds();
		if(spreader.strength >= strength) {spreader.strength = strength;
		fading = false;
		}
		biomeManager.setUpdated(true);
	}

}
