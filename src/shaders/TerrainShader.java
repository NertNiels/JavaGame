package shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Light;
import main.Configs;
import toolbox.Maths;

public class TerrainShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/shaders/vertexShaderTerrain.glsl";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShaderTerrain.glsl";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPostion;
	private int location_lightColor;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColor;
	private int location_density;
	private int location_plane;
	private int location_biomeTexture;
	private int location_size;
	private int location_skyBoxTexture;
	
	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "normal");
		super.bindAttribute(2, "color");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightPostion = super.getUniformLocation("lightPosition");
		location_lightColor = super.getUniformLocation("lightColor");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_skyColor = super.getUniformLocation("skyColor");
		location_density = super.getUniformLocation("density");
		location_plane = super.getUniformLocation("plane");
		location_skyBoxTexture = super.getUniformLocation("skyBoxTexture");
		location_biomeTexture = super.getUniformLocation("biomeTexture");
		location_size = super.getUniformLocation("size");
	}
	
	public void loadClipPlane(Vector4f plane) {
		super.loadVector(location_plane, plane);
	}
	
	public void loadDensity(float density) {
		super.loadFloat(location_density, density);
	}
	
	public void loadSkyColor(float red, float green, float blue) {
		super.loadVector(location_skyColor, new Vector3f(red, green, blue));
	}
	
	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadLight(Light light) {
		super.loadVector(location_lightPostion, light.getPostition());
		super.loadVector(location_lightColor, light.getColor());
		
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix4f = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix4f);
	}
	
	public void loadSize() {
		super.loadFloat(location_size, (float)Configs.SIZE);
	}

	public void connectTextureUnits() {
		super.loadInt(location_biomeTexture, 0);
		super.loadInt(location_skyBoxTexture, 1);
	}

}
