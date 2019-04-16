package shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import timing.Timing;
import toolbox.Maths;

public class WaterShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/shaders/vertexShaderWater.glsl";
	private static final String FRAGMENT_FILE = "/shaders/fragmentShaderWater.glsl";
	private static final String GEOMETRY_FILE = "/shaders/geometryShaderWater.glsl";

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_waveTime;
	private int location_reflectionTexture;
	private int location_refractionTexture;
	private int location_cameraPosition;
	private int location_depthTexture;
	private int location_skyColor;
	private int location_density;
	private int location_gridCoords;
	private int location_skyBoxTexture;

	private float waveTime = 0;

	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, GEOMETRY_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_waveTime = super.getUniformLocation("waveTime");
		location_reflectionTexture = super.getUniformLocation("reflectionTexture");
		location_refractionTexture = super.getUniformLocation("refractionTexture");
		location_cameraPosition = super.getUniformLocation("cameraPos");
		location_depthTexture = super.getUniformLocation("depthTexture");
		location_skyColor = super.getUniformLocation("skyColor");
		location_density = super.getUniformLocation("density");
		location_gridCoords = super.getUniformLocation("gridCoords");
		location_skyBoxTexture = super.getUniformLocation("skyBoxTexture");
	}

	public void connectTextureUnits() {
		super.loadInt(location_reflectionTexture, 0);
		super.loadInt(location_refractionTexture, 1);
		super.loadInt(location_depthTexture, 2);
		super.loadInt(location_skyBoxTexture, 3);
	}

	public void loadGridCoords(Vector2f gridCoords) {
		super.loadVector(location_gridCoords, gridCoords);
	}
	
	public void loadDensity(float density) {
		super.loadFloat(location_density, density);
	}

	public void loadSkyColor(float red, float green, float blue) {
		super.loadVector(location_skyColor, new Vector3f(red, green, blue));
	}

	public void loadFlowRate() {
		super.loadFloat(location_waveTime, Timing.getInGameSeconds() * 0.003f);
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix4f = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix4f);
		super.loadVector(location_cameraPosition, camera.getPosition());
	}

}
