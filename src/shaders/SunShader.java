package shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import toolbox.Maths;

public class SunShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/shaders/vertexShaderSun.glsl";
    private static final String FRAGMENT_FILE = "/shaders/fragmentShaderSun.glsl";
     
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_plane;
    private int location_transformationMatrix;
    
    public SunShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }
    
    public void loadTransformationMatrix(float hourOfDay, float SIZE) {
    	double rotation = (hourOfDay / 24f * Math.PI * 2);
    	Matrix4f transformationMatrix = Maths.createTransformationmatrix(new Vector3f(0, 0, 0), hourOfDay/24f*360,
				0, 0, 1);
    	super.loadMatrix(location_transformationMatrix, transformationMatrix);
    }
 
    public void loadViewMatrix(Camera camera, float SIZE){
    	Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		
		Matrix4f.rotate((float)Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), matrix, matrix);
		
		Vector3f negCamPos = new Vector3f(0, -camera.getPosition().y, 0);
		Matrix4f.translate(negCamPos, matrix, matrix);
        super.loadMatrix(location_viewMatrix, matrix);
    }
    
    public void loadClipPlane(Vector4f plane) {
		super.loadVector(location_plane, plane);
	}
     
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_plane = super.getUniformLocation("plane");
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }
 
    @Override
    protected void bindAttributes() {
    	super.bindAttribute(0, "position");
        super.bindAttribute(1, "color");
    }
	
}
