package shaders;
 
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.camera.Camera;
import shaders.ShaderProgram;
 
public class SkyboxShader extends ShaderProgram {
 
    private static final String VERTEX_FILE = "/shaders/vertexShaderSkybox.glsl";
    private static final String FRAGMENT_FILE = "/shaders/fragmentShaderSkybox.glsl";
     
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_skyTopColor;
    private int location_skyBottomColor;
    private int location_plane;
    private int location_SIZE;
    
    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }
 
    public void loadViewMatrix(Camera camera, float SIZE){
    	Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		
		Matrix4f.rotate((float)Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), matrix, matrix);
//		Matrix4f.rotate((float)Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), matrix, matrix);
		
		Vector3f negCamPos = new Vector3f(0, -camera.getPosition().y, 0);
		Matrix4f.translate(negCamPos, matrix, matrix);
		
		super.loadFloat(location_SIZE, SIZE);
        super.loadMatrix(location_viewMatrix, matrix);
    }
    
    public void loadClipPlane(Vector4f plane) {
		super.loadVector(location_plane, plane);
	}
    
    public void loadSkyColors(Vector3f skyTopColor, Vector3f skyBottomColor) {
    	super.loadVector(location_skyTopColor, skyTopColor);
    	super.loadVector(location_skyBottomColor, skyBottomColor);
    }
     
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_skyTopColor = super.getUniformLocation("skyTopColor");
        location_skyBottomColor = super.getUniformLocation("skyBottomColor");
        location_plane = super.getUniformLocation("plane");
    }
 
    @Override
    protected void bindAttributes() {
    	super.bindAttribute(0, "position");
        super.bindAttribute(1, "color");
    }
 
}