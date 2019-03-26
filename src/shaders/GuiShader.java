package shaders;
 
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
  
public class GuiShader extends ShaderProgram{
     
    private static final String VERTEX_FILE = "src/shaders/vertexShaderGui.glsl";
    private static final String FRAGMENT_FILE = "src/shaders/fragmentShaderGui.glsl";
     
    private int location_transformationMatrix;
    private int location_blend_color;
    private int location_opacity;
 
    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadTransformation(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }
 
    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_blend_color = super.getUniformLocation("blend_color");
        location_opacity = super.getUniformLocation("opacity");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
    
    public void loadBlendColor(Vector4f color) {
    	super.loadVector(location_blend_color, color);
    }
    
    public void loadOpacity(float opacity) {
    	super.loadFloat(location_opacity, opacity);
    }
}