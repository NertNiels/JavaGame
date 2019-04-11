package entities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.util.vector.Vector2f;

import entities.behaviour.BehaviourBiomeSpreader;
import entities.behaviour.BehaviourBreath;
import entities.behaviour.BehaviourEntityWind;
import entities.behaviour.BehaviourGrow;
import models.Model;
import models.io.ModelLoader;
import renderEngine.Loader;
import world.World;

public class EntityLoader {
	
	private static Loader LOADER;

	public static Entity loadEntity(EntityType type, Vector2f position, float scale) {
		try {
			return parseEntity(type.getEntityFile(), type, LOADER, position, scale);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Entity parseEntity(String fileName, EntityType type, Loader loader, Vector2f position, float scale) throws IOException {
		FileReader fr = new FileReader("res/entity/"+fileName+".txt");
		BufferedReader reader = new BufferedReader(fr);
		Entity entity = new Entity(new Model(ModelLoader.getModel(1, loader)), World.world, position, scale, type);
		
		
		String line;
		while((line = reader.readLine()) != null) {
			if(line.startsWith("GROW")) entity.addBehaviour(new BehaviourGrow(entity, line, loader));
			else if(line.startsWith("BREATH")) entity.addBehaviour(new BehaviourBreath(entity, line));
			else if(line.startsWith("BIOME")) entity.addBehaviour(new BehaviourBiomeSpreader(entity, line, World.world.getBiomeManager()));
			else if(line.startsWith("WIND")) entity.addBehaviour(new BehaviourEntityWind(entity, line));
		}
		reader.close();
		return entity;
	}
	
	public static void init(Loader loader) {
		LOADER = loader;
	}
	
}
