package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import gui.View;
import main.Configs;
import models.Model;
import shaders.GuiShader;
import shaders.StaticShader;
import shaders.TerrainShader;
import shaders.WaterShader;
import terrain.Terrain;
import water.WaterFrameBuffers;
import water.WaterTile;
import world.World;

public class MasterRenderer {

	private Matrix4f projectionMatrix;

	private StaticShader entityShader = new StaticShader();
	private EntityRenderer entityRenderer;

	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	private WaterShader waterShader = new WaterShader();
	private WaterRenderer waterRenderer;
	
	private GuiShader guiShader = new GuiShader();
	private GuiRenderer guiRenderer;
	
	private SkyboxRenderer skyboxRenderer;

	private Map<Model, ArrayList<Entity>> entities = new HashMap<Model, ArrayList<Entity>>();
	private ArrayList<Terrain> terrains = new ArrayList<Terrain>();
	private ArrayList<WaterTile> waters = new ArrayList<WaterTile>();
	private ArrayList<View> guis = new ArrayList<View>();
	
	WaterFrameBuffers fbos = new WaterFrameBuffers();
	Fbo skyBoxFbo = new Fbo(Configs.SCREEN_WIDTH, Configs.SCREEN_HEIGHT, Fbo.NONE);


	public MasterRenderer(Loader loader) {
		createProjectionMatrix();
		entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix, skyBoxFbo);
		waterRenderer = new WaterRenderer(waterShader, projectionMatrix, loader, fbos, skyBoxFbo);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
		guiRenderer = new GuiRenderer(loader, guiShader);
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public void render(Light sun, Camera camera, World world) {
		prepare();
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		
		skyBoxFbo.bindFrameBuffer();
		skyboxRenderer.render(camera, new Vector4f(0, -1, 0, 10000000));
		skyBoxFbo.unbindFrameBuffer();
		
		fbos.bindReflectionFrameBuffer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		camera.applyWater(Configs.WATER_HEIGHT);
		renderScene(sun, camera, new Vector4f(0, 1, 0, -Configs.WATER_HEIGHT + 0.1f), world);
		camera.applyWater(Configs.WATER_HEIGHT);
		
		fbos.bindRefractionFrameBuffer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		renderScene(sun, camera, new Vector4f(0, -1, 0, Configs.WATER_HEIGHT + 2), world);
		fbos.unbindCurrentFrameBuffer();
		
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		renderScene(sun, camera, new Vector4f(0, -1, 0, 10000000), world);
		
		if (Keyboard.isKeyDown(Keyboard.KEY_TAB))
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		waterShader.start();
		waterShader.loadViewMatrix(camera);
		waterRenderer.render(waters);
		waterShader.stop();
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		
		guiShader.start();
		guiRenderer.render(guis);
		guiShader.stop();
		
		terrains.clear();
		entities.clear();
		waters.clear();
		guis.clear();
	}
	
	public void renderScene(Light sun, Camera camera, Vector4f clipPlane, World world) {
		entityShader.start();
		entityShader.loadClipPlane(clipPlane);
		entityShader.loadLight(sun);
		entityShader.loadViewMatrix(camera);
		entityRenderer.render(entities);
		entityShader.stop();

		terrainShader.start();
		terrainShader.loadClipPlane(clipPlane);
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains, world);
		terrainShader.stop();
		skyboxRenderer.render(camera, clipPlane);

	}

	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void processWater(WaterTile water) {
		waters.add(water);
	}

	public void processEntity(Entity entity) {
		Model entityModel = entity.getModel();
		ArrayList<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			ArrayList<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void processGui(View gui) {
		guis.add(gui);
	}

	public void cleanUp() {
		fbos.cleanUp();
		skyBoxFbo.cleanUp();
		guiShader.cleanUp();
		waterShader.cleanUp();
		entityShader.cleanUp();
		terrainShader.cleanUp();
		skyboxRenderer.cleanUp();
	}

	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(Configs.SKY_COLOR_TOP.x, Configs.SKY_COLOR_TOP.y, Configs.SKY_COLOR_TOP.z, 1);

		GL32.glProvokingVertex(GL32.GL_FIRST_VERTEX_CONVENTION);
	}

	private void createProjectionMatrix() {
		float aspect_ratio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (1f / (float) Math.tan(Math.toRadians(Configs.FOV / 2f))) * aspect_ratio;
		float x_scale = y_scale / aspect_ratio;
		float frustum_length = Configs.FAR_PLANE - Configs.NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((Configs.FAR_PLANE + Configs.NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * Configs.NEAR_PLANE * Configs.FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
	
	public WaterFrameBuffers getWaterFrameBuffers() {
		return fbos;
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
}
