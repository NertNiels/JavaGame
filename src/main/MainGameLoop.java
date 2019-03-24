package main;

import java.io.IOException;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import entities.Camera;
import entities.Entity;
import entities.Light;
import inputManager.Keyboard;
import models.Model;
import models.RawModel;
import models.io.ModelLoader;
import renderEngine.CustomFileLoader;
import renderEngine.DisplayManager2;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import world.HeightGenerator;
import world.World;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager2.createDisplay();

		Loader loader = new Loader();

		Light light = new Light(new Vector3f(0, 200000, 0), new Vector3f(0.9f, 0.9f, 0.9f));

		Camera camera = new Camera(Configs.SIZE / 2, 100, Configs.SIZE / 2);

		HeightGenerator heightGenerator = new HeightGenerator(Configs.SCALE, Configs.SEED, Configs.OCTAVES,
				Configs.PERSISTANCE, Configs.LACUNARITY, Configs.OFFSET);
		heightGenerator.generateWorldMap(Configs.GRID_WORLD_WIDTH * Configs.VERTEX_COUNT,
				Configs.GRID_WORLD_HEIGHT * Configs.VERTEX_COUNT);
		World world = World.generateWorld(heightGenerator, Configs.SIZE, Configs.SIZE, loader);

		MasterRenderer renderer = new MasterRenderer(loader);


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

		while (!GLFW.glfwWindowShouldClose(DisplayManager2.getWindow())) {

			world.update(loader);
			camera.update(world);
			world.prepareWorld(renderer, loader);
			renderer.render(light, camera);

			DisplayManager2.updateDisplay();
			if (Keyboard.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
				break;
			}
		}

		loader.cleanUp();
		
		DisplayManager2.closeDisplay();
	}

}
