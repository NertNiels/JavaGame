package main;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import entities.EntityLoader;
import entities.EntityType;
import entities.Player;
import entities.camera.Camera;
import entities.camera.CameraFirstPerson;
import entities.camera.CameraThirdPerson;
import gui.GuiManager;
import input.ButtonListener;
import input.ControllerManager;
import input.MouseManager;
import models.Model;
import models.RawModel;
import models.io.ModelLoader;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import timing.Timing;
import toolbox.ScreenPicker;
import world.HeightGenerator;
import world.World;

public class MainGameLoop {
	
	public static Loader loader;
	public static Player localPlayer;

	public static void main(String[] args) {
		DisplayManager.createDisplay();

		loader = new Loader();

		Camera camera = new CameraThirdPerson(Configs.SIZE / 2, 100, Configs.SIZE / 2);
		camera = new CameraFirstPerson(Configs.SIZE / 2, 100, Configs.SIZE / 2);


		HeightGenerator heightGenerator = new HeightGenerator(Configs.SCALE, Configs.SEED, Configs.OCTAVES,
				Configs.PERSISTANCE, Configs.LACUNARITY, Configs.OFFSET);
		heightGenerator.generateWorldMap(Configs.GRID_WORLD_WIDTH * Configs.VERTEX_COUNT,
				Configs.GRID_WORLD_HEIGHT * Configs.VERTEX_COUNT);
		World world = World.generateWorld(heightGenerator, Configs.SIZE, Configs.SIZE, loader);

		MasterRenderer renderer = new MasterRenderer(loader);

		GuiManager guiManager = new GuiManager(loader, camera);

		ControllerManager.initControllers(guiManager);
		Timing.initControls();
		MouseManager.init(renderer, camera, world);
		ScreenPicker screenPicker = new ScreenPicker(camera,renderer.getProjectionMatrix(), world);
		
		EntityLoader.init(loader);
		
		RawModel treeModel = null;
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
			public boolean onButtonReleased() {
				return false;
			}
			
			@Override
			public boolean onButtonDown() {
				Vector3f newPos = screenPicker.getMiddleOnTerrain();
				if (newPos != null) {
					Entity newEntity = EntityLoader.loadEntity(EntityType.GrassGrassland, new Vector2f(newPos.x, newPos.z), 1f);
					world.addEntity(newEntity);
				}
				return true;
			}

			@Override
			public boolean onButtonClicked() {
				return false;
			}
		});
		
		localPlayer = new Player(world, new Vector2f(Configs.SIZE/2f, Configs.SIZE/2f), camera);
		
		MouseManager.addLeftButtonListener(new ButtonListener() {
			@Override
			public boolean onButtonReleased() {
				return false;
			}
			
			@Override
			public boolean onButtonDown() {
				return false;
			}

			@Override
			public boolean onButtonClicked() {
				Vector3f newPos = screenPicker.getMouseOnTerrain();
				if (newPos != null) {
					Entity newEntity = EntityLoader.loadEntity(EntityType.OakTree, new Vector2f(newPos.x, newPos.z), 1f);
					world.addEntity(newEntity);
				}
				return false;
			}
		});
		
		MouseManager.addRightButtonListener(new ButtonListener() {
			@Override
			public boolean onButtonReleased() {
				return false;
			}
			
			@Override
			public boolean onButtonDown() {
				return false;
			}

			@Override
			public boolean onButtonClicked() {
				Vector3f newPos = screenPicker.getMouseOnTerrain();
				if (newPos != null) {
					Entity newEntity = EntityLoader.loadEntity(EntityType.GrassGrassland, new Vector2f(newPos.x, newPos.z), 1f);
					world.addEntity(newEntity);
				}
				return true;
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
			renderer.render(world.getSun(), camera, world);
			DisplayManager.updateDisplay();
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || ControllerManager.getB())
				break;
		}

		loader.cleanUp();
		MouseManager.destroy();
		DisplayManager.closeDisplay();
	}

}
