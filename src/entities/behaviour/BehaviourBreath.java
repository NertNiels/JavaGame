package entities.behaviour;

import org.lwjgl.util.vector.Vector2f;

import entities.Entity;
import entities.EntityLoader;
import main.Configs;
import timing.Timing;
import world.World;

public class BehaviourBreath extends BehaviourBlueprint {

	private boolean canBreath = false;
	private float breathChance = 0f;
	private float breathRange = 1f;
	private int maxEntitiesInRange = 0;
	private float timer = 0;
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
	
	public BehaviourBreath(Entity baseEntity, String line) {
		super(baseEntity, BehaviourType.Breath);
//		BREATH chance range max_entities time
		String[] data = line.split(" ");
		this.breathChance = Float.parseFloat(data[1]);
		this.breathRange = Float.parseFloat(data[2]);
		this.maxEntitiesInRange = Integer.parseInt(data[3]);
		this.breathTime = Long.parseLong(data[4]);
	}

	@Override
	public void update() {
		timer -= Timing.getInGameHoursPast();
		if (timer < 0) {
			if (!canBreath) {
				BehaviourGrow grow = (BehaviourGrow) baseEntity.getBehaviour(BehaviourType.Grow);
				if (grow == null) {
					canBreath = true;
					timer = breathTime + (Configs.RANDOM.nextFloat() * 2 - 1) * breathTime * 0.01f;
					return;
				}
				canBreath = grow.isAdult();
				if(canBreath) timer = breathTime + (Configs.RANDOM.nextFloat() * 2 - 1) * breathTime * 0.01f;
				return;
			}
			if (Configs.RANDOM.nextFloat() < breathChance) {
				breath();
			}
			timer = 48;
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
		Entity newEntity = EntityLoader.loadEntity(baseEntity.type, position, baseEntity.getScale());
		newEntity.increaseRotation(0, Configs.RANDOM.nextFloat() * 360, 0);

		timer = breathTime + (Configs.RANDOM.nextFloat() * 2 - 1) * breathTime * 0.01f;
		World.world.addEntity(newEntity);
	}

}
