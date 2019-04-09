package main;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.behaviour.BehaviourBiomeSpreader;
import entities.behaviour.BehaviourBlueprint;
import entities.behaviour.BehaviourBreath;
import entities.behaviour.BehaviourEntityWind;
import entities.behaviour.BehaviourGrow;
import gui.GuiManager;
import gui.GuiTexture;
import gui.View;
import input.ButtonListener;
import input.ControllerManager;
import input.MouseManager;
import models.Model;
import models.RawModel;
import models.io.ModelLoader;
import renderEngine.CustomFileLoader;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import toolbox.ScreenPicker;
import world.BiomeType;
import world.HeightGenerator;
import world.World;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();

		Loader loader = new Loader();

		Light light = new Light(new Vector3f(0, 200000, 0), new Vector3f(0.9f, 0.9f, 0.9f));

		Camera camera = new Camera(Configs.SIZE / 2, 100, Configs.SIZE / 2);

		HeightGenerator heightGenerator = new HeightGenerator(Configs.SCALE, Configs.SEED, Configs.OCTAVES,
				Configs.PERSISTANCE, Configs.LACUNARITY, Configs.OFFSET);
		heightGenerator.generateWorldMap(Configs.GRID_WORLD_WIDTH * Configs.VERTEX_COUNT,
				Configs.GRID_WORLD_HEIGHT * Configs.VERTEX_COUNT);
		World world = World.generateWorld(heightGenerator, Configs.SIZE, Configs.SIZE, loader);

		MasterRenderer renderer = new MasterRenderer(loader);

		GuiManager guiManager = new GuiManager(loader);

		ControllerManager.initControllers(guiManager);
		MouseManager.init(renderer, camera, world);
		ScreenPicker screenPicker = new ScreenPicker(camera,renderer.getProjectionMatrix(), world);
		
		RawModel treeModel = null;
		RawModel grassModel = null;
		try {
			treeModel = ModelLoader.loadModel("tree", loader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Model modelTree = new Model(treeModel);
		modelTree.setReflectivity(0.2f);
		modelTree.setShineDamper(10);
		ControllerManager.addListenerA(new ButtonListener() {
			
			@Override
			public void onButtonReleased() {
			}
			
			@Override
			public void onButtonDown() {
				Vector3f newPos = screenPicker.getMiddleOnTerrain();
				if (newPos != null) {
					Entity newEntity = new Entity(modelTree, world,
							new Vector2f(newPos.x, newPos.z), 1f);
					newEntity.addBehaviour(new BehaviourEntityWind(newEntity));
					newEntity.addBehaviour(new BehaviourBiomeSpreader(newEntity, world.getBiomeManager(), 1, 0.1f, BiomeType.Grassland));
					newEntity.addBehaviour(new BehaviourGrow(newEntity, 2, 0.01f));
					newEntity.addBehaviour(new BehaviourBreath(newEntity, false, 0.5f, Configs.BIOME_MAX_RANGE, 5));
					world.addEntity(newEntity);
				}
			}

			@Override
			public void onButtonClicked() {
				// TODO Auto-generated method stub
				
			}
		});
		
		MouseManager.addLeftButtonListener(new ButtonListener() {
			@Override
			public void onButtonReleased() {
				
			}
			
			@Override
			public void onButtonDown() {
				
			}

			@Override
			public void onButtonClicked() {
				Vector3f newPos = screenPicker.getMouseOnTerrain();
				if (newPos != null) {
					Entity newEntity = new Entity(modelTree, world,
							new Vector2f(newPos.x, newPos.z), 1f);
					newEntity.addBehaviour(new BehaviourEntityWind(newEntity));
					newEntity.addBehaviour(new BehaviourBiomeSpreader(newEntity, world.getBiomeManager(), 1, 0.5f, BiomeType.Grassland));
					world.addEntity(newEntity);
					newEntity.addBehaviour(new BehaviourGrow(newEntity, 2, 0.01f));
					newEntity.addBehaviour(new BehaviourBreath(newEntity, false, 0.5f, Configs.BIOME_MAX_RANGE, 5));


				}
			}
		});
		
		MouseManager.addRightButtonListener(new ButtonListener() {
			@Override
			public void onButtonReleased() {
				
			}
			
			@Override
			public void onButtonDown() {
				
			}

			@Override
			public void onButtonClicked() {
				Vector3f newPos = screenPicker.getMouseOnTerrain();
				if (newPos != null) {
					Entity newEntity = new Entity(modelTree, world,
							new Vector2f(newPos.x, newPos.z), 1f);
					newEntity.addBehaviour(new BehaviourEntityWind(newEntity));
					newEntity.addBehaviour(new BehaviourBiomeSpreader(newEntity, world.getBiomeManager(), 1, 0.5f, BiomeType.Beach));
					world.addEntity(newEntity);
					newEntity.addBehaviour(new BehaviourGrow(newEntity, 2, 0.01f));
					newEntity.addBehaviour(new BehaviourBreath(newEntity, false, 0.5f, Configs.BIOME_MAX_RANGE, 10));


				}
			}
		});
		
		while (!Display.isCloseRequested()) {
			ControllerManager.update();
			MouseManager.update();
			screenPicker.update();
			world.update(loader);
			camera.update(world);
			world.prepareWorld(renderer, loader);
			guiManager.update(renderer);
			renderer.render(light, camera, world);
			DisplayManager.updateDisplay();
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || ControllerManager.getB())
				break;
		}

		loader.cleanUp();

		DisplayManager.closeDisplay();
	}

}
