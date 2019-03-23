package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import main.Configs;
import models.RawModel;
import shaders.SkyboxShader;

public class SkyboxRenderer {

	private static final float SIZE = (Configs.FAR_PLANE);
	
	private static final float[] VERTICES = {        
		    -SIZE,  SIZE, -SIZE * 0.6f,
		    -SIZE, -SIZE, -SIZE * 0.6f,
		    SIZE, -SIZE, -SIZE * 0.6f,
		     SIZE, -SIZE, -SIZE * 0.6f,
		     SIZE,  SIZE, -SIZE * 0.6f,
		    -SIZE,  SIZE, -SIZE * 0.6f,
//
//		    -SIZE, -SIZE,  SIZE,
//		    -SIZE, -SIZE, -SIZE,
//		    -SIZE,  SIZE, -SIZE,
//		    -SIZE,  SIZE, -SIZE,
//		    -SIZE,  SIZE,  SIZE,
//		    -SIZE, -SIZE,  SIZE,
//
//		     SIZE, -SIZE, -SIZE,
//		     SIZE, -SIZE,  SIZE,
//		     SIZE,  SIZE,  SIZE,
//		     SIZE,  SIZE,  SIZE,
//		     SIZE,  SIZE, -SIZE,
//		     SIZE, -SIZE, -SIZE,
//
//		    -SIZE, -SIZE,  SIZE,
//		    -SIZE,  SIZE,  SIZE,
//		     SIZE,  SIZE,  SIZE,
//		     SIZE,  SIZE,  SIZE,
//		     SIZE, -SIZE,  SIZE,
//		    -SIZE, -SIZE,  SIZE,
//
//		    -SIZE,  SIZE, -SIZE,
//		     SIZE,  SIZE, -SIZE,
//		     SIZE,  SIZE,  SIZE,
//		     SIZE,  SIZE,  SIZE,
//		    -SIZE,  SIZE,  SIZE,
//		    -SIZE,  SIZE, -SIZE,
//
//		    -SIZE, -SIZE, -SIZE,
//		    -SIZE, -SIZE,  SIZE,
//		     SIZE, -SIZE, -SIZE,
//		     SIZE, -SIZE, -SIZE,
//		    -SIZE, -SIZE,  SIZE,
//		     SIZE, -SIZE,  SIZE
	};
	
	
	private RawModel cube;
	private SkyboxShader shader;
	
	public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix) {
		cube = loader.loadToVAO(VERTICES, 3);
		shader = new SkyboxShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Camera camera, Vector4f clipPlane) {
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadViewMatrix(camera, SIZE);
		shader.loadSkyColors(Configs.SKY_COLOR_TOP, Configs.SKY_COLOR_BOTTOM);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
}
