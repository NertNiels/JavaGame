package world;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import entities.Light;
import main.Configs;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrain.Terrain;
import timing.Timing;
import water.WaterTile;

public class World {

	private HeightGenerator heightGenerator;
	private BiomeManager biomeManager;
	private int biomeTexture;
	private ArrayList<Terrain> loadedTerrains;
	private ArrayList<WaterTile> waterTiles; 
	private ArrayList<Entity> entities;
	private Vector3f playerPosition;

	private float planeWidth, planeHeight;
	private int currentGridX, currentGridZ;
	
	private Light sun;
	
	public static World world;

	private World(HeightGenerator heightGenerator, Loader loader) {
		this.heightGenerator = heightGenerator;
		biomeManager = new BiomeManager(loader);
		loadedTerrains = new ArrayList<Terrain>();
		waterTiles = new ArrayList<WaterTile>();
		entities = new ArrayList<Entity>();
		playerPosition = new Vector3f();
		world = this;
		sun = new Light(new Vector3f(), Configs.SUN_LIGHT_COLOR, Configs.SUN_BIAS);
	}
	
	private World(HeightGenerator heightGenerator, Loader loader, Light sun) {
		this.heightGenerator = heightGenerator;
		biomeManager = new BiomeManager(loader);
		loadedTerrains = new ArrayList<Terrain>();
		waterTiles = new ArrayList<WaterTile>();
		entities = new ArrayList<Entity>();
		playerPosition = new Vector3f();
		world = this;
		this.sun = sun;
	}

	public void update(Loader loader) {
		int gridX = (int)(playerPosition.x / (float)Configs.SIZE);
		if(playerPosition.x < 0) gridX--;
		int gridZ = (int)(playerPosition.z / (float)Configs.SIZE);
		if(playerPosition.z < 0) gridZ--;
		loadTerrains(gridX, gridZ, loader);
		currentGridX = gridX;
		currentGridZ = gridZ;
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}
		biomeTexture = biomeManager.getTexture(loader);
		
		calculateSunPosition();
	}
	
	private void calculateSunPosition() {
		double hourOfDay = Timing.getInGameHours()%24;
		double currentAngle = -(hourOfDay/24d*360)-90;
		double y = 10000 * Math.sin(Math.toRadians(currentAngle));
		double z = 10000 * Math.cos(Math.toRadians(currentAngle));
		sun.setPostition(new Vector3f(0, (float)y, (float)z));
	}
	
	public void prepareWorld(MasterRenderer renderer, Loader loader) {
		for (Terrain terrain : loadedTerrains) {
			renderer.processTerrain(terrain);
		}
		
		for (Entity entity : entities) {
			renderer.processEntity(entity);
		}
		
		for(WaterTile waterTile : waterTiles) {
			renderer.processWater(waterTile);
		}
	}

	private void initialLoad(int gridX, int gridZ, Loader loader) {
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				loadTerrainAt(gridX + i, gridZ + j, loader);
				loadWaterAt(gridX + i, gridZ + j, loader);
			}
		}
	}
	
	private void loadTerrains(int gridX, int gridZ, Loader loader) {
		if (currentGridX == gridX && currentGridZ == gridZ)
			return;
		
		ArrayList<Vector2f> toBeLoaded = new ArrayList<Vector2f>();
		toBeLoaded.add(new Vector2f(gridX - 1, gridZ - 1));
		toBeLoaded.add(new Vector2f(gridX - 1, gridZ));
		toBeLoaded.add(new Vector2f(gridX - 1, gridZ + 1));
		toBeLoaded.add(new Vector2f(gridX, gridZ - 1));
		toBeLoaded.add(new Vector2f(gridX, gridZ));
		toBeLoaded.add(new Vector2f(gridX, gridZ + 1));
		toBeLoaded.add(new Vector2f(gridX + 1, gridZ - 1));
		toBeLoaded.add(new Vector2f(gridX + 1, gridZ));
		toBeLoaded.add(new Vector2f(gridX + 1, gridZ + 1));

		for (int t = 0; t < loadedTerrains.size(); t++) {
			boolean stayLoaded = false;
			for (int g = 0; g < toBeLoaded.size(); g++) {
				if (loadedTerrains.get(t).getGridX() == toBeLoaded.get(g).x && loadedTerrains.get(t).getGridZ() == toBeLoaded.get(g).y) {
					toBeLoaded.remove(g);
					stayLoaded = true;
					break;
				}
			}
			if (stayLoaded)
				continue;
			loadedTerrains.get(t).cleanUp();
			loadedTerrains.remove(t);
			waterTiles.remove(t);
			t--;
		}
		
		for(Vector2f gridPos:toBeLoaded) {
			Terrain newTerrain = new Terrain((int)gridPos.x, (int)gridPos.y, loader, heightGenerator);
			loadedTerrains.add(newTerrain);
			WaterTile newWaterTile = new WaterTile((int)gridPos.x, (int)gridPos.y, Configs.WATER_HEIGHT, loader);
			waterTiles.add(newWaterTile);
		}
	}

	private void loadTerrainAt(int gridX, int gridZ, Loader loader) {
		Terrain terrain = new Terrain(gridX, gridZ, loader, heightGenerator);
		loadedTerrains.add(terrain);
	}
	
	private void loadWaterAt(int gridX, int gridZ, Loader loader) {
		WaterTile waterTile = new WaterTile(gridX, gridZ, Configs.WATER_HEIGHT, loader);
		waterTiles.add(waterTile);
	}
	
	private Terrain getTerrainAt(int gridX, int gridZ) {
		for(Terrain terrain:loadedTerrains) {
			if(terrain.getGridX() == gridX && terrain.getGridZ() == gridZ) {
				return terrain;
			}
		}
		return null;
	}

	public static World loadWorld(String worldName) {
		throw new UnsupportedOperationException("This function is not yet implemented");
	}

	public static World generateWorld(HeightGenerator heightGenerator,
			int planeWidth, int planeHeight, Loader loader) {
		World world = new World(heightGenerator, loader);
		world.planeWidth = planeWidth;
		world.planeHeight = planeHeight;
		world.initialLoad(0, 0, loader);
		return world;
	}

	public void setPlayerPosition(Vector3f playerPosition) {
		this.playerPosition = playerPosition;
	}
	
	public Vector3f getGroundRotationAt(float x, float z) {
		int gridX = (int) (x / (float)planeWidth);
		int gridZ = (int) (z / (float)planeHeight);
		
		Terrain terrain = getTerrainAt(gridX, gridZ);
		return terrain.getNormalAt(x % Configs.SIZE, z % Configs.SIZE);
	}

	public Terrain getCurrentTerrain() {
		return getTerrainAt(currentGridX, currentGridZ);
	}
	
	public Terrain getTerrainAt(float x, float z) {
		int gridX = (int)(x / (float)Configs.SIZE);
		if(x < 0) gridX--;
		int gridZ = (int)(z / (float)Configs.SIZE);
		if(z < 0) gridZ--;
		return getTerrainAt(gridX, gridZ);
	}
	
	public float getHeightAt(float x, float z) {
		int gridX = (int)(x / (float)Configs.SIZE);
		if(x < 0) gridX--;
		int gridZ = (int)(z / (float)Configs.SIZE);
		if(z < 0) gridZ--;
		
		Terrain terrain = getTerrainAt(gridX, gridZ);
		
		return terrain.getHeightAt(x - terrain.getX(), z - terrain.getZ());
	}
	
	public Vector3f getNormalAt(float x, float z) {
		int gridX = (int)(x / (float)Configs.SIZE);
		if(x < 0) gridX--;
		int gridZ = (int)(z / (float)Configs.SIZE);
		if(z < 0) gridZ--;
		
		Terrain terrain = getTerrainAt(gridX, gridZ);
		
		return terrain.getNormalAt(x - terrain.getX(), z - terrain.getZ());
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public int getBiomeTexture() {
		return biomeTexture;
	}
	
	public BiomeManager getBiomeManager() {
		return biomeManager;
	}

	public int getNumberOfEntitiesInRange(Vector2f origin, float range) {
		range *= range;
		int sum = 0;
		for(Entity entity:entities) {
			Vector2f position = new Vector2f(entity.getPosition().x, entity.getPosition().z);
			float distance = Vector2f.sub(position, origin, null).lengthSquared();
			if(distance < range) sum++;
		}
		return sum;
	}
	
	public Light getSun() {
		return sun;
	}
	

}
