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
import gui.GuiManager;
import gui.GuiTexture;
import gui.View;
import input.ControllerManager;
import input.MouseManager;
import models.Model;
import models.RawModel;
import models.io.ModelLoader;
import renderEngine.CustomFileLoader;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
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

		ControllerManager.initControllers();
		MouseManager.init();

		RawModel rawModel = null;
		try {
			rawModel = ModelLoader.loadModel("dragon", loader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Model model = new Model(rawModel);
		for (int i = 0; i < 1; i++) {
			Entity plant = new Entity(model, world,
					new Vector2f((float) Math.random() * Configs.SIZE, (float) Math.random() * Configs.SIZE), 1f);
			world.addEntity(plant);
		}
		while (!Display.isCloseRequested()) {
			ControllerManager.update();
			MouseManager.update();
			world.update(loader);
			camera.update(world);
			world.prepareWorld(renderer, loader);
			guiManager.update(renderer);
			renderer.render(light, camera);

			DisplayManager.updateDisplay();
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || ControllerManager.getB())
				break;
		}

		loader.cleanUp();

		DisplayManager.closeDisplay();
	}

}
