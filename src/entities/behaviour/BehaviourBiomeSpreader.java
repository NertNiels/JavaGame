package entities.behaviour;

import entities.Entity;
import renderEngine.DisplayManager;
import timing.Timing;
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
	
	public BehaviourBiomeSpreader(Entity baseEntity, String line, BiomeManager biomeManager) {
		super(baseEntity, BehaviourType.BiomeSpreader);
		this.biomeManager = biomeManager;
//		BIOME strength fade type
		String[] data = line.split(" ");
		this.strength = Float.parseFloat(data[1]);
		this.fade = Float.parseFloat(data[2]);
		BiomeType type = BiomeType.Default;
		if(data[3].equalsIgnoreCase("forest")) type = BiomeType.Forest;
		else if(data[3].equalsIgnoreCase("grassland")) type = BiomeType.Grassland;
		spreader = new BiomeSpreader(baseEntity.getPosition().x, baseEntity.getPosition().z, type, 0);
		biomeManager.addSpreader(spreader);
	}

	@Override
	public void update() {
		if(!fading)return;
		spreader.strength += fade * Timing.getInGameHoursPast() * 0.1f;
		if(spreader.strength >= strength) {spreader.strength = strength;
		fading = false;
		}
		biomeManager.setUpdated(true);
	}

}
