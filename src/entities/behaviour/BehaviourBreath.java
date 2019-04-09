package entities.behaviour;

import org.lwjgl.util.vector.Vector2f;

import entities.Entity;
import main.Configs;
import world.BiomeType;
import world.World;

public class BehaviourBreath extends BehaviourBlueprint {

	private boolean canBreath = false;
	private float breathChance = 0f;
	private float breathRange = 1f;
	private int maxEntitiesInRange = 0;
	private long timer = 0;
	private long breathTime = 0;

	public BehaviourBreath(Entity baseEntity, boolean canBreath, float breathChance, float breathRange,
			int maxEntitiesInRange) {
		super(baseEntity, BehaviourType.Breath);
		this.canBreath = canBreath;
		this.breathChance = breathChance;
		this.breathRange = breathRange;
		this.maxEntitiesInRange = maxEntitiesInRange;
		this.breathTime = 600;
	}

	@Override
	public void update() {
		timer--;
		if (timer < 0) {
			if (!canBreath) {
				BehaviourGrow grow = (BehaviourGrow) baseEntity.getBehaviour(BehaviourType.Grow);
				if (grow == null)
					return;
				canBreath = grow.isAdult();
				return;
			}
			if (Configs.RANDOM.nextFloat() < breathChance) {
				breath();
			}
		}

	}

	private void breath() {
		Vector2f position = new Vector2f(baseEntity.getPosition().x, baseEntity.getPosition().z);
		int numberOfEntities = World.world.getNumberOfEntitiesInRange(position, breathRange) - 1;
		if (numberOfEntities >= maxEntitiesInRange)
			return;
		boolean breakOut = false;
		int i = 0;
		while(!breakOut) {
			if(i == 100) return;
			position = new Vector2f(baseEntity.getPosition().x + ((float) Configs.RANDOM.nextFloat() * 2 - 1) * breathRange,
					baseEntity.getPosition().z + ((float) Configs.RANDOM.nextFloat() * 2 - 1) * breathRange);
			if(World.world.getNumberOfEntitiesInRange(position, breathRange*0.2f) == 0) breakOut = true;
			i++;
		}
		Entity newEntity = new Entity(baseEntity.getModel(), World.world, position, 1);
		newEntity.addBehaviour(new BehaviourEntityWind(newEntity));
		newEntity.addBehaviour(
				new BehaviourBiomeSpreader(newEntity, World.world.getBiomeManager(), 1, 0.1f, BiomeType.Grassland));
		newEntity.addBehaviour(new BehaviourGrow(newEntity, 1, 0.01f));
		newEntity.addBehaviour(new BehaviourBreath(newEntity, false, 0.2f, Configs.BIOME_MAX_RANGE, 5));
		World.world.addEntity(newEntity);
		timer = breathTime;
	}

}
